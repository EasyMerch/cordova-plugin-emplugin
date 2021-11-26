
#include <sys/types.h>
#include <sys/sysctl.h>
#include "TargetConditionals.h"

#import <Cordova/CDV.h>
#import "CDVEMPlugin.h"

@interface CDVEMPlugin () {}
@end

@implementation CDVEMPlugin

- (void)getDeviceInfo:(CDVInvokedUrlCommand*)command
{
    NSDictionary* deviceProperties = @{};
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:deviceProperties];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
