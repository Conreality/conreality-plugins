/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.headset;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
public class ConrealityHeadsetPlugin extends BroadcastReceiver implements MethodCallHandler, StreamHandler {
  private static final String TAG = "ConrealityHeadset";
  private static final String CHANNEL = "app.conreality.plugins.headset";

  /** Plugin registration. */
  public static void registerWith(final Registrar registrar) {
    final MethodChannel methodChannel = new MethodChannel(registrar.messenger(), CHANNEL);
    final EventChannel eventChannel = new EventChannel(registrar.messenger(), String.format("%s/status", CHANNEL));
    final ConrealityHeadsetPlugin instance = new ConrealityHeadsetPlugin(registrar);
    methodChannel.setMethodCallHandler(instance);
    eventChannel.setStreamHandler(instance);
  }

  private final Registrar registrar;
  private EventChannel.EventSink events;
  private boolean isConnected;

  ConrealityHeadsetPlugin(final Registrar registrar) {
    this.registrar = registrar;
  }

  /** Implements MethodCallHandler#onMethodCall(). */
  @Override
  public void onMethodCall(final MethodCall call, final Result result) {
    assert(result != null);
    assert(call != null);
    assert(call.method != null);

    switch (call.method) {
      case "isConnected": {
        result.success(this.isConnected);
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
    this.registrar.context().registerReceiver(this, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
  }

  /** Implements StreamHandler#onCancel(). */
  @Override
  public void onCancel(final Object _arguments) {
    this.registrar.context().unregisterReceiver(this);
    this.events = null;
  }

  /** Implements BroadcastReceiver#onReceive(). */
  @Override
  public void onReceive(final Context context, final Intent intent) {
    switch (intent.getAction()) {
      case "android.intent.action.HEADSET_PLUG": {
        Log.i(TAG, "BroadcastReceiver#onReceive: " + intent.toString() + " state=" + intent.getIntExtra("state", -1) + " microphone=" + intent.getIntExtra("microphone", -1) + " name=" + intent.getStringExtra("name"));
        this.isConnected = (intent.getIntExtra("state", -1) == 1);
        this.events.success(this.isConnected); // TODO
        break;
      }
      default: {
        Log.w(TAG, "BroadcastReceiver#onReceive: " + intent.toString());
      }
    }
  }
}
