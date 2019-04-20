package org.MixedSignals.resignal.mms;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.MixedSignals.resignal.attachments.Attachment;
import org.MixedSignals.resignal.contactshare.Contact;
import org.MixedSignals.resignal.linkpreview.LinkPreview;
import org.MixedSignals.resignal.recipients.Recipient;

import java.util.Collections;
import java.util.List;

public class OutgoingSecureMediaMessage extends OutgoingMediaMessage {

  public OutgoingSecureMediaMessage(Recipient recipient, String body,
                                    List<Attachment> attachments,
                                    long sentTimeMillis,
                                    int distributionType,
                                    long expiresIn,
                                    @Nullable QuoteModel quote,
                                    @NonNull List<Contact> contacts,
                                    @NonNull List<LinkPreview> previews)
  {
    super(recipient, body, attachments, sentTimeMillis, -1, expiresIn, distributionType, quote, contacts, previews, Collections.emptyList(), Collections.emptyList());
  }

  public OutgoingSecureMediaMessage(OutgoingMediaMessage base) {
    super(base);
  }

  @Override
  public boolean isSecure() {
    return true;
  }
}
