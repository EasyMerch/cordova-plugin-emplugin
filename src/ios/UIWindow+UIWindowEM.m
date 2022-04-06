#import "UIWindow+UIWindowEM.h"
#import "EMSceneDelegate.h"

@implementation UIWindow(UIWindowEM)

	- (instancetype) initWithFrameEM:(CGRect)frame{
		EMSceneDelegate* sceneDelegate = ((EMSceneDelegate*)[[[[UIApplication sharedApplication] connectedScenes] anyObject] delegate]);
		return [self initWithWindowScene:sceneDelegate.mainScene];
	}

@end
