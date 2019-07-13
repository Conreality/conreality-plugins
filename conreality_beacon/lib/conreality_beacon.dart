/* This is free and unencumbered software released into the public domain. */

/// Radio beacon gadget support for Conreality live-action games.
library conreality_beacon;

import 'dart:async';

import 'package:flutter/services.dart';

/// The `conreality_beacon` plugin.
abstract class ConrealityBeacon {
  ConrealityBeacon._();

  /// The current plugin version string.
  static String get version => "0.0.1";

  static const MethodChannel _channel =
      const MethodChannel('app.conreality.plugins.beacon');

  static Future<String> get platformVersion {
    return _channel.invokeMethod('getPlatformVersion');
  }
}
