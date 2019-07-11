Changelog
---------

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 0.1.2 - 2019-07-11
### Changed
- Fixed a bug in `Headset.isConnected`
### Added
- `Headset.canSpeak` static property
- `Headset.speak()` static method
- `Headset.stopSpeaking()` static method

## 0.1.1 - 2019-07-11
### Added
- Bluetooth HSP/HFP headset support (on Android only)

## 0.1.0 - 2019-07-10
### Added
- `Headset.isConnected` static property
- `Headset.subscribe()` static method
- `HeadsetEvent` class
- `HeadsetStatus` class
- `HeadsetType` enum

## 0.0.1 - 2019-07-05
### Added
- `ConrealityHeadset.version` getter
