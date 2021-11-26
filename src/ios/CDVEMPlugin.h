#import <UIKit/UIKit.h>
#import <Cordova/CDVPlugin.h>

@interface CDVEMPlugin : CDVPlugin
{}

- (void)getDeviceInfo:(CDVInvokedUrlCommand*)command;

@end