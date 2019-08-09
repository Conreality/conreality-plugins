/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.pulse;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import org.conreality.sdk.android.Pulse;
import org.conreality.sdk.android.PulseService;

/** ConrealityPulsePlugin */
public final class ConrealityPulsePlugin implements ServiceConnection, EventChannel.StreamHandler {
  private static final String TAG = "ConrealityPulse";
  private static final String CHANNEL = "app.conreality.plugins.pulse";

  /** Plugin registration. */
  public static void registerWith(final @NonNull Registrar registrar) {
    assert(registrar != null);

    final EventChannel channel = new EventChannel(registrar.messenger(), CHANNEL);
    channel.setStreamHandler(new ConrealityPulsePlugin(registrar));
  }

  private final @NonNull Registrar registrar;
  private @Nullable PulseService service;
  private @Nullable Disposable input;
  private @Nullable EventChannel.EventSink output;

  private ConrealityPulsePlugin(final @NonNull Registrar registrar) {
    this.registrar = registrar;

    final boolean ok = PulseService.bind(registrar.context(), this);
    if (!ok) {
      Log.e(TAG, "Failed to connect to the pulse service.");
    }
  }

  /** Implements android.content.ServiceConnection#onServiceConnected(). */
  @Override
  public void onServiceConnected(final @NonNull ComponentName name, final @NonNull IBinder service) {
    assert(name != null);
    assert(service != null);

    Log.d(TAG, String.format("onServiceConnected: name=%s service=%s", name, service));

    this.service = ((PulseService.LocalBinder)service).getService();
    this.input = this.service.measure()
        .observeOn(AndroidSchedulers.mainThread())
        // TODO: error handling
        .subscribe(value -> {
          if (output != null) {
            output.success(value.intValue());
          }
        });
  }

  /** Implements android.content.ServiceConnection#onServiceDisconnected(). */
  @Override
  public void onServiceDisconnected(final @NonNull ComponentName name) {
    assert(name != null);

    Log.d(TAG, String.format("onServiceDisconnected: name=%s", name));

    this.service = null;
    if (this.input != null) {
      this.input.dispose();
      this.input = null;
    }
  }

  /** Implements io.flutter.plugin.common.EventChannel.StreamHandler#onListen(). */
  @UiThread
  @Override
  public void onListen(final Object _arguments, final @NonNull EventChannel.EventSink events) {
    assert(events != null);

    this.output = events;
  }

  /** Implements io.flutter.plugin.common.EventChannel.StreamHandler#onCancel(). */
  @UiThread
  @Override
  public void onCancel(final Object _arguments) {
    if (this.output != null) {
      this.output.endOfStream();
      this.output = null;
    }
  }
}
