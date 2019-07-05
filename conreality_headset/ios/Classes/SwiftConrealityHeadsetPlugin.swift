/* This is free and unencumbered software released into the public domain. */

import Flutter
import UIKit

public class SwiftConrealityHeadsetPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "app.conreality.plugins.headset", binaryMessenger: registrar.messenger())
    let instance = SwiftConrealityHeadsetPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
