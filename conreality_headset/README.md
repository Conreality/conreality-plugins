conreality_headset
==================

[![Project license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](https://unlicense.org)
[![Pub package](https://img.shields.io/pub/v/conreality_headset.svg)](https://pub.dev/packages/conreality_headset)
[![Dartdoc reference](https://img.shields.io/badge/dartdoc-reference-blue.svg)](https://pub.dev/documentation/conreality_headset/latest/)

Headset audio and text-to-speech support for Conreality live-action games.

Compatibility
-------------

Android only, at present. (iOS support is planned.)

Examples
--------

### Importing the package

```dart
import 'package:conreality_headset/conreality_headset.dart';
```

### Subscribing to headset events

```dart
Stream<HeadsetEvent> stream = await Headset.subscribe();

stream.listen((HeadsetEvent event) {
  print(event);
});
```

Installation
------------

### `pubspec.yaml` using Pub

    dependencies:
      # See: https://pub.dev/packages/conreality_headset
      conreality_headset: ^0.1.0

### `pubspec.yaml` using Git

    dependencies:
      # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_headset
      conreality_headset:
        git:
          url: https://github.com/conreality/conreality-plugins.git
          path: conreality_headset
