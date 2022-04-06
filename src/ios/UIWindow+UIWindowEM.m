#import "UIWindow+UIWindowEM.h"
#import "EMSceneDelegate.h"

@implementation UIWindow(UIWindowEM)

	- (instancetype) initWithFrameEM:(CGRect)frame{
		NSLog(@"DEBUG UIWindowEM initWithFrameEM");
		EMSceneDelegate* sceneDelegate = ((EMSceneDelegate*)[[[[UIApplication sharedApplication] connectedScenes] anyObject] delegate]);
		return [self initWithWindowScene:sceneDelegate.mainScene];
	}

@end
