#import "EMSceneDelegate.h"

@implementation SceneDelegate

#pragma mark UISceneDelegate implementation

- (void)scene:(UIScene *)scene willConnectToSession:(UISceneSession *)session options:(UISceneConnectionOptions *)connectionOptions{
	NSLog(@"DEBUG willConnectToSession %@", connectionOptions);
}

- (void)scene:(UIScene *)scene openURLContexts:(NSSet<UIOpenURLContext *> *)URLContexts{
	NSLog(@"DEBUG willConnectToSession %@", URLContexts);
}

@end
