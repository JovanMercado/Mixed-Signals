package org.mixedsignals.securesms.jobs;

import org.mixedsignals.securesms.jobmanager.Data;
import org.mixedsignals.securesms.jobmanager.Job;
import org.mixedsignals.securesms.logging.Log;

import android.support.annotation.NonNull;
import android.util.Pair;

import com.google.android.mms.pdu_alt.GenericPdu;
import com.google.android.mms.pdu_alt.NotificationInd;
import com.google.android.mms.pdu_alt.PduHeaders;
import com.google.android.mms.pdu_alt.PduParser;

import org.mixedsignals.securesms.ApplicationContext;
import org.mixedsignals.securesms.database.Address;
import org.mixedsignals.securesms.database.DatabaseFactory;
import org.mixedsignals.securesms.database.MmsDatabase;
import org.mixedsignals.securesms.recipients.Recipient;
import org.mixedsignals.securesms.util.Base64;
import org.mixedsignals.securesms.util.Util;

import java.io.IOException;

public class MmsReceiveJob extends BaseJob {

  public static final String KEY = "MmsReceiveJob";

  private static final String TAG = MmsReceiveJob.class.getSimpleName();

  private static final String KEY_DATA            = "data";
  private static final String KEY_SUBSCRIPTION_ID = "subscription_id";

  private byte[] data;
  private int    subscriptionId;

  public MmsReceiveJob(byte[] data, int subscriptionId) {
    this(new Job.Parameters.Builder().setMaxAttempts(25).build(), data, subscriptionId);
  }

  private MmsReceiveJob(@NonNull Job.Parameters parameters, byte[] data, int subscriptionId) {
    super(parameters);

    this.data           = data;
    this.subscriptionId = subscriptionId;
  }

  @Override
  public @NonNull Data serialize() {
    return new Data.Builder().putString(KEY_DATA, Base64.encodeBytes(data))
                             .putInt(KEY_SUBSCRIPTION_ID, subscriptionId)
                             .build();
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() {
    if (data == null) {
      Log.w(TAG, "Received NULL pdu, ignoring...");
      return;
    }

    PduParser  parser = new PduParser(data);
    GenericPdu pdu    = null;

    try {
      pdu = parser.parse();
    } catch (RuntimeException e) {
      Log.w(TAG, e);
    }

    if (isNotification(pdu) && !isBlocked(pdu)) {
      MmsDatabase database                = DatabaseFactory.getMmsDatabase(context);
      Pair<Long, Long> messageAndThreadId = database.insertMessageInbox((NotificationInd)pdu, subscriptionId);

      Log.i(TAG, "Inserted received MMS notification...");

      ApplicationContext.getInstance(context)
                        .getJobManager()
                        .add(new MmsDownloadJob(messageAndThreadId.first,
                                                messageAndThreadId.second,
                                                true));
    } else if (isNotification(pdu)) {
      Log.w(TAG, "*** Received blocked MMS, ignoring...");
    }
  }

  @Override
  public void onCanceled() {
    // TODO
  }

  @Override
  public boolean onShouldRetry(Exception exception) {
    return false;
  }

  private boolean isBlocked(GenericPdu pdu) {
    if (pdu.getFrom() != null && pdu.getFrom().getTextString() != null) {
      Recipient recipients = Recipient.from(context, Address.fromExternal(context, Util.toIsoString(pdu.getFrom().getTextString())), false);
      return recipients.isBlocked();
    }

    return false;
  }

  private boolean isNotification(GenericPdu pdu) {
    return pdu != null && pdu.getMessageType() == PduHeaders.MESSAGE_TYPE_NOTIFICATION_IND;
  }

  public static final class Factory implements Job.Factory<MmsReceiveJob> {
    @Override
    public @NonNull MmsReceiveJob create(@NonNull Parameters parameters, @NonNull Data data) {
      try {
        return new MmsReceiveJob(parameters, Base64.decode(data.getString(KEY_DATA)), data.getInt(KEY_SUBSCRIPTION_ID));
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }
  }
}
