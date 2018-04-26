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

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;

/**
 * Explorer class file
 * 浏览器类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Explorer.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class Explorer extends com.trotri.android.thunder.view.Explorer {
    /**
     * Js请求和回调辅助类
     */
    private Bridge mJsBridge;

    public Explorer(Context c) {
        super(c);
    }

    public Explorer(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    public void onInitialize() {
        super.onInitialize();

        onInitJsBridge();
    }

    /**
     * 初始化Js请求和回调辅助类
     */
    @SuppressLint("AddJavascriptInterface")
    private void onInitJsBridge() {
        mJsBridge = new Bridge(getContext(), this);

        addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void invoke(String key, String method, String parameter) {
                mJsBridge.exec(key, method, parameter);
            }
        }, Bridge.JS_FUNCTION_NAME);
    }

    /**
     * 获取Js请求和回调辅助类
     *
     * @return Js请求和回调辅助类
     */
    public Bridge getJsBridge() {
        return mJsBridge;
    }

}
