#import "GMImagePickerController+EMGMImagePickerController.h"
#import "GMPresentationDelegate.h"

@implementation GMImagePickerController(GMImagePickerControllerEM)

	- (id)init_new:(bool)allow_v{
		self = [self init_old:allow_v];
		GMPresentationDelegate * delegate = [GMPresentationDelegate alloc];
		delegate.picker = self;

		self.presentationController.delegate = delegate;
		return self;
	}

@end
