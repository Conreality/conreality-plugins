/* This is free and unencumbered software released into the public domain. */

import 'dart:async' show Future, Stream;

import 'package:flutter/services.dart' show EventChannel, MethodChannel;

import 'headset_event.dart' show HeadsetEvent;
import 'headset_status.dart' show HeadsetStatus;
import 'headset_type.dart' show HeadsetType;

const MethodChannel _methodChannel =
    MethodChannel('app.conreality.plugins.headset');

const EventChannel _eventChannel =
    EventChannel('app.conreality.plugins.headset/status');

/// Headset device interface.
abstract class Headset {
  Headset._();

  /// Indicates if a headset device is currently connected.
  static Future<bool> get isConnected async =>
      await _methodChannel.invokeMethod('isConnected');

  /// Indicates if speech synthesis (aka text-to-speech) is supported.
  static Future<bool> get canSpeak async =>
      await _methodChannel.invokeMethod('canSpeak');

  /// Synthesizes speech from the contents of [message] for immediate playback.
  static Future<bool> speak(final String message) {
    return _methodChannel.invokeMethod('speak', message);
  }

  /// Stops any ongoing speech synthesis immediately.
  static Future<bool> stopSpeaking() {
    return _methodChannel.invokeMethod('stopSpeaking');
  }

  /// Subscribes to headset device events.
  static Future<Stream<HeadsetEvent>> subscribe() async {
    return _eventChannel.receiveBroadcastStream().map((dynamic isConnected) =>
        HeadsetStatus(
            type: HeadsetType.wired, isConnected: isConnected as bool));
  }
}
