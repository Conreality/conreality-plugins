/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.beacon;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

/** BeaconService */
public final class BeaconService extends Service implements DefaultLifecycleObserver, BeaconConsumer {
  private static final String TAG = "ConrealityBeacon";
  private static final String IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";
  private static final String ALTBEACON_LAYOUT = BeaconParser.ALTBEACON_LAYOUT;

  public final class LocalBinder extends Binder {
    public @NonNull BeaconService getService() {
      return BeaconService.this;
    }
  }

  private final @NonNull IBinder binder = new LocalBinder();
  private @Nullable BeaconManager beaconManager;

  void setRangeNotifier(final @Nullable RangeNotifier rangeNotifier) {
    assert(this.beaconManager != null);

    this.beaconManager.setRangeNotifier(rangeNotifier);
  }

  /** Implements Service#onBind(). */
  @Override
  public @NonNull IBinder onBind(final @NonNull Intent intent) {
    return this.binder;
  }

  /** Implements Service#onCreate(). */
  @Override
  public void onCreate() {
    Log.i(TAG, "Created the bound service.");
  }

  public void onConnection(final @NonNull Activity activity, final @NonNull Context context) {
    assert(activity != null);
    assert(context != null);

    if (activity instanceof LifecycleOwner) {
      ((LifecycleOwner)activity).getLifecycle().addObserver(this);
    }

    this.beaconManager = BeaconManager.getInstanceForApplication(context);
    this.beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
    this.beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(ALTBEACON_LAYOUT));
    this.beaconManager.bind(this);
    //this.beaconManager.setDebug(true); // DEBUG
  }

  /** Implements Service#onDestroy(). */
  @Override
  public void onDestroy() {
    Log.d(TAG, "Terminating the bound service...");
    if (this.beaconManager != null) {
      this.beaconManager.unbind(this);
      this.beaconManager = null;
    }
    Log.i(TAG, "Terminated the bound service.");
  }

  /** Implements Service#onStartCommand(). */
  @Override
  public int onStartCommand(final @NonNull Intent intent, final int flags, final int startID) {
    assert(intent != null);

    final String action = (intent != null) ? intent.getAction() : null;
    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(TAG, String.format("BeaconService.onStartCommand: intent=%s flags=%d startID=%d action=%s", intent, flags, startID, action));
    }
    return START_REDELIVER_INTENT;
  }

  /** Implements BeaconConsumer#onBeaconServiceConnect(). */
  @Override
  public void onBeaconServiceConnect() {
    try {
      this.beaconManager.startRangingBeaconsInRegion(new Region("Anywhere", null, null, null)); // TODO
    }
    catch (final RemoteException error) {
      throw new RuntimeException(error);
    }
  }
}
