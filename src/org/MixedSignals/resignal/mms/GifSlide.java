package org.MixedSignals.resignal.mms;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import org.MixedSignals.resignal.attachments.Attachment;
import org.MixedSignals.resignal.util.MediaUtil;

public class GifSlide extends ImageSlide {

  public GifSlide(Context context, Attachment attachment) {
    super(context, attachment);
  }


  public GifSlide(Context context, Uri uri, long size, int width, int height) {
    this(context, uri, size, width, height, null);
  }

  public GifSlide(Context context, Uri uri, long size, int width, int height, @Nullable String caption) {
    super(context, constructAttachmentFromUri(context, uri, MediaUtil.IMAGE_GIF, size, width, height, true, null, caption, false, false));
  }

  @Override
  @Nullable
  public Uri getThumbnailUri() {
    return getUri();
  }
}
