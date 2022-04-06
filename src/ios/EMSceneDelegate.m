#import <UIKit/UIKit.h>
#import "EMSceneDelegate.h"
#import "MainViewController.h"
#import "MSALPublicClientApplication.h"
#import "MethodSwizzle.h"
#import "UIWindow+UIWindowEM.h"

@implementation EMSceneDelegate

@synthesize window, viewController;

#pragma mark UISceneDelegate implementation

- (void)scene:(UIScene *)scene willConnectToSession:(UISceneSession *)session options:(UISceneConnectionOptions *)connectionOptions{
	self.window = [[UIWindow alloc] initWithWindowScene:(UIWindowScene *)scene];
    self.window.autoresizesSubviews = YES;
	MethodSwizzle([UIWindow class], @selector(initWithFrame:), @selector(initWithFrameEM:));

    // only set if not already set in subclass
	self.viewController = [[MainViewController alloc] init];

    // Set your app's start page by setting the <content src='foo.html' /> tag in config.xml.
    // If necessary, uncomment the line below to override it.
    // self.viewController.startPage = @"index.html";

    // NOTE: To customize the view's frame size (which defaults to full screen), override
    // [self.viewController viewWillAppear:] in your view controller.


    self.window.rootViewController = self.viewController;
    [self.window makeKeyAndVisible];
	[[[UIApplication sharedApplication] delegate] setWindow:window];

	[self handleOpenURL:connectionOptions.URLContexts];
}

- (void)scene:(UIScene *)scene openURLContexts:(NSSet<UIOpenURLContext *> *)URLContexts{
	[self handleOpenURL:URLContexts];
}

- (void) handleOpenURL:(NSSet<UIOpenURLContext *> *)URLContexts {
	NSEnumerator *enumerator = [URLContexts objectEnumerator];
	UIOpenURLContext* urlData;
	
	while ((urlData = [enumerator nextObject])) {
		NSURL* url = [urlData URL];
		UISceneOpenURLOptions* options = [urlData options];
		if (!url) {
			continue;
		}

		NSMutableDictionary * openURLData = [[NSMutableDictionary alloc] init];

		[openURLData setValue:url forKey:@"url"];

		if (options.sourceApplication) {
			[openURLData setValue:options.sourceApplication forKey:@"sourceApplication"];
		}

		if (options.annotation) {
			[openURLData setValue:options.annotation forKey:@"annotation"];
		}

		// all plugins will get the notification, and their handlers will be called
		[[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:CDVPluginHandleOpenURLNotification object:url]];
		[[NSNotificationCenter defaultCenter] postNotification:[NSNotification notificationWithName:CDVPluginHandleOpenURLWithAppSourceAndAnnotationNotification object:openURLData]];
		[MSALPublicClientApplication handleMSALResponse:url sourceApplication:options.sourceApplication];
	}
}

@end
