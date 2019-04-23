package org.mixedsignals.securesms.giph.ui;


import android.os.Bundle;
import android.support.v4.content.Loader;

import org.mixedsignals.securesms.giph.model.GiphyImage;
import org.mixedsignals.securesms.giph.net.GiphyGifLoader;

import java.util.List;

public class GiphyGifFragment extends GiphyFragment {

  @Override
  public Loader<List<GiphyImage>> onCreateLoader(int id, Bundle args) {
    return new GiphyGifLoader(getActivity(), searchString);
  }

}
