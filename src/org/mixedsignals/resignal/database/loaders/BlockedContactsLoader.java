package org.mixedsignals.resignal.database.loaders;

import android.content.Context;
import android.database.Cursor;

import org.mixedsignals.resignal.database.DatabaseFactory;
import org.mixedsignals.resignal.util.AbstractCursorLoader;

public class BlockedContactsLoader extends AbstractCursorLoader {

  public BlockedContactsLoader(Context context) {
    super(context);
  }

  @Override
  public Cursor getCursor() {
    return DatabaseFactory.getRecipientDatabase(getContext())
                          .getBlocked();
  }

}
