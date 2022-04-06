#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "MainViewController.h"

@interface EMSceneDelegate : UIResponder <UIWindowSceneDelegate>{}

@property (nonatomic, strong) IBOutlet UIWindow* window;
@property (nonatomic, strong) IBOutlet MainViewController* viewController;
@property (nonatomic, strong) IBOutlet UIWindowScene * mainScene;

@end
