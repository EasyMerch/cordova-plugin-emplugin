#import "GMImagePickerController+EMGMImagePickerController.h"
#import "GMPresentationDelegate.h"

@implementation GMImagePickerController(GMImagePickerControllerEM)

	- (id)init_new:(bool)allow_v{
		[self init_old:allow_v];
		self.presentationDelegate = [GMPresentationDelegate alloc];
	}

@end
