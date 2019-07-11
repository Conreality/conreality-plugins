conreality_pulse
================

[![Project license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](https://unlicense.org)
[![Pub package](https://img.shields.io/pub/v/conreality_pulse.svg)](https://pub.dev/packages/conreality_pulse)
[![Dartdoc reference](https://img.shields.io/badge/dartdoc-reference-blue.svg)](https://pub.dev/documentation/conreality_pulse/latest/)

Player heart-rate monitoring support for Conreality live-action games.

Compatibility
-------------

Android (5.0+). (iOS support is planned.)

Features
--------

### Supported devices

- [Polar H10](https://www.polar.com/en/products/accessories/H10_heart_rate_sensor)
  heart-rate monitor chest strap

- [Polar OH1](https://www.polar.com/en/products/accessories/oh1-optical-heart-rate-sensor)
  heart-rate sensor armband

Examples
--------

### Importing the package

```dart
import 'package:conreality_pulse/conreality_pulse.dart';
```

### Subscribing to heart-rate updates

```dart
Stream<PulseEvent> stream = await Pulse.subscribe();

stream.listen((PulseEvent event) {
  print("Your current heart rate is: ${event.value}");
});
```

Installation
------------

### `pubspec.yaml` using Pub

    dependencies:
      # See: https://pub.dev/packages/conreality_pulse
      conreality_pulse: ^0.1.0

### `pubspec.yaml` using Git

    dependencies:
      # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_pulse
      conreality_pulse:
        git:
          url: https://github.com/conreality/conreality-plugins.git
          path: conreality_pulse

See Also
--------

Follow [@ConrealityCode](https://twitter.com/ConrealityCode) and
[@ConrealityGame](https://twitter.com/ConrealityGame) on Twitter for
project updates.
