/* This is free and unencumbered software released into the public domain. */

/// Radio beacon gadget support for Conreality live-action games.
///
/// {@canonicalFor beacon.Beacon}
/// {@canonicalFor beacon_event.BeaconEvent}
/// {@canonicalFor beacon_protocol.BeaconProtocol}
/// {@canonicalFor beacon_scan.BeaconScan}
/// {@canonicalFor beacons.Beacons}
library conreality_beacon;

export 'src/beacon.dart' show Beacon;
export 'src/beacon_event.dart' show BeaconEvent;
export 'src/beacon_protocol.dart' show BeaconProtocol, getBeaconProtocolLabel;
export 'src/beacon_scan.dart' show BeaconScan;
export 'src/beacons.dart' show Beacons;

/// The `conreality_beacon` plugin.
abstract class ConrealityBeacon {
  ConrealityBeacon._();

  /// The current plugin version string.
  static String get version => '0.0.1';
}
