/* This is free and unencumbered software released into the public domain. */

import 'dart:async' show Future, Stream;
import 'dart:convert';

import 'package:flutter/services.dart' show EventChannel, MethodChannel;

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
  static Future<Stream<HeadphoneStatus>> subscribe() async {
    return _eventChannel.receiveBroadcastStream().map(
      (dynamic json) {
        return HeadphoneStatus.fromJson(jsonDecode(json));
      },
    );
  }
}

class HeadphoneStatus {
  bool isConnected;
  bool hasWiredHeadset;
  bool hasWirelessHeadset;
  bool hasMicrophone;
  bool hasInbuiltMicroPhone;
  bool hasHeadsetMicroPhone;

  HeadphoneStatus({
    this.isConnected,
    this.hasWiredHeadset,
    this.hasWirelessHeadset,
    this.hasMicrophone,
    this.hasInbuiltMicroPhone,
    this.hasHeadsetMicroPhone,
  });

  static fromJson(Map<String, dynamic> json) {
    return HeadphoneStatus(
      isConnected: json["isConnected"],
      hasWiredHeadset: json["hasWiredHeadset"],
      hasWirelessHeadset: json["hasWirelessHeadset"],
      hasMicrophone: json["hasMicrophone"],
      hasInbuiltMicroPhone: json["hasInbuiltMicroPhone"],
      hasHeadsetMicroPhone: json["hasHeadsetMicroPhone"],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      "isConnected": isConnected,
      "hasWiredHeadset": hasWiredHeadset,
      "hasWirelessHeadset": hasWirelessHeadset,
      "hasMicrophone": hasMicrophone,
      "hasInbuiltMicroPhone": hasInbuiltMicroPhone,
      "hasHeadsetMicroPhone": hasHeadsetMicroPhone,
    };
  }
}
