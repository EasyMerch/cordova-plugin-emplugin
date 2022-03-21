
#include <sys/types.h>
#include <sys/sysctl.h>
#include "TargetConditionals.h"

#import <Cordova/CDV.h>
#import "CDVEMPlugin.h"
#import <Photos/Photos.h>
#import <AssetsLibrary/AssetsLibrary.h>
#import <ImageIO/ImageIO.h>
#import <MobileCoreServices/MobileCoreServices.h>

@implementation CDVEMPlugin{
}

- (void)getDeviceInfo:(CDVInvokedUrlCommand*)command{
    NSDictionary* deviceProperties = @{};
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:deviceProperties];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)saveImageToGallery:(CDVInvokedUrlCommand*)command{
	dispatch_async(dispatch_get_main_queue(), ^{
		__block PHImageRequestOptions * imageRequestOptions = [[PHImageRequestOptions alloc] init];
		
		imageRequestOptions.synchronous = YES;
		NSString* path = command.arguments[0];
		NSDictionary* options = (NSDictionary*)command.arguments[1];
		// NSString* filename = [options objectForKey:@"filename"];
		// NSString* description = [options objectForKey:@"description"];

		UIImage* image;
		if ([[NSFileManager defaultManager] fileExistsAtPath:path]){
			image = [UIImage imageWithData: [[NSFileManager defaultManager] contentsAtPath:path]];
		}else {
			image = [UIImage imageWithData: [NSData dataWithContentsOfURL: [NSURL URLWithString:path]]];
		}

		CGImageRef newCgIm = CGImageCreateCopy(image.CGImage);
		UIImage *newImage = [UIImage imageWithCGImage:newCgIm scale:image.scale orientation:image.imageOrientation];
		CGImageRelease(newCgIm);

		UIImageWriteToSavedPhotosAlbum(newImage, self, @selector(image:didFinishSavingWithError:contextInfo:), (void*) CFBridgingRetain(command));
	});
}

- (void)image:(UIImage*)image didFinishSavingWithError:(NSError*)error contextInfo:(void*)contextInfo{
	CDVInvokedUrlCommand* command = (CDVInvokedUrlCommand*) CFBridgingRelease(contextInfo);

	CDVPluginResult* pluginResult;
	if(error){
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[error localizedDescription]];
	} else {
		pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK];
	}

	[self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
