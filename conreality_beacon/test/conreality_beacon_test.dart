/* This is free and unencumbered software released into the public domain. */

import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:conreality_beacon/conreality_beacon.dart';

void main() {
  const MethodChannel channel = MethodChannel('app.conreality.plugins.beacon');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await ConrealityBeacon.platformVersion, '42');
  });
}
