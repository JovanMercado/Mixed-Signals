package org.mixedsignals.securesms.util;

import android.app.Activity;

import org.mixedsignals.securesms.R;

public class DynamicRegistrationTheme extends DynamicTheme {
  @Override
  protected int getSelectedTheme(Activity activity) {
    String theme = TextSecurePreferences.getTheme(activity);

    if (theme.equals("dark")) return R.style.TextSecure_DarkRegistrationTheme;

    return R.style.TextSecure_LightRegistrationTheme;
  }
}
