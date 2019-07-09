/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.pulse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import io.flutter.plugin.common.EventChannel;

/** SensorPulseStream */
class SensorPulseStream implements EventChannel.StreamHandler {
  private static final String TAG = "Conreality/SensorPulseStream";

  private final SensorManager sensorManager;
  private final Sensor sensor;
  private SensorEventListener sensorEventListener;

  SensorPulseStream(final Context context) {
    this.sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
    this.sensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
    assert(this.sensor != null);
  }

  @Override
  public void onListen(final Object _arguments, final EventChannel.EventSink events) {
    this.sensorEventListener = createSensorEventListener(events);
    final boolean ok = this.sensorManager.registerListener(this.sensorEventListener,
        this.sensor, SensorManager.SENSOR_DELAY_NORMAL, 0);
    if (!ok) {
      Log.e(TAG, "Failed to register TYPE_HEART_RATE sensor listener");
    }
  }

  @Override
  public void onCancel(final Object _arguments) {
    if (this.sensorEventListener != null) {
      this.sensorManager.unregisterListener(this.sensorEventListener);
      this.sensorEventListener= null;
    }
  }

  SensorEventListener createSensorEventListener(final EventChannel.EventSink events) {
    return new SensorEventListener() {
      @Override
      public void onAccuracyChanged(final Sensor sensor, final int accuracy) {}

      @Override
      public void onSensorChanged(final SensorEvent event) {
        assert(event.sensor.getType() == Sensor.TYPE_HEART_RATE);
        double[] sensorValues = new double[event.values.length];
        for (int i = 0; i < event.values.length; i++) {
          sensorValues[i] = event.values[i];
        }
        events.success(0); // TODO: sensorValues[0]?
      }
    };
  }
}
