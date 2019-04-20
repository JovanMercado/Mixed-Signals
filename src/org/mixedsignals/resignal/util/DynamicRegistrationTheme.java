package org.mixedsignals.resignal.util;

import android.app.Activity;

import org.mixedsignals.resignal.R;

public class DynamicRegistrationTheme extends DynamicTheme {
  @Override
  protected int getSelectedTheme(Activity activity) {
    String theme = TextSecurePreferences.getTheme(activity);

    if (theme.equals("dark")) return R.style.TextSecure_DarkRegistrationTheme;

    return R.style.TextSecure_LightRegistrationTheme;
  }
}
