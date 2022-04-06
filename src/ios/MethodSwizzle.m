#import <objc/runtime.h> 
#import <objc/message.h>

void MethodSwizzle(Class c, SEL orig_sel, SEL new_sel){
	Method origMethod = class_getInstanceMethod(c, orig_sel);
	Method newMethod = class_getInstanceMethod(c, new_sel);
	if(class_addMethod(c, orig_sel, method_getImplementation(newMethod), method_getTypeEncoding(newMethod)))
		class_replaceMethod(c, new_sel, method_getImplementation(origMethod), method_getTypeEncoding(origMethod));
	else
	method_exchangeImplementations(origMethod, newMethod);
}