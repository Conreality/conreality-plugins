/* This is free and unencumbered software released into the public domain. */

import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:conreality_pulse/conreality_pulse.dart';

void main() {
  const MethodChannel channel = MethodChannel('app.conreality.plugins.pulse');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await ConrealityPulse.platformVersion, '42');
  });
}
