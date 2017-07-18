#import "TencentMLVB.h"

@implementation TencentMLVB

@synthesize videoView;
@synthesize livePusher;
@synthesize livePlayer;

//- (void) greet:(CDVInvokedUrlCommand*)command {
//    NSString* name = [[command arguments] objectAtIndex:0];
//    NSString* msg = [NSString stringWithFormat: @"Hello, %@", name];
//    CDVPluginResult* result = [CDVPluginResult
//                               resultWithStatus:CDVCommandStatus_OK
//                               messageAsString:msg];
//    [self alert:msg];
//    [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
//}

- (void) prepareVideoView {
    self.videoView = [[UIView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]];
    [self.webView.superview addSubview:self.videoView];
    [self.webView.superview bringSubviewToFront:self.webView];
    [self.webView setBackgroundColor:[UIColor clearColor]];
    [self.webView setOpaque:NO];
}

- (void) destroyVideoView {
    if (!self.videoView) return;
    [self.videoView removeFromSuperview];
    self.videoView = nil;
    // 把 webView 变回白色
    [self.webView setBackgroundColor:[UIColor whiteColor]];
}

- (void) getVersion:(CDVInvokedUrlCommand*)command {
    NSString* version = [[TXLivePush getSDKVersion] componentsJoinedByString:@"."];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:version];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void) startPush:(CDVInvokedUrlCommand*)command {
    NSString* url = [command.arguments objectAtIndex:0];
    dispatch_async(dispatch_get_main_queue(), ^{
        [self prepareVideoView];
        TXLivePushConfig* _config = [[TXLivePushConfig alloc] init];
        self.livePusher = [[TXLivePush alloc] initWithConfig: _config];
        [self.livePusher startPreview:videoView];
        [self.livePusher startPush:url];
    });
}

- (void) stopPush:(CDVInvokedUrlCommand*)command {
}

- (void) startPlay:(CDVInvokedUrlCommand*)command {
    NSString* url = [command.arguments objectAtIndex:0];
    id playUrlType = [command.arguments objectAtIndex:0];

    UIView* videoView = [[UIView alloc] initWithFrame:[[UIScreen mainScreen] applicationFrame]];

    [self.webView setBackgroundColor:[UIColor clearColor]];
    [self.webView setOpaque:NO];

    [self.webView.superview addSubview:videoView];
    [self.webView.superview bringSubviewToFront:self.webView];

    //TXLivePushConfig* _config = [[TXLivePushConfig alloc] init];
    //TXLivePush* _txLivePush = [[TXLivePush alloc] initWithConfig: _config];
    TXLivePlayer* _txLivePlayer = [[TXLivePlayer alloc] init];
    [_txLivePlayer setupVideoWidget:CGRectMake(0, 0, 0, 0) containView:videoView insertIndex:0];
    [_txLivePlayer startPlay:url type:playUrlType];
}

- (void) stopPlay:(CDVInvokedUrlCommand*)command {
}

- (void) setVideoQuality:(CDVInvokedUrlCommand*)command {
}

- (void) setBeautyFilterDepth:(CDVInvokedUrlCommand*)command {
}

- (void) setWhiteningFilterDepth:(CDVInvokedUrlCommand*)command {
}

- (void) setFilter:(CDVInvokedUrlCommand*)command {
}

- (void) switchCamera:(CDVInvokedUrlCommand*)command {
}

- (void) toggleTorch:(CDVInvokedUrlCommand*)command {
}

- (void) setFocusPosition:(CDVInvokedUrlCommand*)command {
}

- (void) setWaterMark:(CDVInvokedUrlCommand*)command {
}

- (void) setPauseImage:(CDVInvokedUrlCommand*)command {
}

- (void) resize:(CDVInvokedUrlCommand*)command {
}

- (void) pause:(CDVInvokedUrlCommand*)command {
}

- (void) resume:(CDVInvokedUrlCommand*)command {
}

- (void) setRenderMode:(CDVInvokedUrlCommand*)command {
}

- (void) setRenderRotation:(CDVInvokedUrlCommand*)command {
}

- (void) seek:(CDVInvokedUrlCommand*)command {
}

- (void) enableHWAcceleration:(CDVInvokedUrlCommand*)command {
}

- (void) startRecord:(CDVInvokedUrlCommand*)command {
}

- (void) stopRecord:(CDVInvokedUrlCommand*)command {
}

- (void) alert:(NSString*)message title:(NSString*)title {
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

- (void)  alert:(NSString*)message {
    [self alert:message title:@"系统消息"];
}

@end
