package org.mixedsignals.securesms.database.loaders;


import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import org.mixedsignals.securesms.database.Address;
import org.mixedsignals.securesms.database.DatabaseFactory;
import org.mixedsignals.securesms.recipients.Recipient;
import org.mixedsignals.securesms.util.AbstractCursorLoader;

public class ThreadMediaLoader extends AbstractCursorLoader {

  private final Address address;
  private final boolean gallery;

  public ThreadMediaLoader(@NonNull Context context, @NonNull Address address, boolean gallery) {
    super(context);
    this.address = address;
    this.gallery = gallery;
  }

  @Override
  public Cursor getCursor() {
    long threadId = DatabaseFactory.getThreadDatabase(getContext()).getThreadIdFor(Recipient.from(getContext(), address, true));

    if (gallery) return DatabaseFactory.getMediaDatabase(getContext()).getGalleryMediaForThread(threadId);
    else         return DatabaseFactory.getMediaDatabase(getContext()).getDocumentMediaForThread(threadId);
  }

  public Address getAddress() {
    return address;
  }

}
