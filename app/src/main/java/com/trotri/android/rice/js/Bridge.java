/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trotri.android.rice.js;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.trotri.android.rice.bean.Constants;
import com.trotri.android.rice.util.GsonHelper;
import com.trotri.android.thunder.ap.Logger;

/**
 * Bridge class file
 * Js请求和回调辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Bridge.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class Bridge {

    public static final String TAG = "Bridge";

    /**
     * Used to expose the object in JavaScript
     */
    public static final String JS_FUNCTION_NAME = "Thunder";

    /**
     * JavaScript Bridge Object
     */
    public static final String JS_BRIDGE_NAME = "javascript:" + JS_FUNCTION_NAME + "JsBridge";

    /**
     * Callback JavaScript
     */
    public static final String JS_ON_LISTEN_NAME = JS_BRIDGE_NAME + ".onListen";

    /**
     * Before Asynchronous Requests Callback JavaScript
     */
    public static final String JS_BEFORE_ASYNC_ON_LISTEN_NAME = JS_BRIDGE_NAME + ".onBeforeAsyncListen";

    /**
     * 如果方法名不存在，没有找到Js处理器，错误码
     */
    public static final int METHOD_NOT_FOUND_ERR_NO = -3;

    /**
     * 如果方法名不存在，没有找到Js处理器，错误消息
     */
    public static final String METHOD_NOT_FOUND_ERR_MSG = "Js method not found exception";

    /**
     * 如果Json格式不正确，错误码
     */
    public static final int JSON_SYNTAX_ERR_NO = -4;

    /**
     * 如果Json格式不正确，错误消息
     */
    public static final String JSON_SYNTAX_ERR_MSG = "Js parameter json syntax exception";

    /**
     * 主线程
     */
    private static Handler mMainHandler = new Handler(Looper.getMainLooper());

    /**
     * 用于解析参数和回调数据
     */
    private final Gson mGson;

    /**
     * a WebView Object
     */
    private final Explorer mWebView;

    /**
     * 上下文环境
     */
    private final Context mContext;

    /**
     * 构造方法：初始化上下文环境、an Explorer Object
     */
    public Bridge(Context c, Explorer v) {
        mContext = c.getApplicationContext();
        mWebView = v;
        mGson = GsonHelper.create();
    }

    /**
     * 执行操作
     *
     * @param key       寄存回调函数的Key
     * @param method    请求方法名
     * @param parameter 请求参数，a Json String
     */
    public void exec(String key, String method, String parameter) {
        HandlerFactory.BaseHandler handler = HandlerFactory.getHandler(method);
        if (handler == null) {
            loadMethodNotFoundException(key);
            return;
        }

        handler.run(this, key, parameter);
    }

    /**
     * 请求方法名不存在，回调Js报告异常
     *
     * @param key 寄存回调函数的Key
     */
    public void loadMethodNotFoundException(String key) {
        Logger.e(Constants.TAG_LOG, TAG + " loadMethodNotFoundException " + METHOD_NOT_FOUND_ERR_MSG);
        loadListen(key, null, METHOD_NOT_FOUND_ERR_NO, METHOD_NOT_FOUND_ERR_MSG);
    }

    /**
     * 请求参数Json字符串格式错误，转Bean失败，回调Js报告异常
     *
     * @param key 寄存回调函数的Key
     */
    public void loadJsonSyntaxException(String key) {
        Logger.e(Constants.TAG_LOG, TAG + " loadJsonSyntaxException " + JSON_SYNTAX_ERR_MSG);
        loadListen(key, null, JSON_SYNTAX_ERR_NO, JSON_SYNTAX_ERR_MSG);
    }

    /**
     * 回调Js代码：ThunderJsBridge.onListen
     *
     * @param key  寄存回调函数的Key
     * @param data listener.success(data); data参数
     */
    public void loadListen(String key, String data) {
        loadListen(key, data, 0, "");
    }

    /**
     * 回调Js代码：ThunderJsBridge.onListen
     *
     * @param key    寄存回调函数的Key
     * @param data   listener.success(data); data参数
     * @param errNo  listener.error(throwable); throwable的错误码
     * @param errMsg listener.error(throwable); throwable的错误消息
     */
    public void loadListen(String key, String data, int errNo, String errMsg) {
        String code = JS_ON_LISTEN_NAME + "(";
        code += "'" + key + "', ";
        code += (TextUtils.isEmpty(data) ? "''" : data) + ", ";
        code += convertThrowable(errNo, errMsg);
        code += ")";
        loadJs(code);
    }

    /**
     * 回调Js代码：ThunderJsBridge.onBeforeAsyncListen
     *
     * @param key  寄存回调函数的Key
     * @param data listener.beforeAsync(data); data参数
     */
    public void loadBeforeAsyncListen(String key, String data) {
        String code = JS_BEFORE_ASYNC_ON_LISTEN_NAME + "(";
        code += "'" + key + "', ";
        code += (TextUtils.isEmpty(data) ? "''" : data);
        code += ")";
        loadJs(code);
    }

    /**
     * 回调Js代码
     *
     * @param code Js代码，需要"javascript:"前缀
     */
    public void loadJs(final String code) {
        Logger.d(Constants.TAG_LOG, TAG + " loadJs " + code);
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(code);
            }
        });
    }

    /**
     * 请求参数Json字符串转Bean对象，如果失败，回调Js报告异常
     *
     * @param key       寄存回调函数的Key
     * @param parameter 请求参数，a Json String
     * @param classOfT  a Bean Class
     * @param <T>       a Generic of Bean
     * @return a Bean Object, or null
     */
    public <T> T convertParameterThrowException(String key, String parameter, Class<T> classOfT) {
        T result = convertParameter(parameter, classOfT);
        if (result == null) {
            loadJsonSyntaxException(key);
        }

        return result;
    }

    /**
     * 请求参数Json字符串转Bean对象
     *
     * @param parameter 请求参数，a Json String
     * @param classOfT  a Bean Class
     * @param <T>       a Generic of Bean
     * @return a Bean Object, or null
     */
    public <T> T convertParameter(String parameter, Class<T> classOfT) {
        try {
            return mGson.fromJson(parameter, classOfT);
        } catch (JsonSyntaxException e) {
            Logger.e(Constants.TAG_LOG, TAG + " convertParameter()", e);
            return null;
        }
    }

    /**
     * 错误码和错误消息转Json字符串
     *
     * @param errNo  错误码
     * @param errMsg 错误消息
     * @return a Json String
     */
    public String convertThrowable(int errNo, String errMsg) {
        return convertData(new Throwable(errNo, ((errMsg == null) ? "" : errMsg)));
    }

    /**
     * Bean对象转Json字符串
     *
     * @param o a Bean Object
     * @return a Json String
     */
    public String convertData(Object o) {
        return mGson.toJson(o);
    }

    /**
     * 获取上下文环境
     *
     * @return a Context Object
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Throwable class
     * 错误信息类
     *
     * @since 1.0
     */
    class Throwable {
        /**
         * 错误码
         */
        int mErrNo;

        /**
         * 错误消息
         */
        String mErrMsg;

        /**
         * 构造方法：初始化错误码、错误消息
         */
        public Throwable(int errNo, String errMsg) {
            mErrNo = errNo;
            mErrMsg = errMsg;
        }

    }

}
