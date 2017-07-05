Cordova Tencent MLVB Plugin
===========================

腾讯云《移动直播》 Cordova 插件
-------------------------------

本插件是腾讯云《移动直播》SDK 的 Cordova 封装。

腾讯云官方文档：<https://cloud.tencent.com/document/product/454>

### 主要问题答疑

**腾讯云移动直播的主要工作原理是怎样的？**

主要是实现了 rtmp 协议，通过手机 APP（主要是 iOS，Android）在手机平台上实现推流和播放的接口，
同时绑定了摄像头等硬件可以直接在界面上展示直播内容。

**这个插件通过什么方式集成到 Cordova 应用中呢？**

这个插件可以通过 js 调用开启直播推流或者播放视频流，展示的界面元素会叠放在 CordovaWebView 的后面，
同时能够自动将 webView 设置成透明。webView 实现直播交互的功能组件即可。

**支持的 rmtp 流 url 格式是怎样的？**
    
注册开通腾讯云的视频直播应用后，访问<https://console.cloud.tencent.com/live/livecodemanage>，
可以看到具体生成推流地址的逻辑。

推流地址格式：

```
rtmp://1234.livepush.myqcloud.com/live/1234_Room47?bizid=1234&txSecret=XXXXXX&txTime=XXXXXXXX
```

其中 `bizid=1234` 四位数字是创建的应用编号，`Room47` 是房间号，可以根据需要自己生成。

`txSecret` 是经过验签的推流秘钥串，看文档用简单的 md5 即可生成。`txTime` 是加密的时间串。

播放地址格式（支持 RTMP/FLV/HLS 三种协议）：

```
rtmp://1234.liveplay.myqcloud.com/live/1234_Room47
http://1234.liveplay.myqcloud.com/live/1234_Room47.flv
http://1234.liveplay.myqcloud.com/live/1234_Room47.m3u8
```

### 安装插件

##### 方法一：直接命令行安装插件

```
cordova plugin add --save https://github.com/easecloud/cordova-plugin-tencent-mlvb.git
```

##### 方法二：手动添加 `config.xml` 配置

```
<plugin name="cordova-plugin-tencent-mlvb" 
        spec="https://github.com/easecloud/cordova-plugin-tencent-mlvb.git" />
```

然后通过 `cordova prepare` 即可安装。
    
##### 方法三：手动下载项目源码整个目录，放在 cordova 项目的 plugins 目录下

API
---

使用 `window.TencentMLVB` 对象调用相关的方法。

#### 开始直播

```
TencentMLVB.startPush(url, successCallback, errorCallback);
```

> 注意，为了使得背后的视频 UI 层能够显露出来，请务必把 HTML body 的背景设置为透明。

> Android: 完成, iOS: 完成

#### 结束直播

```
TencentMLVB.stopPush();
```

> Android: 完成, iOS: 开发中

#### 开始观看

```
TencentMLVB.startPush(url, playUrlType, successCallback, errorCallback);
```

需要手动指定拉流的类型，`TencentMLVB.PLAY_URL_TYPE` 列举了相关的枚举值：

```
    PLAY_URL_TYPE: {
        PLAY_TYPE_LIVE_RTMP:     0, // 传入的URL为RTMP直播地址
        PLAY_TYPE_LIVE_FLV:      1, // 传入的URL为FLV直播地址
        PLAY_TYPE_VOD_FLV:       2, // 传入的URL为RTMP点播地址
        PLAY_TYPE_VOD_HLS:       3, // 传入的URL为HLS(m3u8)点播地址
        PLAY_TYPE_VOD_MP4:       4, // 传入的URL为MP4点播地址
        PLAY_TYPE_LIVE_RTMP_ACC: 5, // 低延迟连麦链路直播地址（仅适合于连麦场景）
        PLAY_TYPE_LOCAL_VIDEO:   6  // 手机本地视频文件
    },
```

例如：

```
TencentMLVB.startPlay(
    'http://1234.liveplay.myqcloud.com/live/1234_Room47.flv',
    TencentMLVB.PLAY_URL_TYPE.PLAY_TYPE_LIVE_FLV,
    function (msgSuccess) {
        // ...
    },
    function (msgError) {
        // ...
    },
)
```

#### 结束观看
    
```
TencentMLVB.stopPlay();
```

> Android: 完成, iOS: 开发中

#### \[开发中] 推流事件处理 onPushEvent

#### \[开发中] 播放事件处理 onPlayEvent

#### \[开发中] 设定清晰度 setVideoQuality

#### \[计划V2] 设定美颜级别 setBeautyFilterDepth

#### \[计划V2] 设定美白级别 setWhiteningFilterDepth

#### \[计划V3] 设定滤镜 setFilter

#### \[开发中] 切换镜头 switchCamera

#### \[计划V3] 开关闪光灯 toggleTorch

#### \[计划V4] 设置手动对焦位置 setFocusPosition

#### \[计划V4] 设置视频水印 setWaterMark

#### \[计划V2] 设置暂停时后台推流图片 setPaushImage

#### \[计划V2] 修改推流屏幕的位置大小 resize

#### \[开发中] 暂停播放 pause

#### \[开发中] 继续播放 resume

#### \[计划V2] 窗口适配 setRenderMode

#### \[计划V2] 旋转屏幕 setRenderRotation

#### \[计划V2] 修改播放进度 seek

#### \[计划V2] 开关硬件加速 enableHWAcceleration

#### \[计划V2] 开始录音 startRecord

#### \[计划V2] 结束录音 stopRecord

### 更多

如何创建 Cordova 项目请看[文档](http://cordova.apache.org/docs/en/latest/guide/cli/index.html)

Cordova 插件相关信息请看[文档](http://cordova.apache.org/docs/en/latest/guide/hybrid/plugins/index.html)
