package org.mixedsignals.securesms.jobs;


import android.content.Context;
import android.support.annotation.NonNull;

import org.mixedsignals.securesms.dependencies.InjectableType;
import org.mixedsignals.securesms.jobmanager.Data;
import org.mixedsignals.securesms.jobmanager.Job;
import org.mixedsignals.securesms.jobmanager.impl.NetworkConstraint;
import org.mixedsignals.securesms.logging.Log;
import org.mixedsignals.securesms.util.TextSecurePreferences;
import org.whispersystems.signalservice.api.SignalServiceAccountManager;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

@SuppressWarnings("WeakerAccess")
public class RotateCertificateJob extends BaseJob implements InjectableType {

  public static final String KEY = "RotateCertificateJob";

  private static final String TAG = RotateCertificateJob.class.getSimpleName();

  @Inject SignalServiceAccountManager accountManager;

  public RotateCertificateJob(Context context) {
    this(new Job.Parameters.Builder()
                           .setQueue("__ROTATE_SENDER_CERTIFICATE__")
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build());
    setContext(context);
  }

  private RotateCertificateJob(@NonNull Job.Parameters parameters) {
    super(parameters);
  }

  @Override
  public @NonNull Data serialize() {
    return Data.EMPTY;
  }

  @Override
  public String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onAdded() {}

  @Override
  public void onRun() throws IOException {
    synchronized (RotateCertificateJob.class) {
      byte[] certificate = accountManager.getSenderCertificate();
      TextSecurePreferences.setUnidentifiedAccessCertificate(context, certificate);
    }
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    return e instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {
    Log.w(TAG, "Failed to rotate sender certificate!");
  }

  public static final class Factory implements Job.Factory<RotateCertificateJob> {
    @Override
    public @NonNull RotateCertificateJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new RotateCertificateJob(parameters);
    }
  }
}