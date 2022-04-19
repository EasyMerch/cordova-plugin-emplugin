#import "GMPresentationDelegate.h"
#import "SOSPicker.h"

@implementation GMPresentationDelegate

#pragma mark - UIAdaptivePresentationControllerDelegate

- (void)presentationControllerDidDismiss:(UIPresentationController *)presentationController {
	SOSPicker* delegate = (SOSPicker*)[self.picker delegate];
	// [delegate imagePickerControllerDidCancel:nil];
	[delegate performSelector:@(imagePickerControllerDidCancel) withObject:nil];
}

@end
