/* This is free and unencumbered software released into the public domain. */

import 'dart:async' show Future, Stream;

import 'package:flutter/services.dart' show EventChannel;

import 'pulse_event.dart' show PulseEvent;

const EventChannel _channel = EventChannel('app.conreality.plugins.pulse');

/// Heart-rate measurement interface.
abstract class Pulse {
  Pulse._();

  /// TODO
  static Future<bool> get isAvailable async => false; // TODO

  /// Subscribes to heart-rate measurement events.
  static Future<Stream<PulseEvent>> subscribe() async {
    return _channel
        .receiveBroadcastStream()
        .map((dynamic value) => PulseEvent(value: value as int));
  }
}
