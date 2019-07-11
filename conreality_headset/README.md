conreality_headset
==================

[![Project license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](https://unlicense.org)
[![Pub package](https://img.shields.io/pub/v/conreality_headset.svg)](https://pub.dev/packages/conreality_headset)
[![Dartdoc reference](https://img.shields.io/badge/dartdoc-reference-blue.svg)](https://pub.dev/documentation/conreality_headset/latest/)

Headset audio and text-to-speech support for Conreality live-action games.

Compatibility
-------------

Android (5.0+). (iOS support is planned.)

Features
--------

- Determines whether a wired or wireless headset is currently connected.

- Provides headset events including connect/disconnect status notifications.

- Supports speech synthesis (text-to-speech) when a headset is connected.

### Supported Devices

- Bluetooth headsets supporting the
  [Headset Profile (HSP)](https://en.wikipedia.org/wiki/List_of_Bluetooth_profiles#Headset_Profile_(HSP))
  and/or
  [Hands-Free Profile (HFP)](https://en.wikipedia.org/wiki/List_of_Bluetooth_profiles#Hands-Free_Profile_(HFP))

- Wired headphones connected to the
  [headphone jack](https://en.wikipedia.org/wiki/Phone_connector_(audio))

Examples
--------

### Importing the package

```dart
import 'package:conreality_headset/conreality_headset.dart';
```

### Checking for a headset

```dart
var ok = await Headset.isConnected;

print("Headset is " + (ok ? "connected" : "not connected"));
```

### Subscribing to headset events

```dart
Stream<HeadsetEvent> stream = await Headset.subscribe();

stream.listen((HeadsetEvent event) {
  print(event);
});
```

### Speaking into the headset

```dart
await Headset.speak("Hello, world!");
```

Installation
------------

### `pubspec.yaml` using Pub

    dependencies:
      # See: https://pub.dev/packages/conreality_headset
      conreality_headset: ^0.1.1

### `pubspec.yaml` using Git

    dependencies:
      # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_headset
      conreality_headset:
        git:
          url: https://github.com/conreality/conreality-plugins.git
          path: conreality_headset
