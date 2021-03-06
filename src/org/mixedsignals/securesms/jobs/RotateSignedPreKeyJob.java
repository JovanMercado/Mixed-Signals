package org.mixedsignals.securesms.jobs;


import android.support.annotation.NonNull;

import org.mixedsignals.securesms.ApplicationContext;
import org.mixedsignals.securesms.crypto.IdentityKeyUtil;
import org.mixedsignals.securesms.crypto.PreKeyUtil;
import org.mixedsignals.securesms.dependencies.InjectableType;
import org.mixedsignals.securesms.jobmanager.Data;
import org.mixedsignals.securesms.jobmanager.Job;
import org.mixedsignals.securesms.jobmanager.impl.NetworkConstraint;
import org.mixedsignals.securesms.logging.Log;
import org.mixedsignals.securesms.util.TextSecurePreferences;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.signalservice.api.SignalServiceAccountManager;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import javax.inject.Inject;

public class RotateSignedPreKeyJob extends BaseJob implements InjectableType {

  public static final String KEY = "RotateSignedPreKeyJob";

  private static final String TAG = RotateSignedPreKeyJob.class.getSimpleName();

  @Inject SignalServiceAccountManager accountManager;

  public RotateSignedPreKeyJob() {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setMaxAttempts(5)
                           .build());
  }

  private RotateSignedPreKeyJob(@NonNull Job.Parameters parameters) {
    super(parameters);
  }

  @Override
  public @NonNull Data serialize() {
    return Data.EMPTY;
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() throws Exception {
    Log.i(TAG, "Rotating signed prekey...");

    IdentityKeyPair    identityKey        = IdentityKeyUtil.getIdentityKeyPair(context);
    SignedPreKeyRecord signedPreKeyRecord = PreKeyUtil.generateSignedPreKey(context, identityKey, false);

    accountManager.setSignedPreKey(signedPreKeyRecord);

    PreKeyUtil.setActiveSignedPreKeyId(context, signedPreKeyRecord.getId());
    TextSecurePreferences.setSignedPreKeyRegistered(context, true);
    TextSecurePreferences.setSignedPreKeyFailureCount(context, 0);

    ApplicationContext.getInstance(context)
                      .getJobManager()
                      .add(new CleanPreKeysJob());
  }

  @Override
  public boolean onShouldRetry(Exception exception) {
    return exception instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {
    TextSecurePreferences.setSignedPreKeyFailureCount(context, TextSecurePreferences.getSignedPreKeyFailureCount(context) + 1);
  }

  public static final class Factory implements Job.Factory<RotateSignedPreKeyJob> {
    @Override
    public @NonNull RotateSignedPreKeyJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new RotateSignedPreKeyJob(parameters);
    }
  }
}
