package org.mixedsignals.securesms.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import org.mixedsignals.securesms.R;
import org.mixedsignals.securesms.attachments.Attachment;
import org.mixedsignals.securesms.mms.GlideRequests;
import org.mixedsignals.securesms.mms.Slide;
import org.mixedsignals.securesms.mms.SlideClickListener;
import org.mixedsignals.securesms.mms.SlidesClickedListener;
import org.mixedsignals.securesms.util.ThemeUtil;

import java.util.List;

public class ConversationItemThumbnail extends FrameLayout {

  private ThumbnailView          thumbnail;
  private AlbumThumbnailView     album;
  private ImageView              shade;
  private ConversationItemFooter footer;
  private CornerMask             cornerMask;
  private Outliner               outliner;

  public ConversationItemThumbnail(Context context) {
    super(context);
    init(null);
  }

  public ConversationItemThumbnail(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public ConversationItemThumbnail(final Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(attrs);
  }

  private void init(@Nullable AttributeSet attrs) {
    inflate(getContext(), R.layout.conversation_item_thumbnail, this);

    this.thumbnail  = findViewById(R.id.conversation_thumbnail_image);
    this.album      = findViewById(R.id.conversation_thumbnail_album);
    this.shade      = findViewById(R.id.conversation_thumbnail_shade);
    this.footer     = findViewById(R.id.conversation_thumbnail_footer);
    this.cornerMask = new CornerMask(this);
    this.outliner   = new Outliner();

    outliner.setColor(ThemeUtil.getThemedColor(getContext(), R.attr.conversation_item_image_outline_color));

    if (attrs != null) {
      TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ConversationItemThumbnail, 0, 0);
      thumbnail.setBounds(typedArray.getDimensionPixelSize(R.styleable.ConversationItemThumbnail_conversationThumbnail_minWidth, 0),
                          typedArray.getDimensionPixelSize(R.styleable.ConversationItemThumbnail_conversationThumbnail_maxWidth, 0),
                          typedArray.getDimensionPixelSize(R.styleable.ConversationItemThumbnail_conversationThumbnail_minHeight, 0),
                          typedArray.getDimensionPixelSize(R.styleable.ConversationItemThumbnail_conversationThumbnail_maxHeight, 0));
      typedArray.recycle();
    }
  }

  @SuppressWarnings("SuspiciousNameCombination")
  @Override
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);

    cornerMask.mask(canvas);

    if (album.getVisibility() != VISIBLE) {
      outliner.draw(canvas);
    }
  }

  @Override
  public void setFocusable(boolean focusable) {
    thumbnail.setFocusable(focusable);
    album.setFocusable(focusable);
  }

  @Override
  public void setClickable(boolean clickable) {
    thumbnail.setClickable(clickable);
    album.setClickable(clickable);
  }

  @Override
  public void setOnLongClickListener(@Nullable OnLongClickListener l) {
    thumbnail.setOnLongClickListener(l);
    album.setOnLongClickListener(l);
  }

  public void showShade(boolean show) {
    shade.setVisibility(show ? VISIBLE : GONE);
    forceLayout();
  }

  public void setCorners(int topLeft, int topRight, int bottomRight, int bottomLeft) {
    cornerMask.setRadii(topLeft, topRight, bottomRight, bottomLeft);
    outliner.setRadii(topLeft, topRight, bottomRight, bottomLeft);
  }

  public ConversationItemFooter getFooter() {
    return footer;
  }

  @UiThread
  public void setImageResource(@NonNull GlideRequests glideRequests, @NonNull List<Slide> slides,
                               boolean showControls, boolean isPreview)
  {
    if (slides.size() == 1) {
      thumbnail.setVisibility(VISIBLE);
      album.setVisibility(GONE);

      Attachment attachment = slides.get(0).asAttachment();
      thumbnail.setImageResource(glideRequests, slides.get(0), showControls, isPreview, attachment.getWidth(), attachment.getHeight());
      setTouchDelegate(thumbnail.getTouchDelegate());
    } else {
      thumbnail.setVisibility(GONE);
      album.setVisibility(VISIBLE);

      album.setSlides(glideRequests, slides, showControls);
      setTouchDelegate(album.getTouchDelegate());
    }
  }

  public void setConversationColor(@ColorInt int color) {
    if (album.getVisibility() == VISIBLE) {
      album.setCellBackgroundColor(color);
    }
  }

  public void setThumbnailClickListener(SlideClickListener listener) {
    thumbnail.setThumbnailClickListener(listener);
    album.setThumbnailClickListener(listener);
  }

  public void setDownloadClickListener(SlidesClickedListener listener) {
    thumbnail.setDownloadClickListener(listener);
    album.setDownloadClickListener(listener);
  }
}
