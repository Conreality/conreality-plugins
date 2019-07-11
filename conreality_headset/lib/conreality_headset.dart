/* This is free and unencumbered software released into the public domain. */

/// Headset audio and text-to-speech support for Conreality live-action games.
///
/// {@canonicalFor headset.Headset}
/// {@canonicalFor headset_event.HeadsetEvent}
/// {@canonicalFor headset_status.HeadsetStatus}
/// {@canonicalFor headset_type.HeadsetType}
library conreality_headset;

export 'src/headset.dart' show Headset;
export 'src/headset_event.dart' show HeadsetEvent;
export 'src/headset_status.dart' show HeadsetStatus;
export 'src/headset_type.dart' show HeadsetType;

/// The `conreality_headset` plugin.
abstract class ConrealityHeadset {
  ConrealityHeadset._();

  /// The current plugin version string.
  static String get version => "0.1.2";
}
