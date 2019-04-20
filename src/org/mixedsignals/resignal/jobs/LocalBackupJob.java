package org.mixedsignals.resignal.jobs;


import android.Manifest;
import android.support.annotation.NonNull;

import org.mixedsignals.resignal.backup.BackupPassphrase;
import org.mixedsignals.resignal.jobmanager.Data;
import org.mixedsignals.resignal.jobmanager.Job;
import org.mixedsignals.resignal.logging.Log;

import org.mixedsignals.resignal.R;
import org.mixedsignals.resignal.backup.FullBackupExporter;
import org.mixedsignals.resignal.crypto.AttachmentSecretProvider;
import org.mixedsignals.resignal.database.DatabaseFactory;
import org.mixedsignals.resignal.database.NoExternalStorageException;
import org.mixedsignals.resignal.notifications.NotificationChannels;
import org.mixedsignals.resignal.permissions.Permissions;
import org.mixedsignals.resignal.service.GenericForegroundService;
import org.mixedsignals.resignal.util.BackupUtil;
import org.mixedsignals.resignal.util.StorageUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LocalBackupJob extends BaseJob {

  public static final String KEY = "LocalBackupJob";

  private static final String TAG = LocalBackupJob.class.getSimpleName();

  public LocalBackupJob() {
    this(new Job.Parameters.Builder()
                           .setQueue("__LOCAL_BACKUP__")
                           .setMaxInstances(1)
                           .setMaxAttempts(3)
                           .build());
  }

  private LocalBackupJob(@NonNull Job.Parameters parameters) {
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
  public void onRun() throws NoExternalStorageException, IOException {
    Log.i(TAG, "Executing backup job...");

    if (!Permissions.hasAll(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
      throw new IOException("No external storage permission!");
    }

    GenericForegroundService.startForegroundTask(context,
                                                 context.getString(R.string.LocalBackupJob_creating_backup),
                                                 NotificationChannels.BACKUPS,
                                                 R.drawable.ic_signal_backup);

    try {
      String backupPassword  = BackupPassphrase.get(context);
      File   backupDirectory = StorageUtil.getBackupDirectory();
      String timestamp       = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US).format(new Date());
      String fileName        = String.format("signal-%s.backup", timestamp);
      File   backupFile      = new File(backupDirectory, fileName);

      if (backupFile.exists()) {
        throw new IOException("Backup file already exists?");
      }

      if (backupPassword == null) {
        throw new IOException("Backup password is null");
      }

      File tempFile = File.createTempFile("backup", "tmp", StorageUtil.getBackupCacheDirectory(context));

      FullBackupExporter.export(context,
                                AttachmentSecretProvider.getInstance(context).getOrCreateAttachmentSecret(),
                                DatabaseFactory.getBackupDatabase(context),
                                tempFile,
                                backupPassword);

      if (!tempFile.renameTo(backupFile)) {
        tempFile.delete();
        throw new IOException("Renaming temporary backup file failed!");
      }

      BackupUtil.deleteOldBackups();
    } finally {
      GenericForegroundService.stopForegroundTask(context);
    }
  }

  @Override
  public boolean onShouldRetry(Exception e) {
    return false;
  }

  @Override
  public void onCanceled() {
  }

  public static class Factory implements Job.Factory<LocalBackupJob> {
    @Override
    public @NonNull LocalBackupJob create(@NonNull Parameters parameters, @NonNull Data data) {
      return new LocalBackupJob(parameters);
    }
  }
}
