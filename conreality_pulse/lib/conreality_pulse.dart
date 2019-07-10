/* This is free and unencumbered software released into the public domain. */

/// Player heart-rate monitoring support for Conreality live-action games.
///
/// {@canonicalFor pulse.Pulse}
/// {@canonicalFor pulse.PulseEvent}
library conreality_pulse;

export 'src/pulse.dart' show Pulse;
export 'src/pulse_event.dart' show PulseEvent;

/// The `conreality_pulse` plugin.
abstract class ConrealityPulse {
  ConrealityPulse._();

  /// The current plugin version string.
  static String get version => "0.1.0";
}
