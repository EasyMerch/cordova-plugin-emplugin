#import "GMImagePickerController+EMGMImagePickerController.h"

@implementation GMImagePickerController(GMImagePickerControllerEM)

	-(void)viewDidDisappear:(bool)animated{
		SOSPicker * delegate = [self delegate];
		[delegate performSelector:@selector(imagePickerControllerDidCancel:) withObject:nil];

		[super viewDidDisappear:animated];
	}

@end
