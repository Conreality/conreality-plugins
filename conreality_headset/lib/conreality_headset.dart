/* This is free and unencumbered software released into the public domain. */

/// Headset audio and text-to-speech support for Conreality live-action games.
library conreality_headset;

import 'dart:async';

import 'package:flutter/services.dart';

/// The `conreality_headset` plugin.
abstract class ConrealityHeadset {
  ConrealityHeadset._();

  /// The current plugin version string.
  static String get version => "0.0.1";

  static const MethodChannel _channel =
      const MethodChannel('app.conreality.plugins.headset');

  static Future<String> get platformVersion {
    return _channel.invokeMethod('getPlatformVersion');
  }
}
