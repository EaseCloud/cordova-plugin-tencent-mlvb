#import <Cordova/CDV.h>

@interface TencentMLVB : CDVPlugin

- (void) greet:(CDVInvokedUrlCommand*)command;

- (void) getVersion:(CDVInvokedUrlCommand*)command;

- (void) demoPush:(CDVInvokedUrlCommand*)command;

- (void) startPush:(CDVInvokedUrlCommand*)command;

- (void) alert:(NSString*)message title:(NSString*)title;

- (void) alert:(NSString*)message;

@end