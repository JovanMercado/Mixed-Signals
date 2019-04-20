package org.mixedsignals.resignal.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.mixedsignals.resignal.ApplicationContext;
import org.mixedsignals.resignal.jobs.PushNotificationReceiveJob;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    ApplicationContext.getInstance(context).getJobManager().add(new PushNotificationReceiveJob(context));
  }
}
