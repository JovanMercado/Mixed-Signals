package org.mixedsignals.securesms.jobmanager.migration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mixedsignals.securesms.jobs.AttachmentDownloadJob;
import org.mixedsignals.securesms.jobs.AttachmentUploadJob;
import org.mixedsignals.securesms.jobs.AvatarDownloadJob;
import org.mixedsignals.securesms.jobs.CleanPreKeysJob;
import org.mixedsignals.securesms.jobs.CreateSignedPreKeyJob;
import org.mixedsignals.securesms.jobs.DirectoryRefreshJob;
import org.mixedsignals.securesms.jobs.FcmRefreshJob;
import org.mixedsignals.securesms.jobs.LocalBackupJob;
import org.mixedsignals.securesms.jobs.MmsDownloadJob;
import org.mixedsignals.securesms.jobs.MmsReceiveJob;
import org.mixedsignals.securesms.jobs.MmsSendJob;
import org.mixedsignals.securesms.jobs.MultiDeviceBlockedUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceConfigurationUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceContactUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceGroupUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceProfileKeyUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceReadUpdateJob;
import org.mixedsignals.securesms.jobs.MultiDeviceVerifiedUpdateJob;
import org.mixedsignals.securesms.jobs.PushContentReceiveJob;
import org.mixedsignals.securesms.jobs.PushDecryptJob;
import org.mixedsignals.securesms.jobs.PushGroupSendJob;
import org.mixedsignals.securesms.jobs.PushGroupUpdateJob;
import org.mixedsignals.securesms.jobs.PushMediaSendJob;
import org.mixedsignals.securesms.jobs.PushNotificationReceiveJob;
import org.mixedsignals.securesms.jobs.PushTextSendJob;
import org.mixedsignals.securesms.jobs.RefreshAttributesJob;
import org.mixedsignals.securesms.jobs.RefreshPreKeysJob;
import org.mixedsignals.securesms.jobs.RefreshUnidentifiedDeliveryAbilityJob;
import org.mixedsignals.securesms.jobs.RequestGroupInfoJob;
import org.mixedsignals.securesms.jobs.RetrieveProfileAvatarJob;
import org.mixedsignals.securesms.jobs.RetrieveProfileJob;
import org.mixedsignals.securesms.jobs.RotateCertificateJob;
import org.mixedsignals.securesms.jobs.RotateProfileKeyJob;
import org.mixedsignals.securesms.jobs.RotateSignedPreKeyJob;
import org.mixedsignals.securesms.jobs.SendDeliveryReceiptJob;
import org.mixedsignals.securesms.jobs.SendReadReceiptJob;
import org.mixedsignals.securesms.jobs.ServiceOutageDetectionJob;
import org.mixedsignals.securesms.jobs.SmsReceiveJob;
import org.mixedsignals.securesms.jobs.SmsSendJob;
import org.mixedsignals.securesms.jobs.SmsSentJob;
import org.mixedsignals.securesms.jobs.TrimThreadJob;
import org.mixedsignals.securesms.jobs.TypingSendJob;
import org.mixedsignals.securesms.jobs.UpdateApkJob;

import java.util.HashMap;
import java.util.Map;

public class WorkManagerFactoryMappings {

  private static final Map<String, String> FACTORY_MAP = new HashMap<String, String>() {{
    put(AttachmentDownloadJob.class.getName(), AttachmentDownloadJob.KEY);
    put(AttachmentUploadJob.class.getName(), AttachmentUploadJob.KEY);
    put(AvatarDownloadJob.class.getName(), AvatarDownloadJob.KEY);
    put(CleanPreKeysJob.class.getName(), CleanPreKeysJob.KEY);
    put(CreateSignedPreKeyJob.class.getName(), CreateSignedPreKeyJob.KEY);
    put(DirectoryRefreshJob.class.getName(), DirectoryRefreshJob.KEY);
    put(FcmRefreshJob.class.getName(), FcmRefreshJob.KEY);
    put(LocalBackupJob.class.getName(), LocalBackupJob.KEY);
    put(MmsDownloadJob.class.getName(), MmsDownloadJob.KEY);
    put(MmsReceiveJob.class.getName(), MmsReceiveJob.KEY);
    put(MmsSendJob.class.getName(), MmsSendJob.KEY);
    put(MultiDeviceBlockedUpdateJob.class.getName(), MultiDeviceBlockedUpdateJob.KEY);
    put(MultiDeviceConfigurationUpdateJob.class.getName(), MultiDeviceConfigurationUpdateJob.KEY);
    put(MultiDeviceContactUpdateJob.class.getName(), MultiDeviceContactUpdateJob.KEY);
    put(MultiDeviceGroupUpdateJob.class.getName(), MultiDeviceGroupUpdateJob.KEY);
    put(MultiDeviceProfileKeyUpdateJob.class.getName(), MultiDeviceProfileKeyUpdateJob.KEY);
    put(MultiDeviceReadUpdateJob.class.getName(), MultiDeviceReadUpdateJob.KEY);
    put(MultiDeviceVerifiedUpdateJob.class.getName(), MultiDeviceVerifiedUpdateJob.KEY);
    put(PushContentReceiveJob.class.getName(), PushContentReceiveJob.KEY);
    put(PushDecryptJob.class.getName(), PushDecryptJob.KEY);
    put(PushGroupSendJob.class.getName(), PushGroupSendJob.KEY);
    put(PushGroupUpdateJob.class.getName(), PushGroupUpdateJob.KEY);
    put(PushMediaSendJob.class.getName(), PushMediaSendJob.KEY);
    put(PushNotificationReceiveJob.class.getName(), PushNotificationReceiveJob.KEY);
    put(PushTextSendJob.class.getName(), PushTextSendJob.KEY);
    put(RefreshAttributesJob.class.getName(), RefreshAttributesJob.KEY);
    put(RefreshPreKeysJob.class.getName(), RefreshPreKeysJob.KEY);
    put(RefreshUnidentifiedDeliveryAbilityJob.class.getName(), RefreshUnidentifiedDeliveryAbilityJob.KEY);
    put(RequestGroupInfoJob.class.getName(), RequestGroupInfoJob.KEY);
    put(RetrieveProfileAvatarJob.class.getName(), RetrieveProfileAvatarJob.KEY);
    put(RetrieveProfileJob.class.getName(), RetrieveProfileJob.KEY);
    put(RotateCertificateJob.class.getName(), RotateCertificateJob.KEY);
    put(RotateProfileKeyJob.class.getName(), RotateProfileKeyJob.KEY);
    put(RotateSignedPreKeyJob.class.getName(), RotateSignedPreKeyJob.KEY);
    put(SendDeliveryReceiptJob.class.getName(), SendDeliveryReceiptJob.KEY);
    put(SendReadReceiptJob.class.getName(), SendReadReceiptJob.KEY);
    put(ServiceOutageDetectionJob.class.getName(), ServiceOutageDetectionJob.KEY);
    put(SmsReceiveJob.class.getName(), SmsReceiveJob.KEY);
    put(SmsSendJob.class.getName(), SmsSendJob.KEY);
    put(SmsSentJob.class.getName(), SmsSentJob.KEY);
    put(TrimThreadJob.class.getName(), TrimThreadJob.KEY);
    put(TypingSendJob.class.getName(), TypingSendJob.KEY);
    put(UpdateApkJob.class.getName(), UpdateApkJob.KEY);
  }};

  public static @Nullable String getFactoryKey(@NonNull String workManagerClass) {
    return FACTORY_MAP.get(workManagerClass);
  }
}
