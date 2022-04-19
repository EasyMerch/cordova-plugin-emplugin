#import "GMPresentationDelegate.h"

@implementation GMPresentationDelegate

#pragma mark - UIAdaptivePresentationControllerDelegate

- (void)presentationControllerDidDismiss:(UIPresentationController *)presentationController {
	[self imagePickerControllerDidCancel: nil];
}

@end
