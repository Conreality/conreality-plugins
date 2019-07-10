/* This is free and unencumbered software released into the public domain. */

import 'package:flutter/material.dart' show required;

import 'headset_event.dart' show HeadsetEvent;
import 'headset_type.dart' show HeadsetType;

/// Represents the headset device status.
class HeadsetStatus extends HeadsetEvent {
  HeadsetStatus({@required this.isConnected, this.type = HeadsetType.unknown});

  /// Whether the headset is wireless or wired.
  final HeadsetType type;

  /// Whether the headset is currently connected.
  final bool isConnected;

  /// Returns a string representation of this status.
  @override
  String toString() => 'HeadsetStatus(type: $type, isConnected: $isConnected)';
}
