package org.mixedsignals.resignal.dependencies;

import android.content.Context;

import org.mixedsignals.resignal.gcm.FcmService;
import org.mixedsignals.resignal.jobs.AttachmentUploadJob;
import org.mixedsignals.resignal.jobs.MultiDeviceConfigurationUpdateJob;
import org.mixedsignals.resignal.jobs.RefreshUnidentifiedDeliveryAbilityJob;
import org.mixedsignals.resignal.jobs.RotateProfileKeyJob;
import org.mixedsignals.resignal.jobs.TypingSendJob;
import org.mixedsignals.resignal.logging.Log;

import org.greenrobot.eventbus.EventBus;
import org.mixedsignals.resignal.BuildConfig;
import org.mixedsignals.resignal.CreateProfileActivity;
import org.mixedsignals.resignal.DeviceListFragment;
import org.mixedsignals.resignal.crypto.storage.SignalProtocolStoreImpl;
import org.mixedsignals.resignal.events.ReminderUpdateEvent;
import org.mixedsignals.resignal.jobs.AttachmentDownloadJob;
import org.mixedsignals.resignal.jobs.AvatarDownloadJob;
import org.mixedsignals.resignal.jobs.CleanPreKeysJob;
import org.mixedsignals.resignal.jobs.CreateSignedPreKeyJob;
import org.mixedsignals.resignal.jobs.FcmRefreshJob;
import org.mixedsignals.resignal.jobs.MultiDeviceBlockedUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceContactUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceGroupUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceProfileKeyUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceReadUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceVerifiedUpdateJob;
import org.mixedsignals.resignal.jobs.PushGroupSendJob;
import org.mixedsignals.resignal.jobs.PushGroupUpdateJob;
import org.mixedsignals.resignal.jobs.PushMediaSendJob;
import org.mixedsignals.resignal.jobs.PushNotificationReceiveJob;
import org.mixedsignals.resignal.jobs.PushTextSendJob;
import org.mixedsignals.resignal.jobs.RefreshAttributesJob;
import org.mixedsignals.resignal.jobs.RefreshPreKeysJob;
import org.mixedsignals.resignal.jobs.RequestGroupInfoJob;
import org.mixedsignals.resignal.jobs.RetrieveProfileAvatarJob;
import org.mixedsignals.resignal.jobs.RetrieveProfileJob;
import org.mixedsignals.resignal.jobs.RotateCertificateJob;
import org.mixedsignals.resignal.jobs.RotateSignedPreKeyJob;
import org.mixedsignals.resignal.jobs.SendDeliveryReceiptJob;
import org.mixedsignals.resignal.jobs.SendReadReceiptJob;
import org.mixedsignals.resignal.preferences.AppProtectionPreferenceFragment;
import org.mixedsignals.resignal.push.SecurityEventListener;
import org.mixedsignals.resignal.push.SignalServiceNetworkAccess;
import org.mixedsignals.resignal.service.IncomingMessageObserver;
import org.mixedsignals.resignal.service.WebRtcCallService;
import org.mixedsignals.resignal.util.TextSecurePreferences;
import org.whispersystems.libsignal.util.guava.Optional;
import org.whispersystems.signalservice.api.SignalServiceAccountManager;
import org.whispersystems.signalservice.api.SignalServiceMessageReceiver;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.util.CredentialsProvider;
import org.whispersystems.signalservice.api.util.RealtimeSleepTimer;
import org.whispersystems.signalservice.api.util.SleepTimer;
import org.whispersystems.signalservice.api.util.UptimeSleepTimer;
import org.whispersystems.signalservice.api.websocket.ConnectivityListener;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, injects = {CleanPreKeysJob.class,
                                     CreateSignedPreKeyJob.class,
                                     PushGroupSendJob.class,
                                     PushTextSendJob.class,
                                     PushMediaSendJob.class,
                                     AttachmentDownloadJob.class,
                                     RefreshPreKeysJob.class,
                                     IncomingMessageObserver.class,
                                     PushNotificationReceiveJob.class,
                                     MultiDeviceContactUpdateJob.class,
                                     MultiDeviceGroupUpdateJob.class,
                                     MultiDeviceReadUpdateJob.class,
                                     MultiDeviceBlockedUpdateJob.class,
                                     DeviceListFragment.class,
                                     RefreshAttributesJob.class,
                                     FcmRefreshJob.class,
                                     RequestGroupInfoJob.class,
                                     PushGroupUpdateJob.class,
                                     AvatarDownloadJob.class,
                                     RotateSignedPreKeyJob.class,
                                     WebRtcCallService.class,
                                     RetrieveProfileJob.class,
                                     MultiDeviceVerifiedUpdateJob.class,
                                     CreateProfileActivity.class,
                                     RetrieveProfileAvatarJob.class,
                                     MultiDeviceProfileKeyUpdateJob.class,
                                     SendReadReceiptJob.class,
                                     AppProtectionPreferenceFragment.class,
                                     FcmService.class,
                                     RotateCertificateJob.class,
                                     SendDeliveryReceiptJob.class,
                                     RotateProfileKeyJob.class,
                                     MultiDeviceConfigurationUpdateJob.class,
                                     RefreshUnidentifiedDeliveryAbilityJob.class,
                                     TypingSendJob.class,
                                     AttachmentUploadJob.class})
public class SignalCommunicationModule {

  private static final String TAG = SignalCommunicationModule.class.getSimpleName();

  private final Context                      context;
  private final SignalServiceNetworkAccess   networkAccess;

  private SignalServiceAccountManager  accountManager;
  private SignalServiceMessageSender   messageSender;
  private SignalServiceMessageReceiver messageReceiver;

  public SignalCommunicationModule(Context context, SignalServiceNetworkAccess networkAccess) {
    this.context       = context;
    this.networkAccess = networkAccess;
  }

  @Provides
  synchronized SignalServiceAccountManager provideSignalAccountManager() {
    if (this.accountManager == null) {
      this.accountManager = new SignalServiceAccountManager(networkAccess.getConfiguration(context),
                                                            new DynamicCredentialsProvider(context),
                                                            BuildConfig.USER_AGENT);
    }

    return this.accountManager;
  }

  @Provides
  synchronized SignalServiceMessageSender provideSignalMessageSender() {
    if (this.messageSender == null) {
      this.messageSender = new SignalServiceMessageSender(networkAccess.getConfiguration(context),
                                                          new DynamicCredentialsProvider(context),
                                                          new SignalProtocolStoreImpl(context),
                                                          BuildConfig.USER_AGENT,
                                                          TextSecurePreferences.isMultiDevice(context),
                                                          Optional.fromNullable(IncomingMessageObserver.getPipe()),
                                                          Optional.fromNullable(IncomingMessageObserver.getUnidentifiedPipe()),
                                                          Optional.of(new SecurityEventListener(context)));
    } else {
      this.messageSender.setMessagePipe(IncomingMessageObserver.getPipe(), IncomingMessageObserver.getUnidentifiedPipe());
      this.messageSender.setIsMultiDevice(TextSecurePreferences.isMultiDevice(context));
    }

    return this.messageSender;
  }

  @Provides
  synchronized SignalServiceMessageReceiver provideSignalMessageReceiver() {
    if (this.messageReceiver == null) {
      SleepTimer sleepTimer =  TextSecurePreferences.isFcmDisabled(context) ? new RealtimeSleepTimer(context) : new UptimeSleepTimer();

      this.messageReceiver = new SignalServiceMessageReceiver(networkAccess.getConfiguration(context),
                                                              new DynamicCredentialsProvider(context),
                                                              BuildConfig.USER_AGENT,
                                                              new PipeConnectivityListener(),
                                                              sleepTimer);
    }

    return this.messageReceiver;
  }

  @Provides
  synchronized SignalServiceNetworkAccess provideSignalServiceNetworkAccess() {
    return networkAccess;
  }

  private static class DynamicCredentialsProvider implements CredentialsProvider {

    private final Context context;

    private DynamicCredentialsProvider(Context context) {
      this.context = context.getApplicationContext();
    }

    @Override
    public String getUser() {
      return TextSecurePreferences.getLocalNumber(context);
    }

    @Override
    public String getPassword() {
      return TextSecurePreferences.getPushServerPassword(context);
    }

    @Override
    public String getSignalingKey() {
      return TextSecurePreferences.getSignalingKey(context);
    }
  }

  private class PipeConnectivityListener implements ConnectivityListener {

    @Override
    public void onConnected() {
      Log.i(TAG, "onConnected()");
    }

    @Override
    public void onConnecting() {
      Log.i(TAG, "onConnecting()");
    }

    @Override
    public void onDisconnected() {
      Log.w(TAG, "onDisconnected()");
    }

    @Override
    public void onAuthenticationFailure() {
      Log.w(TAG, "onAuthenticationFailure()");
      TextSecurePreferences.setUnauthorizedReceived(context, true);
      EventBus.getDefault().post(new ReminderUpdateEvent());
    }

  }

}
