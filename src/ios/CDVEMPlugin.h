#import <UIKit/UIKit.h>
#import <Cordova/CDVPlugin.h>

@interface CDVEMPlugin : CDVPlugin
{}
@property CDVInvokedUrlCommand* callbackCommand;

- (void)getDeviceInfo:(CDVInvokedUrlCommand*)command;
- (void)saveImageToGallery:(CDVInvokedUrlCommand*)command;
- (void)image:(UIImage *)image didFinishSavingWithError:(NSError *)error contextInfo: (void *) contextInfo;


@end
