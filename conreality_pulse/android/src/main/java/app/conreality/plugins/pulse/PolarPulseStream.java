/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.pulse;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import io.flutter.plugin.common.EventChannel;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import polar.com.sdk.api.PolarBleApi;
import polar.com.sdk.api.PolarBleApiCallback;
import polar.com.sdk.api.PolarBleApiDefaultImpl;
import polar.com.sdk.api.errors.PolarInvalidArgument;
import polar.com.sdk.api.model.PolarDeviceInfo;
import polar.com.sdk.api.model.PolarHrData;

/** PolarPulseStream */
class PolarPulseStream extends PolarBleApiCallback implements EventChannel.StreamHandler {
  private static final String TAG = "ConrealityPulse";

  private final PolarBleApi api;
  private EventChannel.EventSink events;
  private String deviceID;

  PolarPulseStream(final Context context) {
    this.api = PolarBleApiDefaultImpl.defaultImplementation(context, PolarBleApi.FEATURE_HR);
    this.api.setApiCallback(this);
    this.api.setAutomaticReconnection(true);
  }

  @Override
  public void onListen(final Object _arguments, final EventChannel.EventSink events) {
    Log.d(TAG, "Discovering devices...");
    this.events = events;
    this.api.autoConnectToDevice(-60/*dBm*/, "180D", null).subscribe(
      new Action() {
        @Override
        public void run() throws Exception {
          Log.i(TAG, "Discovered devices.");
        }
      },
      new Consumer<Throwable>() {
        @Override
        public void accept(final Throwable throwable) throws Exception {
          Log.e(TAG, "Failed to discover devices.", throwable);
        }
      }
    );
  }

  @Override
  public void onCancel(final Object _arguments) {
    Log.i(TAG, "Disconnecting and terminating...");
    if (this.deviceID != null) {
      try {
        this.api.disconnectFromDevice(this.deviceID);
      }
      catch (final PolarInvalidArgument error) {
        throw new RuntimeException(error);
      }
      this.deviceID = null;
    }
    this.api.shutDown();
    this.events = null;
  }

  @Override
  public void deviceConnecting(final PolarDeviceInfo device) {
    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(TAG, "Connecting to " + device.deviceId + " (" + device.rssi + " dBm)...");
    }
    // TODO: events.success(...);
  }

  @Override
  public void deviceConnected(final PolarDeviceInfo device) {
    if (Log.isLoggable(TAG, Log.INFO)) {
      Log.i(TAG, "Connected to " + device.deviceId + " (" + device.rssi + " dBm).");
    }
    this.deviceID = device.deviceId;
    // TODO: events.success(...);
  }

  @Override
  public void deviceDisconnected(final PolarDeviceInfo device) {
    if (Log.isLoggable(TAG, Log.INFO)) {
      Log.i(TAG, "Disconnected from " + device.deviceId + ".");
    }
    this.deviceID = null;
    // TODO: events.success(...);
  }

  @Override
  public void hrFeatureReady(final String deviceID) { // HR notifications are about to start
    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(TAG, "Preparing to receive from " + deviceID + "...");
    }
  }

  @Override
  public void hrNotificationReceived(final String deviceID, final PolarHrData data) {
    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(TAG, "Received a reading of " + data.hr + " bpm.");
    }
    events.success(data.hr);
  }
}
