/* This is free and unencumbered software released into the public domain. */

/// Represents the beacon protocol (AltBeacon or iBeacon).
enum BeaconProtocol {
  unknown,
  altbeacon,
  ibeacon,
}

/// Returns the string label for a given beacon protocol.
String getBeaconProtocolLabel(final BeaconProtocol protocol) {
  switch (protocol) {
    case BeaconProtocol.unknown: return "Unknown";
    case BeaconProtocol.altbeacon: return "AltBeacon";
    case BeaconProtocol.ibeacon: return "iBeacon";
  }
  return null;
}
