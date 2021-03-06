package org.mixedsignals.securesms.jobs;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.mixedsignals.securesms.attachments.Attachment;
import org.mixedsignals.securesms.attachments.AttachmentId;
import org.mixedsignals.securesms.attachments.DatabaseAttachment;
import org.mixedsignals.securesms.attachments.PointerAttachment;
import org.mixedsignals.securesms.database.AttachmentDatabase;
import org.mixedsignals.securesms.database.DatabaseFactory;
import org.mixedsignals.securesms.dependencies.InjectableType;
import org.mixedsignals.securesms.events.PartProgressEvent;
import org.mixedsignals.securesms.jobmanager.Data;
import org.mixedsignals.securesms.jobmanager.Job;
import org.mixedsignals.securesms.jobmanager.impl.NetworkConstraint;
import org.mixedsignals.securesms.logging.Log;
import org.mixedsignals.securesms.mms.MediaConstraints;
import org.mixedsignals.securesms.mms.MediaStream;
import org.mixedsignals.securesms.mms.MmsException;
import org.mixedsignals.securesms.mms.PartAuthority;
import org.mixedsignals.securesms.transport.UndeliverableMessageException;
import org.mixedsignals.securesms.util.MediaUtil;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachment;
import org.whispersystems.signalservice.api.messages.SignalServiceAttachmentPointer;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.SSLException;

public class AttachmentUploadJob extends BaseJob implements InjectableType {

  public static final String KEY = "AttachmentUploadJob";

  private static final String TAG = AttachmentUploadJob.class.getSimpleName();

  private static final String KEY_ROW_ID    = "row_id";
  private static final String KEY_UNIQUE_ID = "unique_id";

  private AttachmentId               attachmentId;
  @Inject SignalServiceMessageSender messageSender;

  public AttachmentUploadJob(AttachmentId attachmentId) {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build(),
         attachmentId);
  }

  private AttachmentUploadJob(@NonNull Job.Parameters parameters, @NonNull AttachmentId attachmentId) {
    super(parameters);
    this.attachmentId = attachmentId;
  }

  @Override
  public @NonNull Data serialize() {
    return new Data.Builder().putLong(KEY_ROW_ID, attachmentId.getRowId())
                             .putLong(KEY_UNIQUE_ID, attachmentId.getUniqueId())
                             .build();
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() throws Exception {
    AttachmentDatabase database           = DatabaseFactory.getAttachmentDatabase(context);
    DatabaseAttachment databaseAttachment = database.getAttachment(attachmentId);

    if (databaseAttachment == null) {
      throw new IllegalStateException("Cannot find the specified attachment.");
    }

    MediaConstraints               mediaConstraints = MediaConstraints.getPushMediaConstraints();
    Attachment                     scaledAttachment = scaleAndStripExif(database, mediaConstraints, databaseAttachment);
    SignalServiceAttachment        localAttachment  = getAttachmentFor(scaledAttachment);
    SignalServiceAttachmentPointer remoteAttachment = messageSender.uploadAttachment(localAttachment.asStream());
    Attachment                     attachment       = PointerAttachment.forPointer(Optional.of(remoteAttachment)).get();

    database.updateAttachmentAfterUpload(databaseAttachment.getAttachmentId(), attachment);
  }

  @Override
  public void onCanceled() { }

  @Override
  protected boolean onShouldRetry(Exception exception) {
    return exception instanceof PushNetworkException ||
           exception instanceof SSLException         ||
           exception instanceof ConnectException;
  }

  private SignalServiceAttachment getAttachmentFor(Attachment attachment) {
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

  private Attachment scaleAndStripExif(@NonNull AttachmentDatabase attachmentDatabase,
                                       @NonNull MediaConstraints constraints,
                                       @NonNull Attachment attachment)
      throws UndeliverableMessageException
  {
    try {
      if (constraints.isSatisfied(context, attachment)) {
        if (MediaUtil.isJpeg(attachment)) {
          MediaStream stripped = constraints.getResizedMedia(context, attachment);
          return attachmentDatabase.updateAttachmentData(attachment, stripped);
        } else {
          return attachment;
        }
      } else if (constraints.canResize(attachment)) {
        MediaStream resized = constraints.getResizedMedia(context, attachment);
        return attachmentDatabase.updateAttachmentData(attachment, resized);
      } else {
        throw new UndeliverableMessageException("Size constraints could not be met!");
      }
    } catch (IOException | MmsException e) {
      throw new UndeliverableMessageException(e);
    }
  }

  public static final class Factory implements Job.Factory<AttachmentUploadJob> {
    @Override
    public @NonNull AttachmentUploadJob create(@NonNull Parameters parameters, @NonNull org.mixedsignals.securesms.jobmanager.Data data) {
      return new AttachmentUploadJob(parameters, new AttachmentId(data.getLong(KEY_ROW_ID), data.getLong(KEY_UNIQUE_ID)));
    }
  }
}
