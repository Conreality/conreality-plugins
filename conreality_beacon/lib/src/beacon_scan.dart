/* This is free and unencumbered software released into the public domain. */

import 'beacon.dart' show Beacon;
import 'beacon_event.dart' show BeaconEvent;

/// Represents a beacon scan event.
class BeaconScan extends BeaconEvent {
  const BeaconScan({this.beacons = const <Beacon>[]});

  /// The beacons currently in range.
  final List<Beacon> beacons;

  bool get isEmpty => beacons.isEmpty;
  bool get isNotEmpty => beacons.isNotEmpty;

  /// Returns a string representation of this object.
  @override
  String toString() => 'BeaconScan(beacons: [' + beacons.join(', ') + '])';
}
