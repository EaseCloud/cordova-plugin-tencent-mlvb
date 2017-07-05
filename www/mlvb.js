/*global cordova, module*/

module.exports = {

    // 错误处理
    ERROR: {
//        10001: { msg: '开启推流失败：视图 videoView 已存在' },
        10002: { msg: '开启推流失败：pusher 已存在' },
        10003: { msg: '停止推流失败：pusher 不存在' },
    },

    // 获取 SDK 版本号
    getVersion: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "getVersion", []);
    },

    // 推流类方法

    startPush: function(url, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "startPush", [url]);
    },

    stopPush: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "stopPush", []);
    },

    // 推流事件处理
    // https://cloud.tencent.com/document/product/454/7879#.E4.BA.8B.E4.BB.B6.E5.A4.84.E7.90.86
    onPushEvent: function(eventCallback) {
    },

    // 播放类方法

    startPlay: function(url, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "startPlay", [url]);
    },

    stopPlay: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "stopPlay", []);
    },

    // 播放事件处理
    // https://cloud.tencent.com/document/product/454/7880#.E7.8A.B6.E6.80.81.E7.9B.91.E5.90.AC
    onPlayEvent: function(eventCallback) {
    },

    // 调整类方法

    // 推流
    // 设定清晰度
    // https://cloud.tencent.com/document/product/454/7879#step-4.3A-.E8.AE.BE.E5.AE.9A.E6.B8.85.E6.99.B0.E5.BA.A6
    setVideoQuality: function(quality, adjustBitrate, adjustResolution,
                              successCallback, errorCallback) {
        cordova.exec(
            successCallback, errorCallback, "TencentMLVB", "setVideoQuality",
            [quality, adjustBitrate, adjustResolution]
        );
    },

    // 推流
    // 设定美颜级别
    // depth: 0-9，0 为关闭
    setBeautyFilterDepth: function(depth, successCallback, errorCallback) {
        cordova.exec(
            successCallback, errorCallback, "TencentMLVB", "setBeautyFilterDepth",
            [depth]
        );
    },

    // 推流
    // 设定美白级别
    // depth: 0-9，0 为关闭
    setWhiteningFilterDepth: function(depth, successCallback, errorCallback) {
        cordova.exec(
            successCallback, errorCallback, "TencentMLVB", "setWhiteningFilterDepth",
            [depth]
        );
    },

    // 推流
    // 设定滤镜
    setFilter: function(filterUrl, successCallback, errorCallback) {
        cordova.exec(
            successCallback, errorCallback, "TencentMLVB", "setFilter",
            [filterUrl]
        );
    },

    // 推流
    // 切换前后置镜头，调用一次切换一次
    // callback 返回调用之后的镜头是 front 还是 back
    switchCamera: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "switchCamera", []);
    },

    // 推流
    // 开启/关闭闪光灯
    toggleTorch: function(enabled, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "toggleTorch", [enabled]);
    },

    // 推流
    // 设置手动对焦位置（输入百分比坐标值）
    setFocusPosition: function(top, left, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "setFocusPosition", [top, left]);
    },

    // 推流
    // 设置视频水印
    setWaterMark: function(imgUrl, top, left, successCallback, errorCallback) {
        cordova.exec(
            successCallback, errorCallback, "TencentMLVB", "setWaterMark",
            [imgUrl, top, left]
        );
    },

    // 推流
    // 设置暂停时的后台推流图片
    setPauseImage: function(imgUrl, successCallback, errorCallback) {
        cordova.exec(
            successCallback, errorCallback, "TencentMLVB", "setPauseImage",
            [imgUrl]
        );
    },



    // 推流 + 播放
    // 调整播放或者推流屏幕的大小，支持 vw, vh 两种单位，表示 1 单位的宽度百分比和 1 单位的高度百分比
    resize: function(top, left, width, height, successCallback, errorCallback) {
        cordova.exec(
            successCallback, errorCallback, "TencentMLVB", "resizePlay",
            [top, left, width, height]
        );
    },

    pause: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "pausePlay", []);
    },

    resume: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "resumePlay", []);
    },

    RENDER_MODE: {
        FILL_SCREEN: 'RENDER_MODE_FILL_SCREEN',  // css cover
        FILL_EDGE: 'RENDER_MODE_FILL_EDGE',  // css contain
    },

    setRenderMode: function(mode, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "setRenderMode", [mode]);
    },

    ROTATION_MODE: {
        LANDSCAPE: 'LANDSCAPE',   // 横屏
        PORTRAIT: 'PORTRAIT',  // 竖屏
    },

    setRenderRotation: function(mode, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "setRenderRotation", [mode]);
    },

    seek: function(progress, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "seekPlay", [progress]);
    },

    // 推流 + 播放
    enableHWAcceleration: function(enabled, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "enableHWAcceleration", [enabled]);
    },

    startRecord: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "startRecord", []);
    },

    stopRecord: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "TencentMLVB", "stopRecord", []);
    },

};
