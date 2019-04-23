package org.mixedsignals.securesms.dependencies;

import android.content.Context;

import org.mixedsignals.securesms.gcm.FcmService;
import org.mixedsignals.securesms.jobs.AttachmentUploadJob;
import org.mixedsignals.securesms.jobs.MultiDeviceConfigurationUpdateJob;
import org.mixedsignals.securesms.jobs.RefreshUnidentifiedDeliveryAbilityJob;
import org.mixedsignals.securesms.jobs.RotateProfileKeyJob;
import org.mixedsignals.securesms.jobs.TypingSendJob;
import org.mixedsignals.securesms.logging.Log;

import org.greenrobot.eventbus.EventBus;
import org.mixedsignals.securesms.BuildConfig;
import org.mixedsignals.securesms.CreateProfileActivity;
import org.mixedsignals.securesms.DeviceListFragment;
import org.mixedsignals.securesms.crypto.storage.SignalProtocolStoreImpl;
import org.mixedsignals.securesms.events.ReminderUpdateEvent;
import org.mixedsignals.securesms.jobs.AttachmentDownloadJob;
import org.mixedsignals.securesms.jobs.AvatarDownloadJob;
import org.mixedsignals.securesms.jobs.CleanPreKeysJob;
import org.mixedsignals.securesms.jobs.CreateSignedPreKeyJob;
import org.mixedsignals.securesms.jobs.FcmRefreshJob;
import org.mixedsignals.securesms.jobs.MultiDeviceBlockedUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceContactUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceGroupUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceProfileKeyUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceReadUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceVerifiedUpdateJob;
import org.mixedsignals.securesms.jobs.PushGroupSendJob;
import org.mixedsignals.securesms.jobs.PushGroupUpdateJob;
import org.mixedsignals.securesms.jobs.PushMediaSendJob;
import org.mixedsignals.securesms.jobs.PushNotificationReceiveJob;
import org.mixedsignals.securesms.jobs.PushTextSendJob;
import org.mixedsignals.securesms.jobs.RefreshAttributesJob;
import org.mixedsignals.securesms.jobs.RefreshPreKeysJob;
import org.mixedsignals.securesms.jobs.RequestGroupInfoJob;
import org.mixedsignals.securesms.jobs.RetrieveProfileAvatarJob;
import org.mixedsignals.securesms.jobs.RetrieveProfileJob;
import org.mixedsignals.securesms.jobs.RotateCertificateJob;
import org.mixedsignals.securesms.jobs.RotateSignedPreKeyJob;
import org.mixedsignals.securesms.jobs.SendDeliveryReceiptJob;
import org.mixedsignals.securesms.jobs.SendReadReceiptJob;
import org.mixedsignals.securesms.preferences.AppProtectionPreferenceFragment;
import org.mixedsignals.securesms.push.SecurityEventListener;
import org.mixedsignals.securesms.push.SignalServiceNetworkAccess;
import org.mixedsignals.securesms.service.IncomingMessageObserver;
import org.mixedsignals.securesms.service.WebRtcCallService;
import org.mixedsignals.securesms.util.TextSecurePreferences;
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
