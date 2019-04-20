package org.mixedsignals.resignal.components.emoji;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import org.mixedsignals.resignal.R;
import org.mixedsignals.resignal.components.emoji.EmojiPageView.EmojiSelectionListener;

import java.util.List;

public class EmojiVariationSelectorPopup extends PopupWindow {

  private final Context                context;
  private final ViewGroup              list;
  private final EmojiSelectionListener listener;

  public EmojiVariationSelectorPopup(@NonNull Context context, @NonNull EmojiSelectionListener listener) {
    super(LayoutInflater.from(context).inflate(R.layout.emoji_variation_selector, null),
          ViewGroup.LayoutParams.WRAP_CONTENT,
          ViewGroup.LayoutParams.WRAP_CONTENT);
    this.context  = context;
    this.listener = listener;
    this.list     = (ViewGroup) getContentView();

    setBackgroundDrawable(null);
    setOutsideTouchable(true);

    if (Build.VERSION.SDK_INT >= 21) {
      setElevation(20);
    }
  }

  public void setVariations(List<String> variations) {
    list.removeAllViews();

    for (String variation : variations) {
      ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.emoji_variation_selector_item, list, false);
      imageView.setImageDrawable(EmojiProvider.getInstance(context).getEmojiDrawable(variation));
      imageView.setOnClickListener(v -> {
        listener.onEmojiSelected(variation);
        dismiss();
      });
      list.addView(imageView);
    }
  }
}
