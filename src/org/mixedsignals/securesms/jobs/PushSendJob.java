package org.mixedsignals.securesms.jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.annimon.stream.Stream;

import org.greenrobot.eventbus.EventBus;
import org.signal.libsignal.metadata.certificate.InvalidCertificateException;
import org.signal.libsignal.metadata.certificate.SenderCertificate;
import org.mixedsignals.securesms.ApplicationContext;
import org.mixedsignals.securesms.TextSecureExpiredException;
import org.mixedsignals.securesms.attachments.Attachment;
import org.mixedsignals.securesms.contactshare.Contact;
import org.mixedsignals.securesms.contactshare.ContactModelMapper;
import org.mixedsignals.securesms.crypto.ProfileKeyUtil;
import org.mixedsignals.securesms.database.Address;
import org.mixedsignals.securesms.database.DatabaseFactory;
import org.mixedsignals.securesms.events.PartProgressEvent;
import org.mixedsignals.securesms.jobmanager.Job;
import org.mixedsignals.securesms.jobmanager.impl.NetworkConstraint;
import org.mixedsignals.securesms.logging.Log;
import org.mixedsignals.securesms.mms.DecryptableStreamUriLoader;
import org.mixedsignals.securesms.mms.OutgoingMediaMessage;
import org.mixedsignals.securesms.mms.PartAuthority;
import org.mixedsignals.securesms.notifications.MessageNotifier;
import org.mixedsignals.securesms.recipients.Recipient;
import org.mixedsignals.securesms.util.Base64;
import org.mixedsignals.securesms.util.BitmapDecodingException;
import org.mixedsignals.securesms.util.BitmapUtil;
import org.mixedsignals.securesms.util.MediaUtil;
import org.mixedsignals.securesms.util.TextSecurePreferences;
import org.mixedsignals.securesms.util.Util;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.crypto.UnidentifiedAccessPair;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachment;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentPointer;
import org.whispersystems.signalservice.api.messages.SignalServiceDataMessage;
import org.whispersystems.signalservice.api.messages.SignalServiceDataMessage.Preview;
import org.whispersystems.signalservice.api.messages.multidevice.SentTranscriptMessage;
import org.whispersystems.signalservice.api.messages.multidevice.SignalServiceSyncMessage;
import org.whispersystems.signalservice.api.messages.shared.SharedContact;
import org.whispersystems.signalservice.api.push.SignalServiceAddress;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class PushSendJob extends SendJob {

  private static final String TAG                           = PushSendJob.class.getSimpleName();
  private static final long   CERTIFICATE_EXPIRATION_BUFFER = TimeUnit.DAYS.toMillis(1);

  protected PushSendJob(Job.Parameters parameters) {
    super(parameters);
  }

  protected static Job.Parameters constructParameters(Address destination) {
    return new Parameters.Builder()
                         .setQueue(destination.serialize())
                         .addConstraint(NetworkConstraint.KEY)
                         .setLifespan(TimeUnit.DAYS.toMillis(1))
                         .setMaxAttempts(Parameters.UNLIMITED)
                         .build();
  }

  @Override
  protected final void onSend() throws Exception {
    if (TextSecurePreferences.getSignedPreKeyFailureCount(context) > 5) {
      ApplicationContext.getInstance(context)
                        .getJobManager()
                        .add(new RotateSignedPreKeyJob());

      throw new TextSecureExpiredException("Too many signed prekey rotation failures");
    }

    onPushSend();
  }

  @Override
  public void onRetry() {
    super.onRetry();
    Log.i(TAG, "onRetry()");

    if (getRunAttempt() > 1) {
      Log.i(TAG, "Scheduling service outage detection job.");
      ApplicationContext.getInstance(context).getJobManager().add(new ServiceOutageDetectionJob());
    }
  }

  protected Optional<byte[]> getProfileKey(@NonNull Recipient recipient) {
    if (!recipient.resolve().isSystemContact() && !recipient.resolve().isProfileSharing()) {
      return Optional.absent();
    }

    return Optional.of(ProfileKeyUtil.getProfileKey(context));
  }

  protected SignalServiceAddress getPushAddress(Address address) {
    String relay = null;
    return new SignalServiceAddress(address.toPhoneString(), Optional.fromNullable(relay));
  }

  protected List<SignalServiceAttachment> getAttachmentsFor(List<Attachment> parts) {
    List<SignalServiceAttachment> attachments = new LinkedList<>();

    for (final Attachment attachment : parts) {
      SignalServiceAttachment converted = getAttachmentFor(attachment);
      if (converted != null) {
        attachments.add(converted);
      }
    }

    return attachments;
  }

  protected SignalServiceAttachment getAttachmentFor(Attachment attachment) {
    try {
      if (attachment.getDataUri() == null || attachment.getSize() == 0) throw new IOException("Assertion failed, outgoing attachment has no data!");
      InputStream is = PartAuthority.getAttachmentStream(context, attachment.getDataUri());
      return SignalServiceAttachment.newStreamBuilder()
                                    .withStream(is)
                                    .withContentType(attachment.getContentType())
                                    .withLength(attachment.getSize())
                                    .withFileName(attachment.getFileName())
                                    .withVoiceNote(attachment.isVoiceNote())
                                    .withWidth(attachment.getWidth())
                                    .withHeight(attachment.getHeight())
                                    .withCaption(attachment.getCaption())
                                    .withListener((total, progress) -> EventBus.getDefault().postSticky(new PartProgressEvent(attachment, total, progress)))
                                    .build();
    } catch (IOException ioe) {
      Log.w(TAG, "Couldn't open attachment", ioe);
    }
    return null;
  }

  protected @NonNull List<SignalServiceAttachment> getAttachmentPointersFor(List<Attachment> attachments) {
    return Stream.of(attachments).map(this::getAttachmentPointerFor).filter(a -> a != null).toList();
  }

  protected @Nullable SignalServiceAttachment getAttachmentPointerFor(Attachment attachment) {
    if (TextUtils.isEmpty(attachment.getLocation())) {
      Log.w(TAG, "empty content id");
      return null;
    }

    if (TextUtils.isEmpty(attachment.getKey())) {
      Log.w(TAG, "empty encrypted key");
      return null;
    }

    try {
      long   id  = Long.parseLong(attachment.getLocation());
      byte[] key = Base64.decode(attachment.getKey());

      return new SignalServiceAttachmentPointer(id,
                                                attachment.getContentType(),
                                                key,
                                                Optional.of(Util.toIntExact(attachment.getSize())),
                                                Optional.absent(),
                                                attachment.getWidth(),
                                                attachment.getHeight(),
                                                Optional.fromNullable(attachment.getDigest()),
                                                Optional.fromNullable(attachment.getFileName()),
                                                attachment.isVoiceNote(),
                                                Optional.fromNullable(attachment.getCaption()));
    } catch (IOException | ArithmeticException e) {
      Log.w(TAG, e);
      return null;
    }
  }

  protected static void notifyMediaMessageDeliveryFailed(Context context, long messageId) {
    long      threadId  = DatabaseFactory.getMmsDatabase(context).getThreadIdForMessage(messageId);
    Recipient recipient = DatabaseFactory.getThreadDatabase(context).getRecipientForThreadId(threadId);

    if (threadId != -1 && recipient != null) {
      MessageNotifier.notifyMessageDeliveryFailed(context, recipient, threadId);
    }
  }

  protected Optional<SignalServiceDataMessage.Quote> getQuoteFor(OutgoingMediaMessage message) {
    if (message.getOutgoingQuote() == null) return Optional.absent();

    long                                                  quoteId          = message.getOutgoingQuote().getId();
    String                                                quoteBody        = message.getOutgoingQuote().getText();
    Address                                               quoteAuthor      = message.getOutgoingQuote().getAuthor();
    List<SignalServiceDataMessage.Quote.QuotedAttachment> quoteAttachments = new LinkedList<>();

    for (Attachment attachment : message.getOutgoingQuote().getAttachments()) {
      BitmapUtil.ScaleResult  thumbnailData = null;
      SignalServiceAttachment thumbnail     = null;

      try {
        if (MediaUtil.isImageType(attachment.getContentType()) && attachment.getDataUri() != null) {
          thumbnailData = BitmapUtil.createScaledBytes(context, new DecryptableStreamUriLoader.DecryptableUri(attachment.getDataUri()), 100, 100, 500 * 1024);
        } else if (MediaUtil.isVideoType(attachment.getContentType()) && attachment.getThumbnailUri() != null) {
          thumbnailData = BitmapUtil.createScaledBytes(context, new DecryptableStreamUriLoader.DecryptableUri(attachment.getThumbnailUri()), 100, 100, 500 * 1024);
        }

        if (thumbnailData != null) {
          thumbnail = SignalServiceAttachment.newStreamBuilder()
                                             .withContentType("image/jpeg")
                                             .withWidth(thumbnailData.getWidth())
                                             .withHeight(thumbnailData.getHeight())
                                             .withLength(thumbnailData.getBitmap().length)
                                             .withStream(new ByteArrayInputStream(thumbnailData.getBitmap()))
                                             .build();
        }

        quoteAttachments.add(new SignalServiceDataMessage.Quote.QuotedAttachment(attachment.getContentType(),
                                                                                 attachment.getFileName(),
                                                                                 thumbnail));
      } catch (BitmapDecodingException e) {
        Log.w(TAG, e);
      }
    }

    return Optional.of(new SignalServiceDataMessage.Quote(quoteId, new SignalServiceAddress(quoteAuthor.serialize()), quoteBody, quoteAttachments));
  }

  List<SharedContact> getSharedContactsFor(OutgoingMediaMessage mediaMessage) {
    List<SharedContact> sharedContacts = new LinkedList<>();

    for (Contact contact : mediaMessage.getSharedContacts()) {
      SharedContact.Builder builder = ContactModelMapper.localToRemoteBuilder(contact);
      SharedContact.Avatar  avatar  = null;

      if (contact.getAvatar() != null && contact.getAvatar().getAttachment() != null) {
        avatar = SharedContact.Avatar.newBuilder().withAttachment(getAttachmentFor(contact.getAvatarAttachment()))
                                                  .withProfileFlag(contact.getAvatar().isProfile())
                                                  .build();
      }

      builder.setAvatar(avatar);
      sharedContacts.add(builder.build());
    }

    return sharedContacts;
  }

  List<Preview> getPreviewsFor(OutgoingMediaMessage mediaMessage) {
    return Stream.of(mediaMessage.getLinkPreviews()).map(lp -> {
      SignalServiceAttachment attachment = lp.getThumbnail().isPresent() ? getAttachmentPointerFor(lp.getThumbnail().get()) : null;
      return new Preview(lp.getUrl(), lp.getTitle(), Optional.fromNullable(attachment));
    }).toList();
  }

  protected void rotateSenderCertificateIfNecessary() throws IOException {
    try {
      byte[] certificateBytes = TextSecurePreferences.getUnidentifiedAccessCertificate(context);

      if (certificateBytes == null) {
        throw new InvalidCertificateException("No certificate was present.");
      }

      SenderCertificate certificate = new SenderCertificate(certificateBytes);

      if (System.currentTimeMillis() > (certificate.getExpiration() - CERTIFICATE_EXPIRATION_BUFFER)) {
        throw new InvalidCertificateException("Certificate is expired, or close to it. Expires on: " + certificate.getExpiration() + ", currently: " + System.currentTimeMillis());
      }

      Log.d(TAG, "Certificate is valid.");
    } catch (InvalidCertificateException e) {
      Log.w(TAG, "Certificate was invalid at send time. Fetching a new one.", e);
      RotateCertificateJob certificateJob = new RotateCertificateJob(context);
      ApplicationContext.getInstance(context).injectDependencies(certificateJob);
      certificateJob.onRun();
    }
  }

  protected SignalServiceSyncMessage buildSelfSendSyncMessage(@NonNull Context context, @NonNull SignalServiceDataMessage message, Optional<UnidentifiedAccessPair> syncAccess) {
    String                localNumber = TextSecurePreferences.getLocalNumber(context);
    SentTranscriptMessage transcript  = new SentTranscriptMessage(localNumber,
                                                                  message.getTimestamp(),
                                                                  message,
                                                                  message.getExpiresInSeconds(),
                                                                  Collections.singletonMap(localNumber, syncAccess.isPresent()));
    return SignalServiceSyncMessage.forSentTranscript(transcript);
  }


  protected abstract void onPushSend() throws Exception;
}
