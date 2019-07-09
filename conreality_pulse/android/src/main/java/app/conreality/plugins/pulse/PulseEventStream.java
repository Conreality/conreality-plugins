/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.pulse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import io.flutter.plugin.common.EventChannel;

/** PulseEventStream */
class PulseEventStream implements EventChannel.StreamHandler {
  private final Handler handler = new Handler();
  private final Runnable runnable = new Runnable() {
    @Override
    public void run() {
      assert(events != null);
      events.success(counter++);
      handler.postDelayed(this, 1000); // one second
    }
  };
  private EventChannel.EventSink events;
  private int counter = 1;

  PulseEventStream(final Context context) {}

  @Override
  public void onListen(final Object _arguments, final EventChannel.EventSink events) {
    this.events = events;
    this.runnable.run();
  }

  @Override
  public void onCancel(final Object _arguments) {
    this.handler.removeCallbacks(this.runnable);
  }
}
