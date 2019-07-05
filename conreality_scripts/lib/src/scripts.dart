/* This is free and unencumbered software released into the public domain. */

import 'script.dart' show Script;

/// The directory of bundled script assets.
abstract class Scripts {
  Scripts._();

  /// The [`developer/announce_hooks.lua`](https://github.com/conreality/conreality-scripts/blob/master/developer/announce_hooks.lua) script.
  static Script get announceHooks => Script("developer/announce_hooks");

  /// The [`developer/announce_time.lua`](https://github.com/conreality/conreality-scripts/blob/master/developer/announce_time.lua) script.
  static Script get announceTime => Script("developer/announce_time");

  /// The [`developer/flux_dosimeter.lua`](https://github.com/conreality/conreality-scripts/blob/master/developer/flux_dosimeter.lua) script.
  static Script get fluxDosimeter => Script("developer/flux_dosimeter");

  /// The [`developer/keep_still.lua`](https://github.com/conreality/conreality-scripts/blob/master/developer/keep_still.lua) script.
  static Script get keepStill => Script("developer/keep_still");

  /// The [`developer/spin_compass.lua`](https://github.com/conreality/conreality-scripts/blob/master/developer/spin_compass.lua) script.
  static Script get spinCompass => Script("developer/spin_compass");

  /// All the scripts, indexed by their path.
  static const Map<String, Script> all = <String, Script>{
    "developer/announce_hooks": Script("developer/announce_hooks"),
    "developer/announce_time": Script("developer/announce_time"),
    "developer/flux_dosimeter": Script("developer/flux_dosimeter"),
    "developer/keep_still": Script("developer/keep_still"),
    "developer/spin_compass": Script("developer/spin_compass"),
  };
}
