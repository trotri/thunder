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

package com.trotri.android.thunder.view;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

import java.io.ByteArrayInputStream;

/**
 * WebViewHelper class file
 * 浏览器辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: WebViewHelper.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class WebViewHelper {

    private static final String TAG = "WebViewHelper";

    /**
     * 字符编码
     */
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";

    /**
     * 默认的字符编码
     */
    public static final String DEFAULT_CHARSET = CHARSET_UTF8;

    /**
     * 空白的HTML
     */
    public static final String BLANK_HTML = "<html><head></head><body></body><html>";

    /**
     * 空白的HTML，满屏
     */
    public static final String BLANK_HTML_FULL_SCREEN = "<html><head><title></title>"
            + "<style type='text/css'>*{margin:0;padding:0;}</style>"
            + "</head><body></body><html>";

    /**
     * 空白的数据
     */
    public static final String BLANK_DATA = "data:text/html," + BLANK_HTML;

    /**
     * 空白的URL
     */
    public static final String BLANK_URL = "about:blank";

    /**
     * 拦截WebView的响应HTML
     */
    public static final byte[] INTERCEPT_RESPONSE_HTML = BLANK_HTML_FULL_SCREEN.getBytes();

    /**
     * 获取拦截WebView的响应数据
     */
    public static final WebResourceResponse sInterceptResponse = new WebResourceResponse("text/html", DEFAULT_CHARSET, new ByteArrayInputStream(INTERCEPT_RESPONSE_HTML));

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static WebViewHelper sInstance;

    /**
     * 用户代理
     */
    private String mUserAgent;

    /**
     * a WebView Object
     */
    private WebView mWebView;

    /**
     * 构造方法：初始化WebView
     *
     * @param v a WebView Object
     */
    private WebViewHelper(WebView v) {
        mWebView = v;
        onInitialize();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static WebViewHelper getInstance(WebView v) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new WebViewHelper(v);
            }

            return sInstance;
        }
    }

    /**
     * 初始化浏览器信息，子线程中不可用
     */
    private void onInitialize() {
        onInitWebView();
        onInitWebSettings();
        onInitUserAgent();
    }

    /**
     * 初始化WebView，子线程中不可用
     */
    private void onInitWebView() {
        mWebView.setDrawingCacheEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.requestFocusFromTouch();
        mWebView.requestFocus();
    }

    /**
     * 初始化WebSettings，子线程中不可用
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void onInitWebSettings() {
        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSupportMultipleWindows(true);
    }

    /**
     * 设置初始的用户代理，子线程中不可用
     */
    private void onInitUserAgent() {
        WebSettings settings = mWebView.getSettings();

        mUserAgent = settings.getUserAgentString();
    }

    /**
     * 加载URL，子线程中不可用
     *
     * @param url a URL String
     */
    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            Logger.e(Constants.TAG_LOG, TAG + " loadUrl() url is Empty");
            return;
        }

        Logger.d(Constants.TAG_LOG, TAG + " loadUrl() url: " + url);

        mWebView.loadUrl(url);
    }

    /**
     * 加载HTML代码，子线程中不可用
     *
     * @param htmlCode HTML代码
     * @param charset  字符编码
     */
    public void loadHtml(String htmlCode, String charset) {
        if (TextUtils.isEmpty(htmlCode)) {
            Logger.e(Constants.TAG_LOG, TAG + " loadHtml() htmlCode is Empty");
            return;
        }

        charset = (charset == null) ? "" : charset.trim();
        if (TextUtils.isEmpty(charset)) {
            charset = DEFAULT_CHARSET;
        }

        Logger.d(Constants.TAG_LOG, TAG + " loadHtml() htmlCode: " + htmlCode + ", charset: " + charset);

        mWebView.loadData(htmlCode, "text/html", charset);
    }

    /**
     * 加载JS代码，子线程中不可用
     *
     * @param jsCode JS代码
     */
    public void loadJs(String jsCode) {
        if (TextUtils.isEmpty(jsCode)) {
            Logger.e(Constants.TAG_LOG, TAG + " loadJs() jsCode is Empty");
            return;
        }

        Logger.d(Constants.TAG_LOG, TAG + " loadJs() jsCode: " + jsCode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript("javascript: " + jsCode, null);
        } else {
            mWebView.loadUrl("javascript: " + jsCode);
        }
    }

    /**
     * 获取用户代理
     *
     * @return 用户代理
     */
    public String getUserAgent() {
        return mUserAgent;
    }

    /**
     * 设置用户代理，子线程中不可用
     *
     * @param userAgent 用户代理
     */
    public void setUserAgent(String userAgent) {
        WebSettings settings = mWebView.getSettings();

        mUserAgent = (userAgent == null) ? "" : userAgent.trim();
        settings.setUserAgentString(mUserAgent);
    }

    /**
     * 获取WebView对象
     *
     * @return an WebView Object
     */
    public WebView getWebView() {
        return mWebView;
    }

}
