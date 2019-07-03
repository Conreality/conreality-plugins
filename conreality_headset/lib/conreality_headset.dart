import 'dart:async';

import 'package:flutter/services.dart';

class ConrealityHeadset {
  static const MethodChannel _channel =
      const MethodChannel('conreality_headset');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
