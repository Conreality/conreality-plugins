/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.headset;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.util.Log;
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

/** ConrealityHeadsetPlugin */
public final class ConrealityHeadsetPlugin extends BroadcastReceiver implements DefaultLifecycleObserver, ServiceConnection, MethodCallHandler, StreamHandler, BluetoothProfile.ServiceListener {
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
  private HeadsetService service;
  private BluetoothHeadset bluetoothHeadset;
  private EventChannel.EventSink events;
  private boolean hasWiredHeadset;
  private boolean hasWirelessHeadset;
  private boolean hasMicrophone;

  @SuppressWarnings("deprecation")
  ConrealityHeadsetPlugin(final Registrar registrar) {
    this.registrar = registrar;
    this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    final Activity activity = registrar.activity();
    if (activity instanceof LifecycleOwner) {
      ((LifecycleOwner)activity).getLifecycle().addObserver(this);
    }

    final Context context = this.registrar.context();
    final boolean ok = context.bindService(new Intent(context, HeadsetService.class), this, Context.BIND_AUTO_CREATE);
    if (!ok) {
      Log.e(TAG, "Failed to connect to the bound service.");
    }

    final AudioManager audioManager = (AudioManager)registrar.context().getSystemService(Context.AUDIO_SERVICE);
    this.hasWiredHeadset = audioManager.isWiredHeadsetOn();
    this.hasWirelessHeadset = audioManager.isBluetoothA2dpOn() || audioManager.isBluetoothScoOn();
  }

  private boolean isConnected() {
    return this.hasWiredHeadset || this.hasWirelessHeadset;
  }

  private void sendEvent(final Object event) {
    assert(this.events != null);
    this.events.success(event);
  }

  private void sendStatus() {
    this.sendEvent(this.isConnected());
  }

  /** Implements ServiceConnection#onServiceConnected(). */
  @Override
  public void onServiceConnected(final ComponentName name, final IBinder service) {
    Log.d(TAG, String.format("onServiceConnected: name=%s service=%s", name, service));
    this.service = ((HeadsetService.LocalBinder)service).getService();
    this.service.onConnection(this.registrar.context());
  }

  /** Implements ServiceConnection#onServiceDisconnected(). */
  @Override
  public void onServiceDisconnected(final ComponentName name) {
    Log.d(TAG, String.format("onServiceDisconnected: name=%s", name));
    this.service = null;
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

      case "canSpeak": {
        result.success((this.service == null) ? false : this.service.canSpeak());
        break;
      }

      case "speak": {
        result.success((this.service == null) ? false : this.service.speak((String)call.arguments));
        break;
      }

      case "stopSpeaking": {
        result.success((this.service == null) ? false : this.service.stopSpeaking());
        break;
      }

      case "shutdown": {
        if (this.service != null) {
          this.registrar.context().unbindService(this);
          this.service = null;
        }
        result.success(null);
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
        Log.e(TAG, "Failed to connect to the Bluetooth headset service.");
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
        Log.d(TAG, "Connected to the Bluetooth headset service.");
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
        Log.i(TAG, "Disconnected from the Bluetooth headset service.");
      }
      this.bluetoothHeadset = null;
      this.hasWirelessHeadset = false;
      this.sendStatus();
    }
  }
}
