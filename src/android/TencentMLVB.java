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

            int[] sdkver = TXLivePusher.getSDKVersion();
            if (sdkver != null && sdkver.length > 0) {
                String ver = "" + sdkver[0];
                for (int i = 1; i < sdkver.length; ++i) {
                    ver += "." + sdkver[i];
                }
//                alert(ver);
                callbackContext.success(ver);
                return true;
            }
            callbackContext.error("Cannot get rtmp sdk version.");
            return false;

        } else if (action.equals("startPush")) {

            final String pushUrl = args.getString(0);

            final ViewGroup rootView = (ViewGroup) activity.findViewById(android.R.id.content);

            final WebView webView = (WebView) rootView.getChildAt(0);

//            alert(rootView instanceof FrameLayout ? "YES" : "NO", "FrameLayout of root");
//            alert(rootView instanceof CordovaWebView ? "YES" : "NO", "CordovaWebView of root");
//            alert(childView instanceof FrameLayout ? "YES" : "NO", "FrameLayout of child");
//            alert(childView instanceof CordovaWebView ? "YES" : "NO", "CordovaWebView of child");
//            alert(childView instanceof WebView ? "YES" : "NO", "WebView of child");
//            alert(rootView.equals(cordovaWebView) ? "YES" : "NO", "Root view is webview");
//            alert(childView.equals(childView) ? "YES" : "NO", "Child view is webview");
//            alert("Children count: " + rootView.getChildCount());

            alert("Gone");
//            webView.setVisibility(View.GONE);

//            TXCloudVideoView videoView = new TXCloudVideoView(
//                    this.activity
//                    ,
//                    new FrameLayout.LayoutParams(
//                            FrameLayout.LayoutParams.FILL_PARENT,
//                            FrameLayout.LayoutParams.FILL_PARENT
//                    )
//            );

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    LayoutInflater layoutInflater = LayoutInflater.from(activity);
                    TXCloudVideoView videoView =
                            (TXCloudVideoView) layoutInflater.inflate(_R("layout", "layout_video"), null);
                    videoView.setLayoutParams(new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.FILL_PARENT,
                            FrameLayout.LayoutParams.FILL_PARENT
                    ));

                    rootView.addView(videoView);
                    videoView.setVisibility(View.VISIBLE);
                    webView.setBackgroundColor(Color.TRANSPARENT);
                    webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
                    webView.bringToFront();

//                    alert("Children count: " + rootView.getChildCount());

                    TXLivePusher mLivePusher = new TXLivePusher(activity);
                    TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
                    mLivePusher.setConfig(mLivePushConfig);

                    mLivePusher.startPusher(pushUrl);
//            TXCloudVideoView mCaptureView = (TXCloudVideoView) findViewById(_R("id", "video_view"));
                    mLivePusher.startCameraPreview(videoView);

                }
            });


            return true;

        }

        callbackContext.error("Undefined action: " + action);
        return false;


        ////

//        this.cordova.requestPermission(this, 0, Manifest.permission.READ_CONTACTS);

//        if (action.equals("greet")) {
//
//            String name = data.getString(0);
//            String message = "TencentMLVB, " + name;
//
//            Intent intent = new Intent(this.cordova.getActivity(), TestActivity.class);
//            this.cordova.startActivityForResult(this, intent, 0);
//
//            callbackContext.success(message);
//
//            return true;
//
//        } else if (action.equals("initSdk")) {
//
//            int appid = data.getInt(0);
//            int accountType = data.getInt(1);
//            ILiveSDK.getInstance().initSdk(this.context, appid, accountType);
//            iLiveSuccessCallback(0, callbackContext);
//            return true;
//
//        } else if (action.equals("iLiveLogin")) {
//
//            String id = data.getString(0); // 用户 id
//            String sig = data.getString(1); // 用户鉴权密钥 sig
//
//            ILiveLoginManager.getInstance().iLiveLogin(id, sig, new ILiveCallBack() {
//                @Override
//                public void onSuccess(Object data) {
//                    iLiveSuccessCallback(data, callbackContext);
//                }
//
//                @Override
//                public void onError(String module, int errCode, String errMsg) {
//                    iLiveErrorCallback(module, errCode, errMsg, callbackContext);
//                }
//            });
//            return true;
//
//        } else if (action.equals("iLiveLogout")) {
//
//            // TODO: Not Implemented
//            return false;
//
//        } else if (action.equals("createRoom")) {
//
//            int id = data.getInt(0); // 房间id
//            alert("createRoom: " + data.getString(0));
//
//            ILVLiveRoomOption hostOption = new ILVLiveRoomOption("amy")
//                    .controlRole("Host")//角色设置
//                    .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)//权限设置
//                    .cameraId(ILiveConstants.FRONT_CAMERA)//摄像头前置后置
//                    .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);//是否开始半自动接收
//            alert("roomOptionOk");
//            alert(jsonEncode(hostOption));
//
//            //创建房间
//            ILVLiveManager.getInstance().createRoom(id, hostOption, new ILiveCallBack() {
//                @Override
//                public void onSuccess(Object data) {
//                    Gson gson = new Gson();
//                    alert(gson.toJson(data));
//                    iLiveSuccessCallback(data, callbackContext);
////                    Gson gson = new Gson();
////                    callbackContext.success(gson.toJson(data));
////                    Toast.makeText(LiveActivity.this, "create room  ok", Toast.LENGTH_SHORT).show();
////                    logoutBtn.setVisibility(View.INVISIBLE);
////                    backBtn.setVisibility(View.VISIBLE);
//                }
//
//                @Override
//                public void onError(String module, int errCode, String errMsg) {
//                    alert(errMsg);
//                    iLiveErrorCallback(module, errCode, errMsg, callbackContext);
////                    Toast.makeText(LiveActivity.this, module + "|create fail " + errMsg + " " + errMsg, Toast.LENGTH_SHORT).show();
//                }
//            });
//            return true;
//
//        } else if (action.equals("joinRoom")) {
//
//            return false;
//
//        } else {
//            return false;
//        }
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

//    protected void iLiveSuccessCallback(Object data, final CallbackContext callbackContext) {
//        callbackContext.success(jsonEncode(data));
//    }
//
//    protected void iLiveErrorCallback(String module, int errCode, String errMsg, final CallbackContext callbackContext) {
//        JSONObject obj = new JSONObject();
//        try {
//            obj.put("module", module);
//            obj.put("errCode", errCode);
//            obj.put("errMsg", errMsg);
//        } catch (JSONException e) {
//            e.printStackTrace();
//            alert(errMsg);
//            callbackContext.error("ERROR: " + errMsg);
//        }
//        callbackContext.error(obj);
//    }

    public int _R(String defType, String name) {
        return activity.getApplication().getResources().getIdentifier(
                name, defType, activity.getApplication().getPackageName());
    }

    public static void printViewHierarchy(ViewGroup vg, String prefix) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            String desc = prefix + " | " + "[" + i + "/" + (vg.getChildCount() - 1) + "] " + v.getClass().getSimpleName() + " " + v.getId();
            Log.v("x", desc);

            if (v instanceof ViewGroup) {
                printViewHierarchy((ViewGroup) v, desc);
            }
        }
    }


}
