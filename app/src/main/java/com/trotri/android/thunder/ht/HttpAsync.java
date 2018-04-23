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

package com.trotri.android.thunder.ht;

import com.trotri.android.thunder.ap.ThreadPool;
import com.trotri.android.thunder.ap.UiThread;

import java.util.Map;

/**
 * HttpAsync class file
 * HTTP异步类，在后台线程中执行HTTP请求，执行完后，在主线程中回调Listener接口
 * 需要权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: HttpAsync.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class HttpAsync {

    private static final String TAG = "HttpAsync";

    /**
     * HTTP类
     */
    private final Http mHttp;

    /**
     * 构造方法：初始化HTTP类
     *
     * @param http HTTP类
     */
    public HttpAsync(Http http) {
        mHttp = http;
    }

    /**
     * 发送GET请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param url 访问链接，a URL String
     * @param l   回执线程的处理接口
     */
    public void get(String url, Http.Listener l) {
        get(url, "", l);
    }

    /**
     * 发送GET请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param url    访问链接，a URL String
     * @param params 查询串，Map集合，Key => String, Value => String
     * @param l      回执线程的处理接口
     */
    public void get(String url, Map<String, String> params, Http.Listener l) {
        request(Http.METHOD_GET, url, params, l);
    }

    /**
     * 发送GET请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param url    访问链接，a URL String
     * @param params 查询串，a String, or null
     * @param l      回执线程的处理接口
     */
    public void get(String url, String params, Http.Listener l) {
        request(Http.METHOD_GET, url, params, l);
    }

    /**
     * 发送POST请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param url    访问链接，a URL String
     * @param params 查询串，Map集合，Key => String, Value => String
     * @param l      回执线程的处理接口
     */
    public void post(String url, Map<String, String> params, Http.Listener l) {
        request(Http.METHOD_POST, url, params, l);
    }

    /**
     * 发送POST请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param url    访问链接，a URL String
     * @param params 查询串，a String, or null
     * @param l      回执线程的处理接口
     */
    public void post(String url, String params, Http.Listener l) {
        request(Http.METHOD_POST, url, params, l);
    }

    /**
     * 发送HTTP请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param method 访问方式
     * @param url    访问链接，a URL String
     * @param params 查询串，Map集合，Key => String, Value => String
     * @param l      回执线程的处理接口
     */
    public void request(String method, String url, Map<String, String> params, Http.Listener l) {
        request(method, url, mHttp.joinString(params), l);
    }

    /**
     * 发送HTTP请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param method 访问方式
     * @param url    访问链接，a URL String
     * @param params 查询串，a String, or null
     * @param l      回执线程的处理接口
     */
    public void request(String method, String url, String params, Http.Listener l) {
        ThreadPool.execute(new AsyncCommand(method, url, params, l));
    }

    /**
     * 【后台线程】执行命令，发送HTTP请求
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.HttpAsync$AsyncCommand { *; }
     * </p>
     */
    class AsyncCommand extends ThreadPool.AbstractCommand {
        /**
         * 访问方式
         */
        private String mMethod;

        /**
         * 访问链接，a URL String
         */
        private String mUrl;

        /**
         * 查询串，a String, or null
         */
        private String mParams;

        /**
         * 回执线程的处理接口
         */
        private Http.Listener mListener;

        /**
         * 构造方法：初始化访问方式、访问链接、查询串、回执线程的处理接口
         *
         * @param method 访问方式
         * @param url    访问链接，a URL String
         * @param params 查询串，a String, or null
         * @param l      回执线程的处理接口
         */
        public AsyncCommand(String method, String url, String params, Http.Listener l) {
            mMethod = method;
            mUrl = url;
            mParams = params;
            mListener = l;
        }

        @Override
        public void exec() {
            mHttp.request(mMethod, mUrl, mParams, new Http.Listener() {
                @Override
                public void onComplete(byte[] data) {
                    UiThread.exec(new UiCommand(data, mListener));
                }

                @Override
                public void onError(int statusCode, Throwable tr, byte[] data) {
                    UiThread.exec(new UiCommand(statusCode, tr, data, mListener));
                }
            });
        }

    }

    /**
     * 【主线程】执行命令，通知Http请求结果
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.HttpAsync$UiCommand { *; }
     * </p>
     */
    class UiCommand extends UiThread.AbstractCommand {
        /**
         * 结果类型：Http请求完成
         */
        public static final int TYPE_COMPLETE = 1;

        /**
         * 结果类型：Http请求失败
         */
        public static final int TYPE_ERROR = 2;

        /**
         * HTTP状态码
         */
        private int mStatusCode;

        /**
         * HTTP返回数据，a Byte Array
         */
        private byte[] mData;

        /**
         * 失败原因，a Throwable Object
         */
        private Throwable mThr;

        /**
         * 结果类型，1：Http请求完成、2：Http请求失败
         */
        private int mType;

        /**
         * 回执线程的处理接口
         */
        private Http.Listener mListener;

        /**
         * 构造方法：初始化HTTP返回数据、回执线程的处理接口
         *
         * @param data HTTP返回数据，a Byte Array
         * @param l    回执线程的处理接口
         */
        public UiCommand(byte[] data, Http.Listener l) {
            mType = TYPE_COMPLETE;

            mData = data;
            mListener = l;
        }

        /**
         * 构造方法：初始化HTTP状态码、失败原因、HTTP返回数据、回执线程的处理接口
         *
         * @param statusCode HTTP状态码
         * @param tr         失败原因，a Throwable Object
         * @param data       HTTP返回数据，a Byte Array
         * @param l          回执线程的处理接口
         */
        public UiCommand(int statusCode, Throwable tr, byte[] data, Http.Listener l) {
            mType = TYPE_ERROR;

            mStatusCode = statusCode;
            mThr = tr;
            mData = data;
            mListener = l;
        }

        @Override
        public void exec() {
            switch (mType) {
                case TYPE_COMPLETE:
                    mListener.onComplete(mData);
                    break;
                case TYPE_ERROR:
                    mListener.onError(mStatusCode, mThr, mData);
                    break;
                default:
                    break;
            }
        }

    }

}
