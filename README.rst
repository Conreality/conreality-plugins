******************
Conreality Plugins
******************

.. image:: https://img.shields.io/badge/license-Public%20Domain-blue.svg
   :alt: Project license
   :target: https://unlicense.org

.. image:: https://img.shields.io/travis/conreality/conreality-plugins/master.svg
   :alt: Travis CI build status
   :target: https://travis-ci.org/conreality/conreality-plugins

|

https://conreality.wiki/Plugins

----

Requirements
============

- `Flutter <https://flutter.dev>`__
  `1.7.8+ <https://github.com/flutter/flutter/wiki/Release-Notes-Flutter-1.7.8>`__
  with
  `Dart <https://dart.dev>`__
  `2.4.0+ <https://github.com/dart-lang/sdk/blob/master/CHANGELOG.md#240---2019-06-27>`__

- `Android <https://developer.android.com>`__
  `5.0+ <https://developer.android.com/about/dashboards>`__
  with `AndroidX <https://developer.android.com/jetpack/androidx>`__

- `iOS <https://developer.apple.com/ios/>`__
  `11+ <https://en.wikipedia.org/wiki/IOS_11>`__

----

Overview
========

.. list-table::
   :widths: 20 60 20
   :header-rows: 1

   * - Widget
     - Summary
     - Status

   * - `conreality_beacon <#conreality_beacon>`__
     - Radio beacon gadget support.
     - Development

   * - `conreality_headset <#conreality_headset>`__
     - Headset audio and text-to-speech support.
     - Usable

   * - `conreality_pulse <#conreality_pulse>`__
     - Player heart-rate monitoring support.
     - Usable

   * - `conreality_scripts <#conreality_scripts>`__
     - Precompiled scripts repository.
     - Stable

----

conreality_beacon
=================

::

   dependencies:
     # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_beacon
     conreality_beacon:
       git:
         url: https://github.com/conreality/conreality-plugins.git
         path: conreality_beacon

::

   import 'package:conreality_beacon/conreality_beacon.dart';

----

conreality_headset
==================

::

   dependencies:
     # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_headset
     conreality_headset:
       git:
         url: https://github.com/conreality/conreality-plugins.git
         path: conreality_headset

::

   import 'package:conreality_headset/conreality_headset.dart';

----

conreality_pulse
================

::

   dependencies:
     # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_pulse
     conreality_pulse:
       git:
         url: https://github.com/conreality/conreality-plugins.git
         path: conreality_pulse

::

   import 'package:conreality_pulse/conreality_pulse.dart';

----

conreality_scripts
==================

::

   dependencies:
     # See: https://github.com/conreality/conreality-plugins/tree/master/conreality_scripts
     conreality_scripts:
       git:
         url: https://github.com/conreality/conreality-plugins.git
         path: conreality_scripts

::

   import 'package:conreality_scripts/conreality_scripts.dart';

----

See Also
========

- `Conreality Widgets
  <https://github.com/conreality/conreality-widgets>`__

- `Conreality Developer App
  <https://github.com/conreality/conreality-developer>`__

Follow `@ConrealityCode <https://twitter.com/ConrealityCode>`__ and
`@ConrealityGame <https://twitter.com/ConrealityGame>`__ on Twitter for
project updates.
