/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.headset;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** ConrealityHeadsetPlugin */
public final class ConrealityHeadsetPlugin extends BroadcastReceiver implements BluetoothProfile.ServiceListener, MethodCallHandler, StreamHandler {
  private static final String TAG = "ConrealityHeadset";
  private static final String METHOD_CHANNEL = "app.conreality.plugins.headset";
  private static final String EVENT_CHANNEL = "app.conreality.plugins.headset/status";

  /** Plugin registration. */
  public static void registerWith(final Registrar registrar) {
    final MethodChannel methodChannel = new MethodChannel(registrar.messenger(), METHOD_CHANNEL);
    final EventChannel eventChannel = new EventChannel(registrar.messenger(), EVENT_CHANNEL);
    final ConrealityHeadsetPlugin instance = new ConrealityHeadsetPlugin(registrar);
    methodChannel.setMethodCallHandler(instance);
    eventChannel.setStreamHandler(instance);
  }

  private final Registrar registrar;
  private final BluetoothAdapter bluetoothAdapter;
  private BluetoothHeadset bluetoothHeadset;
  private EventChannel.EventSink events;
  private boolean hasWiredHeadset;
  private boolean hasWirelessHeadset;
  private boolean hasMicrophone;

  ConrealityHeadsetPlugin(final Registrar registrar) {
    this.registrar = registrar;
    this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
  }

  private boolean isConnected() {
    return this.hasWiredHeadset || this.hasWirelessHeadset;
  }

  private void sendStatus() {
    assert(this.events != null);
    this.events.success(this.isConnected());
  }

  /** Implements MethodCallHandler#onMethodCall(). */
  @Override
  public void onMethodCall(final MethodCall call, final Result result) {
    assert(result != null);
    assert(call != null);
    assert(call.method != null);

    switch (call.method) {
      case "isConnected": {
        result.success(this.isConnected());
        break;
      }

      default: {
        result.notImplemented();
      }
    }
  }

  /** Implements StreamHandler#onListen(). */
  @Override
  public void onListen(final Object _arguments, final EventChannel.EventSink events) {
    this.events = events;

    final Context context = this.registrar.context();

    if (this.bluetoothAdapter != null && this.bluetoothAdapter.isEnabled()) {
      final boolean ok = this.bluetoothAdapter.getProfileProxy(context, this, BluetoothProfile.HEADSET);
      if (!ok) {
        Log.e(TAG, "Failed to connect to the Bluetooth headset profile proxy.");
      }
    }

    context.registerReceiver(this, new IntentFilter(AudioManager.ACTION_HEADSET_PLUG));
    context.registerReceiver(this, new IntentFilter(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED));
  }

  /** Implements StreamHandler#onCancel(). */
  @Override
  public void onCancel(final Object _arguments) {
    this.registrar.context().unregisterReceiver(this);

    if (this.bluetoothAdapter != null && this.bluetoothHeadset != null) {
      this.bluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, this.bluetoothHeadset);
      this.bluetoothHeadset = null;
    }

    this.events = null;
  }

  /** Implements BroadcastReceiver#onReceive(). */
  @Override
  public void onReceive(final Context context, final Intent intent) {
    switch (intent.getAction()) {
      case AudioManager.ACTION_HEADSET_PLUG: {
        final int state = intent.getIntExtra("state", -1);
        final int microphone = intent.getIntExtra("microphone", -1);
        if (Log.isLoggable(TAG, Log.DEBUG)) {
          final String name = intent.getStringExtra("name");
          Log.d(TAG, String.format("Received broadcast: %s state=%d microphone=%d name=%s", intent.toString(), state, microphone, name));
        }
        this.hasWiredHeadset = (state == 1);
        this.hasMicrophone = (microphone == 1);
        this.sendStatus();
        break;
      }

      case BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED: {
        final int state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1);
        if (Log.isLoggable(TAG, Log.DEBUG)) {
          final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
          Log.d(TAG, String.format("Received broadcast: %s state=%d device=%s", intent.toString(), state, device.toString()));
        }
        this.hasWirelessHeadset = (state == BluetoothProfile.STATE_CONNECTED);
        this.sendStatus();
        break;
      }

      default: break; // ignore UFOs
    }
  }

  /** Implements BluetoothProfile.ServiceListener#onServiceConnected(). */
  @Override
  public void onServiceConnected(final int profile, final BluetoothProfile proxy) {
    if (profile == BluetoothProfile.HEADSET) {
      if (Log.isLoggable(TAG, Log.DEBUG)) {
        Log.d(TAG, "Connected to the Bluetooth headset profile proxy.");
      }
      this.bluetoothHeadset = (BluetoothHeadset)proxy;
      this.hasWirelessHeadset = (proxy.getConnectedDevices().size() > 0);
      this.sendStatus();
    }
  }

  /** Implements BluetoothProfile.ServiceListener#onServiceDisconnected(). */
  @Override
  public void onServiceDisconnected(final int profile) {
    if (profile == BluetoothProfile.HEADSET) {
      if (Log.isLoggable(TAG, Log.INFO)) {
        Log.i(TAG, "Disconnected from the Bluetooth headset profile proxy.");
      }
      this.bluetoothHeadset = null;
      this.hasWirelessHeadset = false;
      this.sendStatus();
    }
  }
}
