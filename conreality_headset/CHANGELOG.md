Changelog
---------

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## 0.2.0 - 2019-07-15
### Changed
- Required Flutter 1.7.8+ and Dart 2.4.0+
### Added
- AndroidX `@NonNull`/`@Nullable`/etc annotations
- [AndroidX lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) support

## 0.1.4 - 2019-07-12
### Added
- Service API for speech synthesis (on Android)

## 0.1.3 - 2019-07-12
### Changed
- Moved the speech synthesis to a bound service (on Android)

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
