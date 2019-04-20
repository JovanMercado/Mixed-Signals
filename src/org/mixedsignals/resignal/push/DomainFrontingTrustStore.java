package org.mixedsignals.resignal.push;


import android.content.Context;

import org.mixedsignals.resignal.R;
import org.whispersystems.signalservice.api.push.TrustStore;

import java.io.InputStream;

public class DomainFrontingTrustStore implements TrustStore {

  private final Context context;

  public DomainFrontingTrustStore(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public InputStream getKeyStoreInputStream() {
    return context.getResources().openRawResource(R.raw.censorship_fronting);
  }

  @Override
  public String getKeyStorePassword() {
    return "whisper";
  }

}
