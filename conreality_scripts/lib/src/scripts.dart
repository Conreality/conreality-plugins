/* This is free and unencumbered software released into the public domain. */

import 'script.dart' show Script;

/// The directory of bundled script assets.
abstract class Scripts {
  Scripts._();

  /// The [`developer/keep_still.lua`](https://github.com/conreality/conreality-scripts/blob/master/developer/keep_still.lua) script.
  static Script get keepStill => Script("developer/keep_still");

  /// All the scripts, indexed by their path.
  static const Map<String, Script> all = <String, Script>{
    "developer/keep_still": Script("developer/keep_still"),
  };
}
