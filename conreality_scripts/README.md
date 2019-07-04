conreality_scripts
==================

[![Project license](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](https://unlicense.org)
[![Pub package](https://img.shields.io/pub/v/conreality_scripts.svg)](https://pub.dartlang.org/packages/conreality_scripts)
[![Dartdoc reference](https://img.shields.io/badge/dartdoc-reference-blue.svg)](https://pub.dartlang.org/documentation/conreality_scripts/latest/)

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
      conreality_scripts: ^1.0.0

### `pubspec.yaml` using Git

    dependencies:
      # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_scripts
      conreality_scripts:
        git:
          url: https://github.com/conreality/conreality-plugins.git
          path: conreality_scripts
