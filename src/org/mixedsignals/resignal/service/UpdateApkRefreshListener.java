package org.mixedsignals.resignal.service;


import android.content.Context;
import android.content.Intent;
import org.mixedsignals.resignal.logging.Log;

import org.mixedsignals.resignal.ApplicationContext;
import org.mixedsignals.resignal.BuildConfig;
import org.mixedsignals.resignal.jobs.UpdateApkJob;
import org.mixedsignals.resignal.util.TextSecurePreferences;

import java.util.concurrent.TimeUnit;

public class UpdateApkRefreshListener extends PersistentAlarmManagerListener {

  private static final String TAG = UpdateApkRefreshListener.class.getSimpleName();

  private static final long INTERVAL = TimeUnit.HOURS.toMillis(6);

  @Override
  protected long getNextScheduledExecutionTime(Context context) {
    return TextSecurePreferences.getUpdateApkRefreshTime(context);
  }

  @Override
  protected long onAlarm(Context context, long scheduledTime) {
    Log.i(TAG, "onAlarm...");

    if (scheduledTime != 0 && BuildConfig.PLAY_STORE_DISABLED) {
      Log.i(TAG, "Queueing APK update job...");
      ApplicationContext.getInstance(context)
                        .getJobManager()
                        .add(new UpdateApkJob());
    }

    long newTime = System.currentTimeMillis() + INTERVAL;
    TextSecurePreferences.setUpdateApkRefreshTime(context, newTime);

    return newTime;
  }

  public static void schedule(Context context) {
    new UpdateApkRefreshListener().onReceive(context, new Intent());
  }

}
