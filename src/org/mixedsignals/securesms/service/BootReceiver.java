package org.mixedsignals.securesms.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.mixedsignals.securesms.ApplicationContext;
import org.mixedsignals.securesms.jobs.PushNotificationReceiveJob;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    ApplicationContext.getInstance(context).getJobManager().add(new PushNotificationReceiveJob(context));
  }
}
