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

  /// The script name or label.
  ///
  /// Note: parsing this metadata requires loading the source file.
  Future<String> get label => _parseMetadata("label");

  /// A short summary of what the script does.
  ///
  /// Note: parsing this metadata requires loading the source file.
  Future<String> get summary => _parseMetadata("summary");

  /// The author of the script.
  ///
  /// Note: parsing this metadata requires loading the source file.
  Future<String> get author => _parseMetadata("author");

  /// Loads the binary data from the script's `.luac` bytecode file.
  Future<ByteData> loadBinary() {
    return rootBundle.load(binaryKey);
  }

  /// Loads the source code from the script's `.lua` source file.
  Future<String> loadSource() {
    return rootBundle.loadString(sourceKey);
  }

  Future<String> _parseMetadata(final String property) async {
    final pattern = RegExp(r'^\s*([^=\s]+)\s*=\s*"([^"]+)"');
    final source = await loadSource();
    for (final line in source.split("\n")) {
      final match = pattern.firstMatch(line);
      if (match != null && match.group(1) == property) {
        return match.group(2);
      }
    }
    return null;
  }
}
