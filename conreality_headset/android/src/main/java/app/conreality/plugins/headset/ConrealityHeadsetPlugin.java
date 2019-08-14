/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.headset;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import org.conreality.sdk.android.Headset;
import org.conreality.sdk.android.HeadsetService;
import org.conreality.sdk.android.HeadsetStatus;

/** ConrealityHeadsetPlugin */
public final class ConrealityHeadsetPlugin implements DefaultLifecycleObserver, ServiceConnection, StreamHandler, MethodCallHandler {
  private static final String TAG = "ConrealityHeadset";
  private static final String METHOD_CHANNEL = "app.conreality.plugins.headset";
  private static final String EVENT_CHANNEL = "app.conreality.plugins.headset/status";

  /** Plugin registration. */
  public static void registerWith(final @NonNull Registrar registrar) {
    assert(registrar != null);

    final ConrealityHeadsetPlugin instance = new ConrealityHeadsetPlugin(registrar);

    new EventChannel(registrar.messenger(), EVENT_CHANNEL)
        .setStreamHandler(instance);
    new MethodChannel(registrar.messenger(), METHOD_CHANNEL)
        .setMethodCallHandler(instance);
  }

  private final @NonNull Registrar registrar;
  private @Nullable HeadsetService service;
  private @Nullable Disposable input;
  private @Nullable EventChannel.EventSink output;

  private ConrealityHeadsetPlugin(final @NonNull Registrar registrar) {
    assert(registrar != null);

    this.registrar = registrar;

    final @NonNull Activity activity = registrar.activity();
    if (activity instanceof LifecycleOwner) {
      ((LifecycleOwner)activity).getLifecycle().addObserver(this);
    }

    final @NonNull Context context = registrar.context();
    final boolean ok = HeadsetService.bind(context, this);
    if (!ok) {
      Log.e(TAG, "Failed to connect to the headset service.");
    }

    // Request the permission to record audio:
    if (!Headset.hasPermissions(context)) {
      Headset.requestPermissions(activity);
    }
  }

  /** Implements android.content.ServiceConnection#onServiceConnected(). */
  @Override
  public void onServiceConnected(final @NonNull ComponentName name, final @NonNull IBinder service) {
    assert(name != null);
    assert(service != null);

    Log.d(TAG, String.format("onServiceConnected: name=%s service=%s", name, service));

    this.service = ((HeadsetService.LocalBinder)service).getService();
    this.input = this.service.listen()
        .observeOn(AndroidSchedulers.mainThread())
        // TODO: error handling
        .subscribe(event -> {
          if (output != null) {
            output.success(event.hasWiredHeadset || event.hasWirelessHeadset);
          }
        });
  }

  /** Implements android.content.ServiceConnection#onServiceDisconnected(). */
  @Override
  public void onServiceDisconnected(final @NonNull ComponentName name) {
    assert(name != null);

    Log.d(TAG, String.format("onServiceDisconnected: name=%s", name));

    if (this.input != null) {
      this.input.dispose();
      this.input = null;
    }
    this.service = null;
  }

  /** Implements io.flutter.plugin.common.EventChannel.StreamHandler#onListen(). */
  @UiThread
  @Override
  public void onListen(final Object _arguments, final @NonNull EventChannel.EventSink events) {
    assert(events != null);

    this.output = events;
    this.output.success(this.isConnected()); // send the initial event
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

  /** Implements io.flutter.plugin.common.MethodChannel.MethodCallHandler#onMethodCall(). */
  @UiThread
  @Override
  public void onMethodCall(final @NonNull MethodCall call, final @NonNull Result result) {
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

      case "playFile": {
        result.success((this.service == null) ? false : this.service.playFile((String)call.arguments));
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

  private boolean isConnected() {
    final HeadsetStatus status = (this.service != null) ? this.service.getStatus() : null;
    return (status != null) && (status.hasWiredHeadset || status.hasWirelessHeadset);
  }
}
