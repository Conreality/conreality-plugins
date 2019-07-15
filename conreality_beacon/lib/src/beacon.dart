/* This is free and unencumbered software released into the public domain. */

import 'package:flutter/material.dart' show Color, Colors;

import 'beacon_protocol.dart' show BeaconProtocol;

/// Represents a beacon.
class Beacon {
  const Beacon(Map<String, Object> data) : _data = data;

  final Map<String, Object> _data;

  /// The protocol used by the beacon (AltBeacon or iBeacon).
  BeaconProtocol get protocol => BeaconProtocol.values[_data['protocol']];

  /// The primary beacon identifier (usually a UUID).
  String get id1 => _data['id1'];

  /// The secondary beacon identifier (usually numeric).
  String get id2 => _data['id2'];

  /// The tertiary beacon identifier (usually numeric).
  String get id3 => _data['id3'];

  /// The beacon's reference RSSI (in dBm) at a 1-meter distance.
  double get referenceRSSI => _data['referenceRSSI'];

  /// The beacon's measured RSSI (in dBm).
  double get measuredRSSI => _data['measuredRSSI'];

  /// The beacon's estimated distance (in meters).
  double get estimatedDistance => _data['estimatedDistance'];

  Color get signalColor {
    final rssi = measuredRSSI;
    if (rssi >= -30) return Colors.green;
    if (rssi >= -50) return Colors.lightGreen;
    if (rssi >= -60) return Colors.yellow;
    if (rssi >= -65) return Colors.orangeAccent;
    if (rssi >= -70) return Colors.orange;
    if (rssi >= -80) return Colors.deepOrange;
    if (rssi >= -90) return Colors.red;
  }

  /// Returns a string representation of this beacon.
  @override
  String toString() => 'Beacon(protocol: $protocol, id1: $id1, id2: $id2, id3: $id3, referenceRSSI: $referenceRSSI, measuredRSSI: $measuredRSSI, estimatedDistance: $estimatedDistance)';
}
