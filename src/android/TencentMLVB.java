package cn.easecloud.cordova.tencent;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.webkit.WebView;
import android.widget.FrameLayout;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONTokener;

import com.google.gson.Gson;

//import com.tencent.av.sdk.*;
//import com.tencent.ilivesdk.*;
//import com.tencent.ilivesdk.core.*;
//import com.tencent.livesdk.*;

import com.tencent.rtmp.*;
import com.tencent.rtmp.ui.*;

public class TencentMLVB extends CordovaPlugin {

    private Context context;
    private Activity activity;
    private CordovaInterface cordova;
    private CordovaWebView cordovaWebView;
    private ViewGroup rootView;
    private WebView webView;
    private CallbackContext callbackContext;

    private TXCloudVideoView videoView = null;
    private TXLivePusher mLivePusher = null;
    private TXLivePlayer mLivePlayer = null;

    private String[] permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_LOGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.cordovaWebView = webView;
        this.cordova = cordova;
        this.activity = cordova.getActivity();
        this.context = this.activity.getApplicationContext();
        this.rootView = (ViewGroup) activity.findViewById(android.R.id.content);
        this.webView = (WebView) rootView.getChildAt(0);
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          The action to execute.
     * @param args            JSONArry of arguments for the plugin.
     * @param callbackContext The callback id used when calling back into JavaScript.
     * @return True if the action was valid, false if not.
     */
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

        if (!hasPermisssion()) {
            requestPermissions(0);
        }

        this.callbackContext = callbackContext;

        if (action.equals("getVersion")) {
            return getVersion(callbackContext);
        } else if (action.equals("startPush")) {
            final String url = args.getString(0);
            return startPush(url, callbackContext);
        } else if (action.equals("stopPush")) {
            return stopPush(callbackContext);
        } else if (action.equals("onPushEvent")) {
            alert("尚未实现");
        } else if (action.equals("startPlay")) {
            final String url = args.getString(0);
            final int playType = args.getInt(1);
            return startPlay(url, playType, callbackContext);
        } else if (action.equals("stopPlay")) {
            return stopPlay(callbackContext);
        } else if (action.equals("onPlayEvent")) {
            alert("尚未实现");
        } else if (action.equals("setVideoQuality")) {
            alert("尚未实现");
        } else if (action.equals("setBeautyFilterDepth")) {
            // TODO: 尚未测试
            if (mLivePusher == null) return false;
            final int beautyDepth = args.getInt(0);
            final int whiteningDepth = args.getInt(1);
            mLivePusher.setBeautyFilter(beautyDepth, whiteningDepth);
        } else if (action.equals("setExposureCompensation")) {
            // TODO: 尚未测试
            if (mLivePusher == null) return false;
            final float depth = (float) args.getDouble(0);
            mLivePusher.setExposureCompensation(depth);
        } else if (action.equals("setFilter")) {
            alert("尚未实现");
        } else if (action.equals("switchCamera")) {
            // TODO: 尚未测试
            if (mLivePusher == null) return false;
            mLivePusher.switchCamera();
        } else if (action.equals("toggleTorch")) {
            // TODO: 尚未测试
            if (mLivePusher == null) return false;
            final boolean enabled = args.getBoolean(0);
            mLivePusher.turnOnFlashLight(enabled);
        } else if (action.equals("setFocusPosition")) {
            alert("尚未实现");
        } else if (action.equals("setWaterMark")) {
            alert("尚未实现");
        } else if (action.equals("setPauseImage")) {
            alert("尚未实现");
        } else if (action.equals("resize")) {
            alert("尚未实现");
        } else if (action.equals("pause")) {
            alert("尚未实现");
        } else if (action.equals("resume")) {
            alert("尚未实现");
        } else if (action.equals("setRenderMode")) {
            alert("尚未实现");
        } else if (action.equals("setRenderRotation")) {
            alert("尚未实现");
        } else if (action.equals("enableHWAcceleration")) {
            alert("尚未实现");
        } else if (action.equals("startRecord")) {
//            return startRecord(callbackContext);
        } else if (action.equals("stopRecord")) {
//            return stopRecord(callbackContext);
        }

        callbackContext.error("Undefined action: " + action);
        return false;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String statusCode;
        switch (requestCode) {
            case 990:  // demoPush
                if (resultCode == 1) {
                    statusCode = "success";
                    callbackContext.success(statusCode);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 在当前 Activity 底部 UI 层注册一个 TXCloudVideoView 以供直播渲染
     */
    private void prepareVideoView() {
        if (videoView != null) return;
        // 通过 layout 文件插入 videoView
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        videoView = (TXCloudVideoView) layoutInflater.inflate(_R("layout", "layout_video"), null);
        // 设置 webView 透明
        videoView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT
        ));
        // 插入视图
        rootView.addView(videoView);
        videoView.setVisibility(View.VISIBLE);
        // 设置 webView 透明
        webView.setBackgroundColor(Color.TRANSPARENT);
        // 关闭 webView 的硬件加速（否则不能透明）
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        // 将 webView 提到顶层
        webView.bringToFront();
    }

    /**
     * 销毁 videoView
     */
    private void destroyVideoView() {
        if (videoView == null) return;
        videoView.onDestroy();
        rootView.removeView(videoView);
        videoView = null;
        // 把 webView 变回白色
        webView.setBackgroundColor(Color.WHITE);
    }

    /**
     * 返回 MLVB SDK 版本字符串
     *
     * @param callbackContext
     * @return
     */
    private boolean getVersion(final CallbackContext callbackContext) {
        int[] sdkver = TXLivePusher.getSDKVersion();
        if (sdkver != null && sdkver.length > 0) {
            String ver = "" + sdkver[0];
            for (int i = 1; i < sdkver.length; ++i) {
                ver += "." + sdkver[i];
            }
            callbackContext.success(ver);
            return true;
        }
        callbackContext.error("Cannot get rtmp sdk version.");
        return false;
    }

    /**
     * 开始推流，并且在垫底的 videoView 显示视频
     * 会在当前对象上下文注册一个 TXLivePusher
     *
     * @param url             推流URL
     * @param callbackContext
     * @return
     */
    private boolean startPush(final String url, final CallbackContext callbackContext) {
        if (mLivePusher != null) {
            callbackContext.error("10002");
            return false;
        }
        // 准备 videoView，没有的话生成
        activity.runOnUiThread(new Runnable() {
            public void run() {
                prepareVideoView();
                // 开始推流
                mLivePusher = new TXLivePusher(activity);
                TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
                mLivePusher.setConfig(mLivePushConfig);
                mLivePusher.startPusher(url);
                // 将视频绑定到 videoView
                mLivePusher.startCameraPreview(videoView);
            }
        });
        return true;
    }

    /**
     * 停止推流，并且注销 mLivePusher 对象
     *
     * @param callbackContext
     * @return
     */
    private boolean stopPush(final CallbackContext callbackContext) {
        if (mLivePusher == null) {
            callbackContext.error("10003");
            return false;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                // 停止摄像头预览
                mLivePusher.stopCameraPreview(true);
                // 停止推流
                mLivePusher.stopPusher();
                // 解绑 Listener
                mLivePusher.setPushListener(null);
                // 移除 pusher 引用
                mLivePusher = null;
                // 销毁 videoView
                destroyVideoView();
            }
        });
        return true;
    }

    /**
     * 开始播放，在垫底的 videoView 显示视频
     * 会在当前对象上下文注册一个 TXLivePlayer
     *
     * @param url             播放URL
     * @param playType        播放类型，参见 mlvb.js 相关的枚举定义
     * @param callbackContext
     * @return
     */
    private boolean startPlay(final String url, final int playType, final CallbackContext callbackContext) {
        if (mLivePlayer != null) {
            callbackContext.error("10004");
            return false;
        }
        // 准备 videoView，没有的话生成
        activity.runOnUiThread(new Runnable() {
            public void run() {
                prepareVideoView();
                // 开始推流
                mLivePlayer = new TXLivePlayer(activity);
                TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
                // 将视频绑定到 videoView
                mLivePlayer.setPlayerView(videoView);
                mLivePlayer.startPlay(url, playType);
            }
        });
        return true;
    }

    /**
     * 停止推流，并且注销 mLivePlay 对象
     *
     * @param callbackContext
     * @return
     */
    private boolean stopPlay(final CallbackContext callbackContext) {
        if (mLivePlayer == null) {
            callbackContext.error("10005");
            return false;
        }
        activity.runOnUiThread(new Runnable() {
            public void run() {
                // 停止播放
                mLivePlayer.stopPlay(true);
                // 销毁 videoView
                destroyVideoView();
                // 移除 pusher 引用
                mLivePlayer = null;
            }
        });
        return true;
    }

//    /**
//     * 开始录制
//     *
//     * @param callbackContext
//     * @return
//     */
//    private boolean startRecord(final CallbackContext callbackContext) {
//        if (mLivePlayer != null) {
//            callbackContext.error("10006");
//            return false;
//        }
//        activity.runOnUiThread(new Runnable() {
//            public void run() {
//                //指定一个 ITXVideoRecordListener 用于同步录制的进度和结果
//                mLivePlayer.setVideoRecordListener(recordListener);
//                //启动录制，目前只支持录制视频源，弹幕消息等等目前还不支持
//                mLivePlayer.startRecord(int recordType);
//            }
//        });
//        return true;
//    }

//    /**
//     * 结束录制
//     *
//     * @param callbackContext
//     * @return
//     */
//    private boolean stopRecord(final CallbackContext callbackContext) {
//        if (mLivePlayer != null) {
//            callbackContext.error("10007");
//            return false;
//        }
//        activity.runOnUiThread(new Runnable() {
//            public void run() {
//                mLivePlayer.stopRecord();
//            }
//        });
//        return true;
//    }

    /**
     * check application's permissions
     */
    public boolean hasPermisssion() {
        for (String p : permissions) {
            if (!PermissionHelper.hasPermission(this, p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * We override this so that we can access the permissions variable, which no longer exists in
     * the parent class, since we can't initialize it reliably in the constructor!
     *
     * @param requestCode The code to get request action
     */
    public void requestPermissions(int requestCode) {
        PermissionHelper.requestPermissions(this, requestCode, permissions);
    }

    public void alert(String msg, String title) {
        new AlertDialog.Builder(this.activity)
                .setTitle(title)
                .setMessage(msg)//设置显示的内容
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        // TODO Auto-generated method stub
//                        finish();
                    }
                }).show();//在按键响应事件中显示此对话框
    }

    public void alert(String msg) {
        alert(msg, "系统提示");
    }

    public String jsonEncode(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public int _R(String defType, String name) {
        return activity.getApplication().getResources().getIdentifier(
                name, defType, activity.getApplication().getPackageName());
    }

//    public static void printViewHierarchy(ViewGroup vg, String prefix) {
//        for (int i = 0; i < vg.getChildCount(); i++) {
//            View v = vg.getChildAt(i);
//            String desc = prefix + " | " + "[" + i + "/" + (vg.getChildCount() - 1) + "] " + v.getClass().getSimpleName() + " " + v.getId();
//            Log.v("x", desc);
//
//            if (v instanceof ViewGroup) {
//                printViewHierarchy((ViewGroup) v, desc);
//            }
//        }
//    }


}
