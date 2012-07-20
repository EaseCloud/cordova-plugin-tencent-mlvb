#import "TencentMLVB.h"
#import "TXRTMPSDK/TXLivePush.h"
#import "TXRTMPSDK/TXLivePlayer.h"

@implementation TencentMLVB

- (void)greet:(CDVInvokedUrlCommand*)command {
    NSString* name = [[command arguments] objectAtIndex:0];
    NSString* msg = [NSString stringWithFormat: @"Hello, %@", name];
    CDVPluginResult* result = [CDVPluginResult
                               resultWithStatus:CDVCommandStatus_OK
                               messageAsString:msg];
    [self alert:msg];
    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
}

- (void)getVersion:(CDVInvokedUrlCommand*)command {
    NSString* version = [[TXLivePush getSDKVersion] componentsJoinedByString:@"."];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:version];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)demoPush:(CDVInvokedUrlCommand*)command {
    NSString* pushUrl = [command.arguments objectAtIndex:0];
    //self.viewController

    UIViewController* vc = [[UIViewController alloc] init];
    vc.view.backgroundColor = [UIColor redColor];
    vc.view.frame = [UIScreen mainScreen].bounds;

    UINavigationController* naVC = [[UINavigationController alloc]initWithRootViewController:vc];
    [self.viewController presentViewController:naVC animated:YES completion:nil];
}

- (void)startPush:(CDVInvokedUrlCommand*)command {
    NSString* url = [command.arguments objectAtIndex:0];

    UIView* videoView = [[UIView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]];
    [self.webView.superview addSubview:videoView];
    [self.webView.superview bringSubviewToFront:self.webView];

    [self.webView setBackgroundColor:[UIColor clearColor]];
    [self.webView setOpaque:NO];

    TXLivePushConfig* _config = [[TXLivePushConfig alloc] init];
    TXLivePush* _txLivePush = [[TXLivePush alloc] initWithConfig: _config];

    [_txLivePush startPreview:videoView];
    [_txLivePush startPush:url];
}

- (void)startPlay:(CDVInvokedUrlCommand*)command {
    NSString* url = [command.arguments objectAtIndex:0];

    UIView* videoView = [[UIView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]];

    [self.webView setBackgroundColor:[UIColor clearColor]];
    [self.webView setOpaque:NO];

    [self.webView.superview addSubview:videoView];
    [self.webView.superview bringSubviewToFront:self.webView];

    //TXLivePushConfig* _config = [[TXLivePushConfig alloc] init];
    //TXLivePush* _txLivePush = [[TXLivePush alloc] initWithConfig: _config];
    TXLivePlayer* _txLivePlayer = [[TXLivePlayer alloc] init];
    [_txLivePlayer setupVideoWidget:CGRectMake(0, 0, 0, 0) containView:videoView insertIndex:0];
    [_txLivePlayer startPlay:url type:PLAY_TYPE_LIVE_FLV];
}

- (void)alert:(NSString*)message title:(NSString*)title {
    UIAlertView* alert = [
        [UIAlertView alloc]
        initWithTitle:title
        message:message
        delegate:nil
        cancelButtonTitle:@"OK"
        otherButtonTitles:nil
    ];
    [alert show];
    //[alert release];
}

- (void)alert:(NSString*)message {
    [self alert:message title:@"系统消息"];
}

@end
