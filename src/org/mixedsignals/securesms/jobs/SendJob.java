package org.mixedsignals.securesms.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mixedsignals.securesms.BuildConfig;
import org.mixedsignals.securesms.TextSecureExpiredException;
import org.mixedsignals.securesms.attachments.Attachment;
import org.mixedsignals.securesms.database.AttachmentDatabase;
import org.mixedsignals.securesms.database.DatabaseFactory;
import org.mixedsignals.securesms.jobmanager.Job;
import org.mixedsignals.securesms.jobmanager.JobLogger;
import org.mixedsignals.securesms.logging.Log;
import org.mixedsignals.securesms.mms.MediaConstraints;
import org.mixedsignals.securesms.mms.MediaStream;
import org.mixedsignals.securesms.mms.MmsException;
import org.mixedsignals.securesms.transport.UndeliverableMessageException;
import org.mixedsignals.securesms.util.MediaUtil;
import org.mixedsignals.securesms.util.Util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public abstract class SendJob extends BaseJob {

  @SuppressWarnings("unused")
  private final static String TAG = SendJob.class.getSimpleName();

  public SendJob(Job.Parameters parameters) {
    super(parameters);
  }

  @Override
  public final void onRun() throws Exception {
    if (Util.getDaysTillBuildExpiry() <= 0) {
      throw new TextSecureExpiredException(String.format("TextSecure expired (build %d, now %d)",
                                                         BuildConfig.BUILD_TIMESTAMP,
                                                         System.currentTimeMillis()));
    }

    Log.i(TAG, "Starting message send attempt");
    onSend();
    Log.i(TAG, "Message send completed");
  }

  protected abstract void onSend() throws Exception;

  protected void markAttachmentsUploaded(long messageId, @NonNull List<Attachment> attachments) {
    AttachmentDatabase database = DatabaseFactory.getAttachmentDatabase(context);

    for (Attachment attachment : attachments) {
      database.markAttachmentUploaded(messageId, attachment);
    }
  }

  protected List<Attachment> scaleAndStripExifFromAttachments(@NonNull MediaConstraints constraints,
                                                              @NonNull List<Attachment> attachments)
      throws UndeliverableMessageException
  {
    AttachmentDatabase attachmentDatabase = DatabaseFactory.getAttachmentDatabase(context);
    List<Attachment>   results            = new LinkedList<>();

    for (Attachment attachment : attachments) {
      try {
        if (constraints.isSatisfied(context, attachment)) {
          if (MediaUtil.isJpeg(attachment)) {
            MediaStream stripped = constraints.getResizedMedia(context, attachment);
            results.add(attachmentDatabase.updateAttachmentData(attachment, stripped));
          } else {
            results.add(attachment);
          }
        } else if (constraints.canResize(attachment)) {
          MediaStream resized = constraints.getResizedMedia(context, attachment);
          results.add(attachmentDatabase.updateAttachmentData(attachment, resized));
        } else {
          throw new UndeliverableMessageException("Size constraints could not be met!");
        }
      } catch (IOException | MmsException e) {
        throw new UndeliverableMessageException(e);
      }
    }

    return results;
  }

  protected void log(@NonNull String tag, @NonNull String message) {
    Log.i(tag, JobLogger.format(this, message));
  }

  protected void warn(@NonNull String tag, @NonNull String message) {
    warn(tag, message, null);
  }

  protected void warn(@NonNull String tag, @Nullable Throwable t) {
    warn(tag, "", t);
  }

  protected void warn(@NonNull String tag, @NonNull String message, @Nullable Throwable t) {
    Log.w(tag, JobLogger.format(this, message), t);
  }
}
