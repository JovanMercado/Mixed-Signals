package org.mixedsignals.securesms;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;

import org.mixedsignals.securesms.util.TextSecurePreferences;
import org.mixedsignals.securesms.util.dynamiclanguage.DynamicLanguageActivityHelper;
import org.mixedsignals.securesms.util.dynamiclanguage.DynamicLanguageContextWrapper;

public abstract class BaseActivity extends FragmentActivity {
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    return (keyCode == KeyEvent.KEYCODE_MENU && isMenuWorkaroundRequired()) || super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU && isMenuWorkaroundRequired()) {
      openOptionsMenu();
      return true;
    }
    return super.onKeyUp(keyCode, event);
  }

  public static boolean isMenuWorkaroundRequired() {
    return VERSION.SDK_INT < VERSION_CODES.KITKAT          &&
           VERSION.SDK_INT > VERSION_CODES.GINGERBREAD_MR1 &&
           ("LGE".equalsIgnoreCase(Build.MANUFACTURER) || "E6710".equalsIgnoreCase(Build.DEVICE));
  }

  @Override
  protected void onResume() {
    super.onResume();
    DynamicLanguageActivityHelper.recreateIfNotInCorrectLanguage(this, TextSecurePreferences.getLanguage(this));
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(DynamicLanguageContextWrapper.updateContext(newBase, TextSecurePreferences.getLanguage(newBase)));
  }
}
