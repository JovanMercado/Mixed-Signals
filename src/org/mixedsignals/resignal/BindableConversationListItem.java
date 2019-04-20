package org.mixedsignals.resignal;

import android.support.annotation.NonNull;

import org.mixedsignals.resignal.database.model.ThreadRecord;
import org.mixedsignals.resignal.mms.GlideRequests;

import java.util.Locale;
import java.util.Set;

public interface BindableConversationListItem extends Unbindable {

  public void bind(@NonNull ThreadRecord thread,
                   @NonNull GlideRequests glideRequests, @NonNull Locale locale,
                   @NonNull Set<Long> typingThreads,
                   @NonNull Set<Long> selectedThreads, boolean batchMode);
}
