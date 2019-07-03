#import "ConrealityHeadsetPlugin.h"
#import <conreality_headset/conreality_headset-Swift.h>

@implementation ConrealityHeadsetPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftConrealityHeadsetPlugin registerWithRegistrar:registrar];
}
@end
