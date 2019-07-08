#import "ConrealityPulsePlugin.h"
#import <conreality_pulse/conreality_pulse-Swift.h>

@implementation ConrealityPulsePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftConrealityPulsePlugin registerWithRegistrar:registrar];
}
@end
