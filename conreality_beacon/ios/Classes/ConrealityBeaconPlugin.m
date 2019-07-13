#import "ConrealityBeaconPlugin.h"
#import <conreality_beacon/conreality_beacon-Swift.h>

@implementation ConrealityBeaconPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftConrealityBeaconPlugin registerWithRegistrar:registrar];
}
@end
