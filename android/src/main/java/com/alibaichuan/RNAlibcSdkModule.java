
package com.alibaichuan;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.util.Log;
import android.app.Application;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.ui.context.CallbackContext;
import com.alibaba.baichuan.android.trade.AlibcTrade;
import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeCallback;
import com.alibaba.baichuan.android.trade.callback.AlibcTradeInitCallback;
import com.alibaba.baichuan.android.trade.model.AlibcShowParams;
import com.alibaba.baichuan.android.trade.model.OpenType;
import com.alibaba.baichuan.android.trade.page.AlibcAddCartPage;
import com.alibaba.baichuan.android.trade.page.AlibcBasePage;
import com.alibaba.baichuan.android.trade.page.AlibcDetailPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyCartsPage;
import com.alibaba.baichuan.android.trade.page.AlibcMyOrdersPage;
import com.alibaba.baichuan.android.trade.page.AlibcShopPage;
import com.alibaba.baichuan.trade.biz.AlibcConstants;
import com.alibaba.baichuan.trade.biz.context.AlibcTradeResult;
import com.alibaba.baichuan.trade.biz.core.taoke.AlibcTaokeParams;
import com.alibaba.baichuan.trade.biz.login.AlibcLogin;
import com.alibaba.baichuan.trade.biz.login.AlibcLoginCallback;
import com.alibaba.baichuan.trade.biz.context.AlibcResultType;
import com.alibaba.baichuan.trade.biz.applink.adapter.AlibcFailModeType;

import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

public class RNAlibcSdkModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private static final String TAG = "RNAlibcSdkModule";
    private final static String NOT_LOGIN = "not login";
    private final static String INVALID_TRADE_RESULT = "invalid trade result";
    private final static String INVALID_PARAM = "invalid";

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            CallbackContext.onActivityResult(requestCode, resultCode, intent);
        }
    };

    static private RNAlibcSdkModule mRNAlibcSdkModule = null;

    static public RNAlibcSdkModule sharedInstance(ReactApplicationContext context) {
        if (mRNAlibcSdkModule == null) {
            return new RNAlibcSdkModule(context);
        } else {
            return mRNAlibcSdkModule;
        }
    }

    public RNAlibcSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "RNAlibcSdk";
    }

    /**
     * 初始化SDK---无参数传入
     */
    @ReactMethod
    public void initTae(final Callback callback) {
        AlibcTradeSDK.asyncInit((Application) reactContext.getApplicationContext(), new AlibcTradeInitCallback() {
            @Override
            public void onSuccess() {
                callback.invoke(null, "init success");
            }

            @Override
            public void onFailure(int code, String msg) {
                WritableMap map = Arguments.createMap();
                map.putInt("code", code);
                map.putString("msg", msg);
                callback.invoke(map);
            }
        });
    }

    /**
     * 登录
     * @param callback
     */
    @ReactMethod
    public void login(final Callback successCallback, final Callback errorCallback) {
        AlibcLogin alibcLogin = AlibcLogin.getInstance();
        alibcLogin.showLogin(new AlibcLoginCallback() {
            @Override
            public void onSuccess(int loginResult, String openId, String userNick) {
                // 参数说明：
                // loginResult(0--登录初始化成功；1--登录初始化完成；2--登录成功)
                // openId：用户id
                // userNick: 用户昵称
                // Log.i(TAG, "获取淘宝用户信息: " + AlibcLogin.getInstance().getSession());
                Session session = AlibcLogin.getInstance().getSession();
                WritableMap map = Arguments.createMap();
                map.putString("userNick", userNick);
                // map.putString("loginResult", loginResult);
                // map.putString("userAva", AlibcLogin.getInstance().getSession().ava);
                map.putString("openId", openId);
                map.putString("openSid", session.openSid);
                map.putString("topAccessToken", session.topAccessToken);
                map.putString("topAuthCode", session.topAuthCode);
                successCallback.invoke(map);
            }

            @Override
            public void onFailure(int code, String msg) {
                // code：错误码  msg： 错误信息
                WritableMap map = Arguments.createMap();
                map.putInt("code", code);
                map.putString("msg", msg);
                errorCallback.invoke(map);
            }
        });
    }

    /**
     * 退出登录---无参数传入
     */
    @ReactMethod
    public void logout(final Callback successCallback, final Callback errorCallback) {
        if (AlibcLogin.getInstance().getSession() != null
                && AlibcLogin.getInstance().isLogin()) {
            AlibcLogin alibcLogin = AlibcLogin.getInstance();

            alibcLogin.logout(new AlibcLoginCallback() {
                @Override
                public void onSuccess(int i, String s, String s1) {
                    WritableMap map = Arguments.createMap();
                    map.putString("code", "0");
                    map.putString("message", "success");
                    successCallback.invoke(map);
                }

                @Override
                public void onFailure(int i, String s) {
                    WritableMap map = Arguments.createMap();
                    map.putString("code", Integer.toString(i));
                    map.putString("message", s);
                    errorCallback.invoke(map);
                }
            });
        } else {
            WritableMap map = Arguments.createMap();
            map.putString("code", "90000");
            map.putString("message", "Not logged in");
            errorCallback.invoke(map);
        }
    }

    /**
     * 展示
     */
    @ReactMethod
    public void show(final ReadableMap param, final Callback callback) {
        String type = param.getString("type");
        ReadableMap payload = param.getMap("payload");
        switch (type) {
            case "detail":
                this._show(new AlibcDetailPage(payload.getString("itemid")), param, callback);
                break;
            case "url":
                this._openUrl(payload.getString("url"), param, callback);
                break;
            case "shop":
                this._show(new AlibcShopPage(payload.getString("shopid")), param, callback);
                break;
            case "orders":
                this._show(new AlibcMyOrdersPage(payload.getInt("orderStatus"), payload.getBoolean("allOrder")), param, callback);
                break;
            case "addCard":
                this._show(new AlibcAddCartPage(param.getString("itemid")), param, callback);
                break;
            case "mycard":
                this._show(new AlibcMyCartsPage(), param, callback);
                break;
            default:
                callback.invoke(INVALID_PARAM);
                break;
        }
    }

    private void _openUrl(String url, final ReadableMap param, final Callback callback) {
        AlibcShowParams showParams = new AlibcShowParams();
        showParams = this.dealShowParams(param);
        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");
        taokeParams = this.dealTaokeParams(param);
        Map<String, String> exParams = new HashMap<String, String>();
        exParams = this.dealExParams(param);

        AlibcTrade.openByUrl(getCurrentActivity(),
                "",
                url,
                null,
                new WebViewClient(),
                new WebChromeClient(),
                showParams,
                taokeParams,
                exParams,
                new AlibcTradeCallback() {
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        Log.v("ReactNative", TAG + ":onTradeSuccess");
                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                        if (tradeResult.resultType.equals(AlibcResultType.TYPECART)) {
                            //加购成功
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "card");
                            callback.invoke(null, map);
                        } else if (tradeResult.resultType.equals(AlibcResultType.TYPEPAY)) {
                            //支付成功
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "pay");
                            map.putString("orders", "" + tradeResult.payResult.paySuccessOrders);
                            callback.invoke(null, map);
                        } else {
                            callback.invoke(INVALID_TRADE_RESULT);
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        WritableMap map = Arguments.createMap();
                        map.putString("type", "error");
                        map.putInt("code", code);
                        map.putString("msg", msg);
                        callback.invoke(msg);
                    }
                });
    }

    private void _show(AlibcBasePage page, final ReadableMap param, final Callback callback) {
        // 处理参数
        AlibcShowParams showParams = new AlibcShowParams();
        showParams = this.dealShowParams(param);
        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");
        taokeParams = this.dealTaokeParams(param);
        Map<String, String> exParams = new HashMap<String, String>();
        exParams = this.dealExParams(param);

        AlibcTrade.openByBizCode(getCurrentActivity(),
                page,
                null,
                new WebViewClient(),
                new WebChromeClient(),
                "detail",
                showParams,
                taokeParams,
                exParams,
                new AlibcTradeCallback() {
                    public void onTradeSuccess(AlibcTradeResult tradeResult) {
                        Log.v("ReactNative", TAG + ":onTradeSuccess");
                        //打开电商组件，用户操作中成功信息回调。tradeResult：成功信息（结果类型：加购，支付；支付结果）
                        if (tradeResult.resultType.equals(AlibcResultType.TYPECART)) {
                            //加购成功
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "card");
                            callback.invoke(null, map);
                        } else if (tradeResult.resultType.equals(AlibcResultType.TYPEPAY)) {
                            //支付成功
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "pay");
                            map.putString("orders", "" + tradeResult.payResult.paySuccessOrders);
                            callback.invoke(null, map);
                        } else {
                            callback.invoke(INVALID_TRADE_RESULT);
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        WritableMap map = Arguments.createMap();
                        map.putString("type", "error");
                        map.putInt("code", code);
                        map.putString("msg", msg);
                        callback.invoke(msg);
                    }
                });
    }

    /**
     * 处理showParams公用参数
     */
    private AlibcShowParams dealShowParams(final ReadableMap param) {
        ReadableMap payload = param.getMap("payload");
        // 初始化参数
        String opentype = "html5";

        AlibcShowParams showParams = new AlibcShowParams();

        if (payload.getString("opentype") != null
                || !payload.getString("opentype").equals("")) {
            opentype = payload.getString("opentype");
        }

        if (opentype.equals("auto")) {
            showParams.setOpenType(OpenType.Auto);
        } else {
            showParams.setOpenType(OpenType.Native);
        }
        showParams.setClientType("taobao");
        showParams.setBackUrl("alisdk://");
        showParams.setNativeOpenFailedMode(AlibcFailModeType.AlibcNativeFailModeJumpH5);
        return showParams;
    }

    /**
     * 处理taokeParams公用参数
     */
    private AlibcTaokeParams dealTaokeParams(final ReadableMap param) {
        ReadableMap payload = param.getMap("payload");
        // 初始化参数
        String mmpid = "mm_23448739_6500158_22182062";
        String adzoneid = "60538822";
        String tkkey = "23482513";

        // 设置mmpid
        if (payload.getString("mmpid") != null
                || !payload.getString("mmpid").equals("")) {
            mmpid = payload.getString("mmpid");
        }

        // 设置adzoneid
        if (payload.getString("adzoneid") != null
                || !payload.getString("adzoneid").equals("")) {
            adzoneid = payload.getString("adzoneid");
        }

        // 设置tkkey
        if (payload.getString("tkkey") != null
                || !payload.getString("tkkey").equals("")) {
            tkkey = payload.getString("tkkey");
        }

        AlibcTaokeParams taokeParams = new AlibcTaokeParams("", "", "");
        taokeParams.setPid(mmpid);
        taokeParams.setAdzoneid(adzoneid);
        Map<String, String> taokeExParams = new HashMap<String, String>();
        taokeExParams.put("taokeAppkey", tkkey);
        taokeParams.extraParams = taokeExParams;
        return taokeParams;
    }

    /**
     * 处理exParams公用参数
     */
    private Map<String, String> dealExParams(final ReadableMap param) {
        ReadableMap payload = param.getMap("payload");
        // 初始化参数
        Map<String, String> exParams = new HashMap<String, String>();
        String isvcode = "app";
        // 设置tkkey
        if (payload.getString("isvcode") != null
                || !payload.getString("isvcode").equals("")) {
            isvcode = payload.getString("isvcode");
        }
        exParams.put(AlibcConstants.ISV_CODE, isvcode);
        return exParams;
    }
}
