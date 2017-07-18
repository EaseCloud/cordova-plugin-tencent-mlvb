#import <Cordova/CDV.h>
#import "TXRTMPSDK/TXLivePush.h"
#import "TXRTMPSDK/TXLivePlayer.h"

@interface TencentMLVB : CDVPlugin

@property UIView* videoView;
@property TXLivePush* livePusher;
@property TXLivePlayer* livePlayer;

- (void) getVersion:(CDVInvokedUrlCommand*)command;
- (void) prepareVideoView;
- (void) destroyVideoView;
- (void) startPush:(CDVInvokedUrlCommand*)command;
- (void) stopPush:(CDVInvokedUrlCommand*)command;
- (void) startPlay:(CDVInvokedUrlCommand*)command;
- (void) stopPlay:(CDVInvokedUrlCommand*)command;
- (void) setVideoQuality:(CDVInvokedUrlCommand*)command;
- (void) setBeautyFilterDepth:(CDVInvokedUrlCommand*)command;
- (void) setFilter:(CDVInvokedUrlCommand*)command;
- (void) switchCamera:(CDVInvokedUrlCommand*)command;
- (void) toggleTorch:(CDVInvokedUrlCommand*)command;
- (void) setFocusPosition:(CDVInvokedUrlCommand*)command;
- (void) setWaterMark:(CDVInvokedUrlCommand*)command;
- (void) setPauseImage:(CDVInvokedUrlCommand*)command;
- (void) pause:(CDVInvokedUrlCommand*)command;
- (void) resume:(CDVInvokedUrlCommand*)command;
- (void) setRenderMode:(CDVInvokedUrlCommand*)command;
- (void) setRenderRotation:(CDVInvokedUrlCommand*)command;
- (void) seek:(CDVInvokedUrlCommand*)command;
- (void) enableHWAcceleration:(CDVInvokedUrlCommand*)command;
- (void) startRecord:(CDVInvokedUrlCommand*)command;
- (void) stopRecord:(CDVInvokedUrlCommand*)command;
- (void) alert:(NSString*)message title:(NSString*)title;
- (void) alert:(NSString*)message;

@end