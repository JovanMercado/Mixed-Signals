package org.mixedsignals.resignal.jobs;

import android.support.annotation.NonNull;

import com.annimon.stream.Stream;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.mixedsignals.resignal.database.Address;
import org.mixedsignals.resignal.jobmanager.Data;
import org.mixedsignals.resignal.jobmanager.Job;
import org.mixedsignals.resignal.jobmanager.impl.NetworkConstraint;
import org.mixedsignals.resignal.logging.Log;

import org.mixedsignals.resignal.crypto.UnidentifiedAccessUtil;
import org.mixedsignals.resignal.database.MessagingDatabase.SyncMessageId;
import org.mixedsignals.resignal.dependencies.InjectableType;
import org.mixedsignals.resignal.util.JsonUtils;
import org.mixedsignals.resignal.util.TextSecurePreferences;
import org.whispersystems.signalservice.api.SignalServiceMessageSender;
import org.whispersystems.signalservice.api.crypto.UntrustedIdentityException;
import org.whispersystems.signalservice.api.messages.multidevice.ReadMessage;
import org.whispersystems.signalservice.api.messages.multidevice.SignalServiceSyncMessage;
import org.whispersystems.signalservice.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class MultiDeviceReadUpdateJob extends BaseJob implements InjectableType {

  public static final String KEY = "MultiDeviceReadUpdateJob";

  private static final String TAG = MultiDeviceReadUpdateJob.class.getSimpleName();

  private static final String KEY_MESSAGE_IDS = "message_ids";

  private List<SerializableSyncMessageId> messageIds;

  @Inject SignalServiceMessageSender messageSender;

  public MultiDeviceReadUpdateJob(List<SyncMessageId> messageIds) {
    this(new Job.Parameters.Builder()
                           .addConstraint(NetworkConstraint.KEY)
                           .setLifespan(TimeUnit.DAYS.toMillis(1))
                           .setMaxAttempts(Parameters.UNLIMITED)
                           .build(),
         messageIds);
  }

  private MultiDeviceReadUpdateJob(@NonNull Job.Parameters parameters, @NonNull List<SyncMessageId> messageIds) {
    super(parameters);

    this.messageIds = new LinkedList<>();

    for (SyncMessageId messageId : messageIds) {
      this.messageIds.add(new SerializableSyncMessageId(messageId.getAddress().toPhoneString(), messageId.getTimetamp()));
    }
  }

  @Override
  public @NonNull Data serialize() {
    String[] ids = new String[messageIds.size()];

    for (int i = 0; i < ids.length; i++) {
      try {
        ids[i] = JsonUtils.toJson(messageIds.get(i));
      } catch (IOException e) {
        throw new AssertionError(e);
      }
    }

    return new Data.Builder().putStringArray(KEY_MESSAGE_IDS, ids).build();
  }

  @Override
  public @NonNull String getFactoryKey() {
    return KEY;
  }

  @Override
  public void onRun() throws IOException, UntrustedIdentityException {
    if (!TextSecurePreferences.isMultiDevice(context)) {
      Log.i(TAG, "Not multi device...");
      return;
    }

    List<ReadMessage> readMessages = new LinkedList<>();

    for (SerializableSyncMessageId messageId : messageIds) {
      readMessages.add(new ReadMessage(messageId.sender, messageId.timestamp));
    }

    messageSender.sendMessage(SignalServiceSyncMessage.forRead(readMessages), UnidentifiedAccessUtil.getAccessForSync(context));
  }

  @Override
  public boolean onShouldRetry(Exception exception) {
    return exception instanceof PushNetworkException;
  }

  @Override
  public void onCanceled() {

  }

  private static class SerializableSyncMessageId implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private final String sender;

    @JsonProperty
    private final long   timestamp;

    private SerializableSyncMessageId(@JsonProperty("sender") String sender, @JsonProperty("timestamp") long timestamp) {
      this.sender = sender;
      this.timestamp = timestamp;
    }
  }

  public static final class Factory implements Job.Factory<MultiDeviceReadUpdateJob> {
    @Override
    public @NonNull MultiDeviceReadUpdateJob create(@NonNull Parameters parameters, @NonNull Data data) {
      List<SyncMessageId> ids = Stream.of(data.getStringArray(KEY_MESSAGE_IDS))
                                      .map(id -> {
                                        try {
                                          return JsonUtils.fromJson(id, SerializableSyncMessageId.class);
                                        } catch (IOException e) {
                                          throw new AssertionError(e);
                                        }
                                      })
                                      .map(id -> new SyncMessageId(Address.fromSerialized(id.sender), id.timestamp))
                                      .toList();

      return new MultiDeviceReadUpdateJob(parameters, ids);

    }
  }
}
