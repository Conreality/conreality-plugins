/* This is free and unencumbered software released into the public domain. */

import 'dart:async' show Future, Stream;

import 'package:flutter/services.dart' show EventChannel;

import 'beacon.dart' show Beacon;
import 'beacon_scan.dart' show BeaconScan;

const EventChannel _eventChannel = EventChannel('app.conreality.plugins.beacon/scan');

/// Beacon discovery interface.
abstract class Beacons {
  Beacons._();

  /// Scans for nearby beacons.
  static Future<Stream<BeaconScan>> scan() async {
    return _eventChannel.receiveBroadcastStream().map(_parseData);
  }

  static BeaconScan _parseData(dynamic data) {
    final List<Map<dynamic, dynamic>> entries = (data as List<dynamic>).cast<Map<dynamic, dynamic>>();
    return BeaconScan(beacons: entries.map((entry) => Beacon(Map<String, dynamic>.from(entry))).toList());
  }
}
