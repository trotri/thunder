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

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * Explorer class file
 * 浏览器类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Explorer.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Explorer extends WebView {

    public static final String TAG = "Explorer";

    /**
     * 浏览器辅助类
     */
    private WebViewHelper mHelper;

    /**
     * 附加的滚动条监听类
     */
    private OnExtraScrollChangeListener mOnExtraScrollChangeListener = null;

    public Explorer(Context c) {
        super(c);
    }

    public Explorer(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    /**
     * 初始化浏览器信息，子线程中不可用
     * 需要手动调用此方法
     */
    public void onInitialize() {
        mHelper = WebViewHelper.getInstance(this);

        // 影响Js的alert函数
        setWebChromeClient(new WebChromeClient());
    }

    /**
     * 加载URL，子线程中不可用
     *
     * @param url a URL String
     */
    @Override
    public void loadUrl(String url) {
        mHelper.loadUrl(url);
    }

    /**
     * 加载HTML代码，子线程中不可用
     *
     * @param htmlCode HTML代码
     * @param charset  字符编码
     */
    public void loadHtml(String htmlCode, String charset) {
        mHelper.loadHtml(htmlCode, charset);
    }

    /**
     * 加载JS代码，子线程中不可用
     *
     * @param jsCode JS代码
     */
    public void loadJs(String jsCode) {
        mHelper.loadJs(jsCode);
    }

    /**
     * 获取用户代理
     *
     * @return 用户代理
     */
    public String getUserAgent() {
        return mHelper.getUserAgent();
    }

    /**
     * 设置用户代理，子线程中不可用
     *
     * @param userAgent 用户代理
     */
    public void setUserAgent(String userAgent) {
        mHelper.setUserAgent(userAgent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnExtraScrollChangeListener != null) {
            mOnExtraScrollChangeListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    /**
     * 设置附加的滚动条监听类
     *
     * @param onExtraScrollChangeListener 滚动条监听类
     * @return This Explorer object to allow for chaining of calls to set methods
     */
    public Explorer setOnExtraScrollChangeListener(OnExtraScrollChangeListener onExtraScrollChangeListener) {
        mOnExtraScrollChangeListener = onExtraScrollChangeListener;
        return this;
    }

    /**
     * 附加的监听滚动条接口
     */
    public interface OnExtraScrollChangeListener {
        /**
         * This is called in response to an internal scroll in this view (i.e., the
         * view scrolled its own contents). This is typically as a result of
         * {@link #scrollBy(int, int)} or {@link #scrollTo(int, int)} having been
         * called.
         *
         * @param v          a WebView Object
         * @param scrollX    Current horizontal scroll origin.
         * @param scrollY    Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChanged(WebView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    /**
     * 自定义WebViewClient
     */
    public static class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onReceivedError(WebView view, int code, String desc, String url) {
            super.onReceivedError(view, code, desc, url);
            Logger.e(Constants.TAG_LOG, TAG + " url: " + url + ", code: " + code + ", desc: " + desc);
            view.loadUrl(WebViewHelper.BLANK_URL);
        }
    }

    /**
     * 自定义WebChromeClient
     */
    public static class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Logger.d(Constants.TAG_LOG, TAG + "Web title: " + title);
        }
    }

}
