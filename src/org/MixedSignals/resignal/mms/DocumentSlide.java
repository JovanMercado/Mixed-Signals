package org.MixedSignals.resignal.mms;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.MixedSignals.resignal.attachments.Attachment;
import org.MixedSignals.resignal.util.StorageUtil;

public class DocumentSlide extends Slide {

  public DocumentSlide(@NonNull Context context, @NonNull Attachment attachment) {
    super(context, attachment);
  }

  public DocumentSlide(@NonNull Context context, @NonNull Uri uri,
                       @NonNull String contentType,  long size,
                       @Nullable String fileName)
  {
    super(context, constructAttachmentFromUri(context, uri, contentType, size, 0, 0, true, StorageUtil.getCleanFileName(fileName), null, false, false));
  }

  @Override
  public boolean hasDocument() {
    return true;
  }

}
