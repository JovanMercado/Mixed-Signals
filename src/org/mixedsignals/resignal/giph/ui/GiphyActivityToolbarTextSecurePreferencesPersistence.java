package org.mixedsignals.resignal.giph.ui;

import android.content.Context;

import org.mixedsignals.resignal.util.TextSecurePreferences;

class GiphyActivityToolbarTextSecurePreferencesPersistence implements GiphyActivityToolbar.Persistence {

  static GiphyActivityToolbar.Persistence fromContext(Context context) {
    return new GiphyActivityToolbarTextSecurePreferencesPersistence(context.getApplicationContext());
  }

  private final Context context;

  private GiphyActivityToolbarTextSecurePreferencesPersistence(Context context) {
    this.context = context;
  }

  @Override
  public boolean getGridSelected() {
    return TextSecurePreferences.isGifSearchInGridLayout(context);
  }

  @Override
  public void setGridSelected(boolean isGridSelected) {
    TextSecurePreferences.setIsGifSearchInGridLayout(context, isGridSelected);
  }
}
