package org.mixedsignals.securesms.components.reminder;

import android.content.Context;
import android.support.annotation.NonNull;

import org.mixedsignals.securesms.R;
import org.mixedsignals.securesms.util.TextSecurePreferences;

public class ServiceOutageReminder extends Reminder {

  public ServiceOutageReminder(@NonNull Context context) {
    super(null,
          context.getString(R.string.reminder_header_service_outage_text));
  }

  public static boolean isEligible(@NonNull Context context) {
    return TextSecurePreferences.getServiceOutage(context);
  }

  @Override
  public boolean isDismissable() {
    return false;
  }

  @NonNull
  @Override
  public Importance getImportance() {
    return Importance.ERROR;
  }
}
