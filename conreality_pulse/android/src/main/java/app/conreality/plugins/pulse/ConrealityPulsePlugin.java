/* This is free and unencumbered software released into the public domain. */

package app.conreality.plugins.pulse;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import java.util.HashMap;
import java.util.Map;

/** ConrealityPulsePlugin */
public class ConrealityPulsePlugin implements MethodCallHandler {
  static final String CHANNEL = "app.conreality.plugins.pulse";

  /** Plugin registration. */
  public static void registerWith(final Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), CHANNEL);
    channel.setMethodCallHandler(new ConrealityPulsePlugin(registrar));
  }

  private final Registrar registrar;
  private final Map<Integer, EventChannel> channels = new HashMap<>();

  ConrealityPulsePlugin(final Registrar registrar) {
    this.registrar = registrar;
  }

  @Override
  public void onMethodCall(final MethodCall call, final Result result) {
    assert(result != null);
    assert(call != null);
    assert(call.method != null);

    switch (call.method) {
      case "openEventChannel": {
        final int channelKey = this.channels.size() + 1;
        final String channelName = String.format("%s/%d", CHANNEL, channelKey);
        final EventChannel channel = new EventChannel(this.registrar.messenger(), channelName);
        final EventChannel.StreamHandler handler = new RandomPulseStream(this.registrar.context()); // TODO
        channel.setStreamHandler(handler);
        this.channels.put(channelKey, channel);
        result.success(channelName);
        break;
      }

      case "closeEventChannel": {
        // TODO
        break;
      }

      default: {
        result.notImplemented();
      }
    }
  }
}
