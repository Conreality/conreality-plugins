conreality_scripts
==================

[![Project license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](https://unlicense.org)
[![Pub package](https://img.shields.io/pub/v/conreality_scripts.svg)](https://pub.dev/packages/conreality_scripts)
[![Dartdoc reference](https://img.shields.io/badge/dartdoc-reference-blue.svg)](https://pub.dev/documentation/conreality_scripts/latest/)

Precompiled scripts repository for Conreality live-action games.

Compatibility
-------------

Android and iOS both.

Examples
--------

### Importing the package

```dart
import 'package:conreality_scripts/conreality_scripts.dart';
```

### Loading script bytecode

```dart
final bytecode = await Scripts.keepStill.loadBinary();
```

### Loading script source code

```dart
final source = await Scripts.keepStill.loadSource();
```

Installation
------------

### `pubspec.yaml` using Pub

    dependencies:
      # See: https://pub.dev/packages/conreality_scripts
      conreality_scripts: ^1.0.1

### `pubspec.yaml` using Git

    dependencies:
      # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_scripts
      conreality_scripts:
        git:
          url: https://github.com/conreality/conreality-plugins.git
          path: conreality_scripts

See Also
--------

Follow [@ConrealityCode](https://twitter.com/ConrealityCode) and
[@ConrealityGame](https://twitter.com/ConrealityGame) on Twitter for
project updates.
