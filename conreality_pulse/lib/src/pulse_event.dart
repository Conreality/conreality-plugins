/* This is free and unencumbered software released into the public domain. */

import 'package:flutter/material.dart' show required;

/// Represents a heart-rate measurement event.
class PulseEvent {
  PulseEvent({@required this.value});

  /// The current heart-rate measurement, in beats per minute (BPM).
  final int value;

  /// Returns a string representation of this event.
  @override
  String toString() => 'PulseEvent(value: $value)';
}
