package org.mixedsignals.resignal.jobmanager.migration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mixedsignals.resignal.jobs.AttachmentDownloadJob;
import org.mixedsignals.resignal.jobs.AttachmentUploadJob;
import org.mixedsignals.resignal.jobs.AvatarDownloadJob;
import org.mixedsignals.resignal.jobs.CleanPreKeysJob;
import org.mixedsignals.resignal.jobs.CreateSignedPreKeyJob;
import org.mixedsignals.resignal.jobs.DirectoryRefreshJob;
import org.mixedsignals.resignal.jobs.FcmRefreshJob;
import org.mixedsignals.resignal.jobs.LocalBackupJob;
import org.mixedsignals.resignal.jobs.MmsDownloadJob;
import org.mixedsignals.resignal.jobs.MmsReceiveJob;
import org.mixedsignals.resignal.jobs.MmsSendJob;
import org.mixedsignals.resignal.jobs.MultiDeviceBlockedUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceConfigurationUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceContactUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceGroupUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceProfileKeyUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceReadUpdateJob;
import org.mixedsignals.resignal.jobs.MultiDeviceVerifiedUpdateJob;
import org.mixedsignals.resignal.jobs.PushContentReceiveJob;
import org.mixedsignals.resignal.jobs.PushDecryptJob;
import org.mixedsignals.resignal.jobs.PushGroupSendJob;
import org.mixedsignals.resignal.jobs.PushGroupUpdateJob;
import org.mixedsignals.resignal.jobs.PushMediaSendJob;
import org.mixedsignals.resignal.jobs.PushNotificationReceiveJob;
import org.mixedsignals.resignal.jobs.PushTextSendJob;
import org.mixedsignals.resignal.jobs.RefreshAttributesJob;
import org.mixedsignals.resignal.jobs.RefreshPreKeysJob;
import org.mixedsignals.resignal.jobs.RefreshUnidentifiedDeliveryAbilityJob;
import org.mixedsignals.resignal.jobs.RequestGroupInfoJob;
import org.mixedsignals.resignal.jobs.RetrieveProfileAvatarJob;
import org.mixedsignals.resignal.jobs.RetrieveProfileJob;
import org.mixedsignals.resignal.jobs.RotateCertificateJob;
import org.mixedsignals.resignal.jobs.RotateProfileKeyJob;
import org.mixedsignals.resignal.jobs.RotateSignedPreKeyJob;
import org.mixedsignals.resignal.jobs.SendDeliveryReceiptJob;
import org.mixedsignals.resignal.jobs.SendReadReceiptJob;
import org.mixedsignals.resignal.jobs.ServiceOutageDetectionJob;
import org.mixedsignals.resignal.jobs.SmsReceiveJob;
import org.mixedsignals.resignal.jobs.SmsSendJob;
import org.mixedsignals.resignal.jobs.SmsSentJob;
import org.mixedsignals.resignal.jobs.TrimThreadJob;
import org.mixedsignals.resignal.jobs.TypingSendJob;
import org.mixedsignals.resignal.jobs.UpdateApkJob;

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
