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
    //    private WebView webView;
    private CallbackContext callbackContext;

    private TXCloudVideoView videoView = null;
    private TXLivePusher mLivePusher = null;

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
//        this.webView = (WebView) webView;
        this.cordova = cordova;
        this.activity = cordova.getActivity();
        this.context = this.activity.getApplicationContext();
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

        this.callbackContext = callbackContext;

//        alert(action);

        if (action.equals("getVersion")) {
            return getVersion(callbackContext);
        } else if (action.equals("startPush")) {
            final String pushUrl = args.getString(0);
            return startPush(pushUrl, callbackContext);
        } else if (action.equals("stopPush")) {
            return stopPush(callbackContext);
        } else if (action.equals("onPushEvent")) {
            alert("尚未实现");
        } else if (action.equals("startPlay")) {
            alert("尚未实现");
        } else if (action.equals("stopPlay")) {
            alert("尚未实现");
        } else if (action.equals("onPlayEvent")) {
            alert("尚未实现");
        } else if (action.equals("setVideoQuality")) {
            alert("尚未实现");
        } else if (action.equals("setBeautyFilterDepth")) {
            alert("尚未实现");
        } else if (action.equals("setWhiteningFilterDepth")) {
            alert("尚未实现");
        } else if (action.equals("setFilter")) {
            alert("尚未实现");
        } else if (action.equals("switchCamera")) {
            alert("尚未实现");
        } else if (action.equals("toggleTorch")) {
            alert("尚未实现");
        } else if (action.equals("setFocusPosition")) {
            alert("尚未实现");
        } else if (action.equals("setWaterMark")) {
            alert("尚未实现");
        } else if (action.equals("setPaushImage")) {
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
            alert("尚未实现");
        } else if (action.equals("stopRecord")) {
            alert("尚未实现");
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

    private void prepareVideoView() {
        final ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);
        final WebView webView = (WebView) rootView.getChildAt(0);
        if (videoView != null) return;
        // 通过 layout 文件插入 videoView
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        this.videoView = (TXCloudVideoView) layoutInflater.inflate(_R("layout", "layout_video"), null);
        activity.runOnUIThread(Runnable runnable = new Runnable() {
            public void run() {
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
                alert("run finish");
            }
        });
    }

    /**
     * 返回 MLVB SDK 版本
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
     * @param url
     * @param callbackContext
     * @return
     */
    private boolean startPush(final String url, final CallbackContext callbackContext) {
        if (mLivePusher != null) {
            callbackContext.error("10002");
            return false;
        }
        // 准备 videoView，没有的话生成
        prepareVideoView();
        // 开始推流
        this.mLivePusher = new TXLivePusher(activity);
        TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
        mLivePusher.setConfig(mLivePushConfig);
        mLivePusher.startPusher(url);
        // 将视频绑定到 videoView
        mLivePusher.startCameraPreview(videoView);
        alert("Good");
        return true;
    }

    private boolean stopPush(final CallbackContext callbackContext) {
        if (mLivePusher == null) {
            callbackContext.error("10003");
            return false;
        }
        // 停止摄像头预览
        mLivePusher.stopCameraPreview(true);
        // 停止推流
        mLivePusher.stopPusher();
        // 解绑 Listener
        mLivePusher.setPushListener(null);
        // 移除 pusher 引用
        this.mLivePusher = null;
        return true;
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
