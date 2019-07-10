/* This is free and unencumbered software released into the public domain. */

import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:conreality_headset/conreality_headset.dart';

void main() {
  const MethodChannel channel = MethodChannel('app.conreality.plugins.headset');

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    //expect(await ConrealityHeadset.platformVersion, '42');
  });
}
