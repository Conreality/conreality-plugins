/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.beacon;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.conreality.sdk.android.BeaconService;

/** ConrealityBeaconPlugin */
public final class ConrealityBeaconPlugin implements DefaultLifecycleObserver, ServiceConnection, MethodCallHandler, StreamHandler {
  private static final String TAG = "ConrealityBeacon";
  private static final String METHOD_CHANNEL = "app.conreality.plugins.beacon";
  private static final String EVENT_CHANNEL = "app.conreality.plugins.beacon/scan";

  /** Plugin registration. */
  public static void registerWith(final @NonNull Registrar registrar) {
    assert(registrar != null);

    final MethodChannel methodChannel = new MethodChannel(registrar.messenger(), METHOD_CHANNEL);
    final EventChannel eventChannel = new EventChannel(registrar.messenger(), EVENT_CHANNEL);
    final ConrealityBeaconPlugin instance = new ConrealityBeaconPlugin(registrar);
    methodChannel.setMethodCallHandler(instance);
    eventChannel.setStreamHandler(instance);
  }

  private final @NonNull Registrar registrar;
  private @Nullable BeaconService service;
  private @Nullable EventChannel.EventSink events;

  @SuppressWarnings("deprecation")
  ConrealityBeaconPlugin(final @NonNull Registrar registrar) {
    assert(registrar != null);

    this.registrar = registrar;

    final @NonNull Activity activity = registrar.activity();
    if (activity instanceof LifecycleOwner) {
      ((LifecycleOwner)activity).getLifecycle().addObserver(this);
    }
  }

  /** Implements MethodCallHandler#onMethodCall(). */
  @Override
  public void onMethodCall(final @NonNull MethodCall call, final @NonNull Result result) {
    assert(result != null);
    assert(call != null);
    assert(call.method != null);

    switch (call.method) {
      default: {
        result.notImplemented();
      }
    }
  }

  /** Implements StreamHandler#onListen(). */
  @Override
  public void onListen(final @Nullable Object _arguments, final @NonNull EventChannel.EventSink events) {
    assert(events != null);

    this.events = events;

    final @NonNull Context context = this.registrar.context();
    final boolean ok = BeaconService.bind(context, this);
    if (!ok) {
      Log.e(TAG, "Failed to connect to the bound service.");
    }
  }

  /** Implements StreamHandler#onCancel(). */
  @Override
  public void onCancel(final @Nullable Object _arguments) {
    this.events = null;
  }

  /** Implements android.content.ServiceConnection#onServiceConnected(). */
  @Override
  public void onServiceConnected(final @NonNull ComponentName name, final @NonNull IBinder service) {
    assert(name != null);
    assert(service != null);

    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(TAG, String.format("ConrealityBeaconPlugin.onServiceConnected: name=%s service=%s", name, service));
    }

    this.service = ((BeaconService.LocalBinder)service).getService();
    this.service.onConnection(this.registrar.activity(), this.registrar.context());
    this.service.setRangeNotifier(new RangeNotifier() {
      @Override
      public void didRangeBeaconsInRegion(final @NonNull Collection<Beacon> beacons, final @NonNull Region region) {
        assert(beacons != null);
        assert(region != null);

        final List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

        for (final Beacon beacon : beacons) {
          if (Log.isLoggable(TAG, Log.DEBUG) || true) {
            Log.d(TAG, String.format("Ranged beacon %s of type %d at %.2fm", beacon, beacon.getBeaconTypeCode(), beacon.getDistance()));
          }
          final HashMap<String, Object> entry = new HashMap<String, Object>();
          entry.put("protocol", beacon.getBeaconTypeCode() == 0x4c000215 ? 2/*BeaconProtocol.ibeacon*/ : 0/*BeaconProtocol.unknown*/);
          entry.put("id1", beacon.getId1().toString());
          entry.put("id2", beacon.getId2().toString());
          entry.put("id3", beacon.getId3().toString());
          entry.put("referenceRSSI", Double.valueOf(beacon.getTxPower()));
          entry.put("measuredRSSI", Double.valueOf(beacon.getRssi()));
          entry.put("estimatedDistance", Double.valueOf(beacon.getDistance()));
          data.add(entry);
        }

        if (events != null) {
          events.success(data); // emit a BeaconScan event
        }
      }
    });
  }

  /** Implements android.content.ServiceConnection#onServiceDisconnected(). */
  @Override
  public void onServiceDisconnected(final @NonNull ComponentName name) {
    assert(name != null);

    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(TAG, String.format("ConrealityBeaconPlugin.onServiceDisconnected: name=%s", name));
    }

    if (this.service != null) {
      this.service.setRangeNotifier(null);
      this.service = null;
    }
  }

  /** Implements DefaultLifecycleObserver#onDestroy(). */
  @Override
  public void onDestroy(final @NonNull LifecycleOwner owner) {
    assert(owner != null);

    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(TAG, String.format("ConrealityBeaconPlugin.onDestroy: owner=%s", owner));
    }

    if (this.service != null) {
      this.registrar.context().unbindService(this);
      this.service = null;
    }
  }
}
