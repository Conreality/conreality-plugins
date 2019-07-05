/* This is free and unencumbered software released into the public domain. */

/// Precompiled scripts repository for Conreality live-action games.
///
/// {@canonicalFor script.Script}
/// {@canonicalFor scripts.Scripts}
library conreality_scripts;

export 'src/script.dart' show Script;
export 'src/scripts.dart' show Scripts;

/// The `conreality_scripts` plugin.
abstract class ConrealityScripts {
  ConrealityScripts._();

  /// The current plugin version string.
  static String get version => "1.0.1+20190705.1";
}
