/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.pulse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import org.conreality.sdk.android.Pulse;

/** ConrealityPulsePlugin */
public final class ConrealityPulsePlugin implements EventChannel.StreamHandler {
  static final String CHANNEL = "app.conreality.plugins.pulse";

  /** Plugin registration. */
  public static void registerWith(final @NonNull Registrar registrar) {
    assert(registrar != null);

    final EventChannel channel = new EventChannel(registrar.messenger(), CHANNEL);
    channel.setStreamHandler(new ConrealityPulsePlugin(registrar));
  }

  private final @NonNull Registrar registrar;
  private @Nullable Disposable input;
  private @Nullable EventChannel.EventSink output;

  private ConrealityPulsePlugin(final @NonNull Registrar registrar) {
    this.registrar = registrar;
  }

  /** Implements io.flutter.plugin.common.EventChannel.StreamHandler#onListen(). */
  @UiThread
  @Override
  public void onListen(final Object _arguments, final @NonNull EventChannel.EventSink events) {
    assert(events != null);

    this.input = Pulse.measure()
        .observeOn(AndroidSchedulers.mainThread())
        // TODO: error handling
        .subscribe(value -> {
          events.success(value.intValue());
        });
    this.output = events;
  }

  /** Implements io.flutter.plugin.common.EventChannel.StreamHandler#onCancel(). */
  @UiThread
  @Override
  public void onCancel(final Object _arguments) {
    if (this.input != null) {
      this.input.dispose();
      this.input = null;
    }
    if (this.output != null) {
      this.output.endOfStream();
      this.output = null;
    }
  }
}
