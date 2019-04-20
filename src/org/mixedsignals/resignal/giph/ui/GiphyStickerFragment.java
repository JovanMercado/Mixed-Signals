package org.mixedsignals.resignal.giph.ui;


import android.os.Bundle;
import android.support.v4.content.Loader;

import org.mixedsignals.resignal.giph.model.GiphyImage;
import org.mixedsignals.resignal.giph.net.GiphyStickerLoader;

import java.util.List;

public class GiphyStickerFragment extends GiphyFragment {
  @Override
  public Loader<List<GiphyImage>> onCreateLoader(int id, Bundle args) {
    return new GiphyStickerLoader(getActivity(), searchString);
  }
}
