/* This is free and unencumbered software released into the public domain. */

import 'dart:async' show Future, Stream;

import 'package:flutter/services.dart' show EventChannel, MethodChannel;

import 'pulse_event.dart' show PulseEvent;

const MethodChannel _channel = MethodChannel('app.conreality.plugins.pulse');

/// TODO
abstract class Pulse {
  Pulse._();

  /// TODO
  static Future<bool> get isAvailable async => false; // TODO

  /// TODO
  static Future<Stream<PulseEvent>> subscribe() async {
    final EventChannel events = await _openEventChannel();
    return events.receiveBroadcastStream().map((dynamic value) => PulseEvent(value: value as int));
  }

  static Future<EventChannel> _openEventChannel() async {
    final String channelName = await _channel.invokeMethod('openEventChannel');
    return (channelName != null) ? EventChannel(channelName) : null;
  }
}
