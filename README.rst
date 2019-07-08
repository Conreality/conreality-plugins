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

https://wiki.conreality.org/Plugins

----

Overview
========

.. list-table::
   :widths: 20 60 20
   :header-rows: 1

   * - Widget
     - Summary
     - Status

   * - `conreality_headset <#conreality_headset>`__
     - Headset audio and text-to-speech support.
     - Development

   * - `conreality_pulse <#conreality_pulse>`__
     - Player heart-rate monitoring support.
     - Development

   * - `conreality_scripts <#conreality_scripts>`__
     - Precompiled scripts repository.
     - Stable

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
