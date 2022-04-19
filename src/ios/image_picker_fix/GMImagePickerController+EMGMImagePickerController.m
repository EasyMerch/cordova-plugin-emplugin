#import "GMImagePickerController+EMGMImagePickerController.h"

@implementation GMImagePickerController(GMImagePickerControllerEM)

	- (id)init_new:(bool)allow_v{
		[self init_old:allow_v];
		self.presentationDelegate = self;
	}

#pragma mark - UIAdaptivePresentationControllerDelegate

- (void)presentationControllerDidDismiss:(UIPresentationController *)presentationController {
	[self imagePickerControllerDidCancel: nil];
}

@end
