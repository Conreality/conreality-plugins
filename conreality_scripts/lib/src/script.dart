/* This is free and unencumbered software released into the public domain. */

import 'dart:typed_data' show ByteData;

import 'package:flutter/services.dart' show rootBundle;

/// A bundled script asset.
class Script {
  const Script(this.path, {this.package = "conreality_scripts"});

  /// The asset path to the script, without a file extension.
  final String path;

  /// The Dart package that bundles the script.
  final String package;

  /// The asset bundle key for the script's `.luac` bytecode file.
  String get binaryKey {
    return (package != null)
        ? "packages/$package/scripts/$path.luac"
        : "scripts/$path.luac";
  }

  /// The asset bundle key for the script's `.lua` source file.
  String get sourceKey {
    return (package != null)
        ? "packages/$package/scripts/$path.lua"
        : "scripts/$path.lua";
  }

  /// Loads the binary data from the script's `.luac` bytecode file.
  Future<ByteData> loadBinary() {
    return rootBundle.load(binaryKey);
  }

  /// Loads the source code from the script's `.lua` source file.
  Future<String> loadSource() {
    return rootBundle.loadString(sourceKey);
  }
}
