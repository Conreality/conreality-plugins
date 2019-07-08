/* This is free and unencumbered software released into the public domain. */

import Flutter
import UIKit

public class SwiftConrealityPulsePlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "app.conreality.plugins.pulse", binaryMessenger: registrar.messenger())
    let instance = SwiftConrealityPulsePlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
