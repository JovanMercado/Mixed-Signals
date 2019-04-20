package org.mixedsignals.resignal.jobs;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import org.mixedsignals.resignal.ApplicationContext;
import org.mixedsignals.resignal.database.Address;
import org.mixedsignals.resignal.database.DatabaseFactory;
import org.mixedsignals.resignal.database.MessagingDatabase.SyncMessageId;
import org.mixedsignals.resignal.database.RecipientDatabase;
import org.mixedsignals.resignal.jobmanager.Job;
import org.mixedsignals.resignal.logging.Log;
import org.mixedsignals.resignal.recipients.Recipient;
import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope;

public abstract class PushReceivedJob extends BaseJob {

  private static final String TAG = PushReceivedJob.class.getSimpleName();

  public static final Object RECEIVE_LOCK = new Object();

  protected PushReceivedJob(Job.Parameters parameters) {
    super(parameters);
  }

  public void processEnvelope(@NonNull SignalServiceEnvelope envelope) {
    synchronized (RECEIVE_LOCK) {
      if (envelope.hasSource()) {
        Address   source    = Address.fromExternal(context, envelope.getSource());
        Recipient recipient = Recipient.from(context, source, false);

        if (!isActiveNumber(recipient)) {
          DatabaseFactory.getRecipientDatabase(context).setRegistered(recipient, RecipientDatabase.RegisteredState.REGISTERED);
          ApplicationContext.getInstance(context).getJobManager().add(new DirectoryRefreshJob(recipient, false));
        }
      }

      if (envelope.isReceipt()) {
        handleReceipt(envelope);
      } else if (envelope.isPreKeySignalMessage() || envelope.isSignalMessage() || envelope.isUnidentifiedSender()) {
        handleMessage(envelope);
      } else {
        Log.w(TAG, "Received envelope of unknown type: " + envelope.getType());
      }
    }
  }

  private void handleMessage(SignalServiceEnvelope envelope) {
    new PushDecryptJob(context).processMessage(envelope);
  }

  @SuppressLint("DefaultLocale")
  private void handleReceipt(SignalServiceEnvelope envelope) {
    Log.i(TAG, String.format("Received receipt: (XXXXX, %d)", envelope.getTimestamp()));
    DatabaseFactory.getMmsSmsDatabase(context).incrementDeliveryReceiptCount(new SyncMessageId(Address.fromExternal(context, envelope.getSource()),
                                                                                               envelope.getTimestamp()), System.currentTimeMillis());
  }

  private boolean isActiveNumber(@NonNull Recipient recipient) {
    return recipient.resolve().getRegistered() == RecipientDatabase.RegisteredState.REGISTERED;
  }
}
