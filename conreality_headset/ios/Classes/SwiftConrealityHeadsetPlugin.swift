/* This is free and unencumbered software released into the public domain. */

import Flutter
import UIKit
import AVFoundation

public class SwiftConrealityHeadsetPlugin: NSObject, FlutterPlugin {
	public static var channel:FlutterMethodChannel?
	public static  var eventChannel:FlutterEventChannel?
	public static var event: FlutterEventSink?
	var headphonesConnected: Bool = false
	var bluetoothHeadphonesConnected: Bool = false
	var wiredHeadphonesConnected: Bool = false
	var hasMicroPhone: Bool = false
	var hasInbuiltMicroPhone: Bool = false
	var hasHeadsetMicroPhone: Bool = false
	
  public static func register(with registrar: FlutterPluginRegistrar) {
    channel = FlutterMethodChannel(name: "app.conreality.plugins.headset", binaryMessenger: registrar.messenger())
	eventChannel = FlutterEventChannel(name: "app.conreality.plugins.headset/status", binaryMessenger: registrar.messenger())
    let instance = SwiftConrealityHeadsetPlugin()
	registrar.addMethodCallDelegate(instance, channel: channel!)
	eventChannel?.setStreamHandler(HeadsetStreamHandler())
	let sessionInstance = AVAudioSession.sharedInstance()
	NotificationCenter.default.addObserver(self, selector: #selector(SwiftConrealityHeadsetPlugin.audioRouteChangeListener(_:)), name: AVAudioSession.routeChangeNotification, object: sessionInstance)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
	print("method called: \(call.method)")
	switch call.method {
		case "isConnected":
			result(headphonesConnected)
		default:
			result(nil)
	}
  }
	
	@objc dynamic private  func audioRouteChangeListener(_ notification:Notification) {
		print("called")
		guard let userInfo = notification.userInfo,
			  let reasonValue = userInfo[AVAudioSessionRouteChangeReasonKey] as? UInt,
			  let reason:AVAudioSession.RouteChangeReason  = AVAudioSession.RouteChangeReason(rawValue:reasonValue) else {
			return
		}
		updateData(reason: reason, userInfo:userInfo )
	}
	
	func updateData(reason: AVAudioSession.RouteChangeReason, userInfo: [AnyHashable:Any]) {
		switch reason {
			case .newDeviceAvailable:
				let session = AVAudioSession.sharedInstance()
				for output in session.currentRoute.outputs where output.portType == AVAudioSession.Port.headphones {
					headphonesConnected = true
					wiredHeadphonesConnected = true
					print("headphone plugged in")
					break
				}
				for output in session.currentRoute.outputs where output.portType == AVAudioSession.Port.bluetoothA2DP {
					headphonesConnected = true
					bluetoothHeadphonesConnected = true
					print("bluetooth headphone plugged in")
					break
				}
				for output in session.currentRoute.outputs where output.portType == AVAudioSession.Port.builtInMic {
					hasMicroPhone = true
					hasInbuiltMicroPhone = true
					print("bluetooth headphone plugged in")
					break
				}
				for output in session.currentRoute.outputs where output.portType == AVAudioSession.Port.headsetMic {
					hasMicroPhone = true
					hasHeadsetMicroPhone = true
					print("bluetooth headphone plugged in")
					break
				}
				sendStatus()
			case .oldDeviceUnavailable:
				if let previousRoute =
					userInfo[AVAudioSessionRouteChangePreviousRouteKey] as? AVAudioSessionRouteDescription {
					for output in previousRoute.outputs where output.portType == AVAudioSession.Port.headphones {
						headphonesConnected = false
						wiredHeadphonesConnected = false
						print("headphone pulled out")
						break
					}
					for output in previousRoute.outputs where output.portType == AVAudioSession.Port.bluetoothA2DP {
						headphonesConnected = false
						bluetoothHeadphonesConnected = false
						print("bluetooth headphone pulled out")
						break
					}
					for output in previousRoute.outputs where output.portType == AVAudioSession.Port.builtInMic {
						hasMicroPhone = false
						hasInbuiltMicroPhone = false
						print("bluetooth headphone pulled out")
						break
					}
					for output in previousRoute.outputs where output.portType == AVAudioSession.Port.headsetMic {
						hasMicroPhone = false
						hasHeadsetMicroPhone = false
						print("bluetooth headphone pulled out")
						break
					}
				}
				sendStatus()
			default: ()
		}
	}
	
	func sendStatus() {
		let json:String = "{\"isConnected\":\(headphonesConnected),\"hasWiredHeadset\":\(wiredHeadphonesConnected),\"hasWirelessHeadset\":\(headphonesConnected),\"hasMicrophone\":\(hasMicroPhone),\"hasInbuiltMicroPhone\":\(hasInbuiltMicroPhone),\"hasHeadsetMicroPhone\":\(hasHeadsetMicroPhone)}"
		print(json)
		SwiftConrealityHeadsetPlugin.event!(json)
	}
}


class HeadsetStreamHandler: NSObject, FlutterStreamHandler {
	public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
		SwiftConrealityHeadsetPlugin.event = events
		
		return nil
	}
	
	public func onCancel(withArguments arguments: Any?) -> FlutterError? {
		return nil
	}
}
