/* This is free and unencumbered software released into the public domain. */

/// Player heart-rate monitoring support for Conreality live-action games.
library conreality_pulse;

import 'dart:async';

import 'package:flutter/services.dart';

/// The `conreality_pulse` plugin.
abstract class ConrealityPulse {
  ConrealityPulse._();

  /// The current plugin version string.
  static String get version => "0.0.1";

  static const MethodChannel _channel =
      const MethodChannel('app.conreality.plugins.pulse');

  static Future<String> get platformVersion {
    return _channel.invokeMethod('getPlatformVersion');
  }
}
