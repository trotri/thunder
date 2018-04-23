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

import android.content.Context;
import android.text.TextUtils;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.HttpStatus;
import com.trotri.android.thunder.ap.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * Http class file
 * HTTP类
 * 需要权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 * 注：主线程里执行Http请求会报android.os.NetworkOnMainThreadException异常
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Http.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Http {

    public static final String TAG = "Http";

    /**
     * 字符编码
     */
    public static final String CHARSET_UTF8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";

    /**
     * 访问协议
     */
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";

    /**
     * 访问方式
     */
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    /**
     * 默认的连接超时，单位：毫秒
     */
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 6 * 1000;

    /**
     * 默认的字符编码
     */
    public static final String DEFAULT_CHARSET = CHARSET_UTF8;

    /**
     * 默认的分配缓存空间，单位：字节
     */
    public static final int DEFAULT_BUCKET = 4096;

    /**
     * 默认的是否使用缓存
     */
    public static final boolean DEFAULT_USE_CACHES = false;

    /**
     * 连接超时，单位：毫秒
     */
    private int mConnectTimeOutMs = DEFAULT_CONNECT_TIMEOUT_MS;

    /**
     * 字符编码，默认：UTF-8
     */
    private String mCharset = DEFAULT_CHARSET;

    /**
     * 分配缓存空间，单位：字节
     */
    private int mBucket = DEFAULT_BUCKET;

    /**
     * 是否使用缓存
     */
    private boolean mUseCaches = DEFAULT_USE_CACHES;

    /**
     * 网络连接类
     */
    private HttpProxy mHttpProxy;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、网络连接类
     */
    private Http(Context c) {
        mAppContext = c.getApplicationContext();
        mHttpProxy = HttpProxy.getInstance(getAppContext());
    }

    /**
     * 发送GET请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param url 访问链接，a URL String
     * @param l   回执线程的处理接口
     */
    public void get(String url, Listener l) {
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
    public void get(String url, Map<String, String> params, Listener l) {
        request(METHOD_GET, url, params, l);
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
    public void get(String url, String params, Listener l) {
        request(METHOD_GET, url, params, l);
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
    public void post(String url, Map<String, String> params, Listener l) {
        request(METHOD_POST, url, params, l);
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
    public void post(String url, String params, Listener l) {
        request(METHOD_POST, url, params, l);
    }

    /**
     * 发送HTTP请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param method 访问方式，{@link #METHOD_GET}、{@link #METHOD_POST}
     * @param url    访问链接，a URL String
     * @param params 查询串，Map集合，Key => String, Value => String
     * @param l      回执线程的处理接口
     */
    public void request(String method, String url, Map<String, String> params, Listener l) {
        request(method, url, joinString(params), l);
    }

    /**
     * 发送HTTP请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param method 访问方式，{@link #METHOD_GET}、{@link #METHOD_POST}
     * @param url    访问链接，a URL String
     * @param params 查询串，a String, or null
     * @param l      回执线程的处理接口
     */
    public void request(String method, String url, String params, Listener l) {
        int statusCode = 0;

        try {
            HttpURLConnection urlConn = null;
            BufferedOutputStream out = null;
            BufferedInputStream in = null;
            ByteArrayOutputStream data = null;

            method = isPost(method) ? METHOD_POST : METHOD_GET;
            if (isGet(method) && !TextUtils.isEmpty(params)) {
                url += ((url.indexOf('?') == -1) ? '?' : '&');
                url += params;
            }

            try {
                urlConn = getHttpProxy().openConnection(url);
                if (isPost(method)) {
                    urlConn.setDoOutput(true);
                }

                urlConn.setRequestProperty("Connection", "Keep-Alive");
                urlConn.setRequestMethod(method);
                urlConn.setUseCaches(isUseCaches());
                urlConn.setConnectTimeout(getConnectTimeOutMs());
                urlConn.connect();

                if (isPost(method) && !TextUtils.isEmpty(params)) {
                    out = new BufferedOutputStream(urlConn.getOutputStream());
                    out.write(params.getBytes());
                    out.flush();
                    out.close();
                    out = null;
                }

                statusCode = urlConn.getResponseCode();
                if (statusCode == HttpStatus.SC_OK) {
                    in = new BufferedInputStream(urlConn.getInputStream());
                    data = new ByteArrayOutputStream();

                    int size;
                    byte[] bucket = new byte[getBucket()];
                    while ((size = in.read(bucket)) != -1) {
                        if (size > 0) {
                            data.write(bucket, 0, size);
                            data.flush();
                        }
                    }

                    l.onComplete(data.toByteArray());
                } else {
                    in = new BufferedInputStream(urlConn.getErrorStream());
                    data = new ByteArrayOutputStream();

                    int size;
                    byte[] bucket = new byte[getBucket()];
                    while ((size = in.read(bucket)) != -1) {
                        if (size > 0) {
                            data.write(bucket, 0, size);
                            data.flush();
                        }
                    }

                    String errMsg = urlConn.getResponseMessage();
                    Logger.e(Constants.TAG_LOG, TAG + " request() failure, statusCode: " + statusCode + ", url: " + url + ", params: " + params + ", errMsg: " + errMsg);
                    l.onError(statusCode, new Throwable(errMsg), data.toByteArray());
                }
            } finally {
                if (data != null) {
                    data.close();
                }

                if (in != null) {
                    in.close();
                }

                if (out != null) {
                    out.close();
                }

                if (urlConn != null) {
                    urlConn.disconnect();
                }
            }
        } catch (IOException e) {
            Logger.e(Constants.TAG_LOG, TAG + " request() failure, statusCode: " + statusCode + ", url: " + url + ", params: " + params + ", errMsg: " + e.getMessage());
            l.onError(statusCode, e, null);
        }
    }

    /**
     * 将Map集合拼接成字符串
     *
     * @param params Map集合，Key => String, Value => String
     * @return 查询串，a Params String
     */
    public String joinString(Map<String, String> params) {
        return HttpHelper.joinString(params, getCharset());
    }

    /**
     * Encode URL
     *
     * @param url a URL String
     * @return a Encoded URL String
     */
    public String urlEncode(String url) {
        return HttpHelper.urlEncode(url, getCharset());
    }

    /**
     * 判断提交方式是否是GET
     *
     * @param method 提交方式
     * @return Returns True, or False
     */
    public boolean isGet(String method) {
        return METHOD_GET.equalsIgnoreCase(method);
    }

    /**
     * 判断提交方式是否是POST
     *
     * @param method 提交方式
     * @return Returns True, or False
     */
    public boolean isPost(String method) {
        return METHOD_POST.equalsIgnoreCase(method);
    }

    /**
     * 连接超时，单位：毫秒
     *
     * @return 等待时间，单位：毫秒
     */
    public int getConnectTimeOutMs() {
        return mConnectTimeOutMs;
    }

    /**
     * 字符编码
     *
     * @return a String, or null
     */
    public String getCharset() {
        return mCharset;
    }

    /**
     * 分配缓存空间，单位：字节
     *
     * @return 缓存空间，单位：字节
     */
    public int getBucket() {
        return mBucket;
    }

    /**
     * 是否使用缓存
     *
     * @return Returns True, or False
     */
    public boolean isUseCaches() {
        return mUseCaches;
    }

    /**
     * 获取网络连接类
     *
     * @return a HttpProxy Object
     */
    private HttpProxy getHttpProxy() {
        return mHttpProxy;
    }

    /**
     * 获取上下文环境
     *
     * @return an Application Context Object
     */
    public Context getAppContext() {
        return mAppContext;
    }

    /**
     * Listener interface
     * 回调接口
     *
     * @since 1.0
     */
    public interface Listener {
        /**
         * Http请求完成后回调方法
         *
         * @param data HTTP返回数据，a Byte Array
         */
        void onComplete(byte[] data);

        /**
         * 打开URL链接失败或读写文件失败后回调方法
         *
         * @param statusCode HTTP状态码
         * @param tr         失败原因，a Throwable Object
         * @param data       HTTP返回数据，a Byte Array
         */
        void onError(int statusCode, Throwable tr, byte[] data);
    }

    /**
     * Builder final class
     * Build a new {@link Http}.
     *
     * @since 1.0
     */
    public static final class Builder {
        /**
         * 连接超时，单位：毫秒
         */
        private int mConnectTimeOutMs = DEFAULT_CONNECT_TIMEOUT_MS;

        /**
         * 字符编码，默认：UTF-8
         */
        private String mCharset = DEFAULT_CHARSET;

        /**
         * 分配缓存空间，单位：字节
         */
        private int mBucket = DEFAULT_BUCKET;

        /**
         * 是否使用缓存
         */
        private boolean mUseCaches = DEFAULT_USE_CACHES;

        /**
         * 上下文环境
         */
        private final Context mAppContext;

        /**
         * 构造方法：初始化上下文环境
         *
         * @param c 上下文环境
         */
        public Builder(Context c) {
            mAppContext = c.getApplicationContext();
        }

        /**
         * 创建Http对象
         *
         * @return a Http Object
         */
        public Http create() {
            Http http = new Http(mAppContext);

            http.mConnectTimeOutMs = mConnectTimeOutMs;
            http.mCharset = mCharset;
            http.mBucket = mBucket;
            http.mUseCaches = mUseCaches;

            return http;
        }

        /**
         * 设置连接超时，单位：毫秒
         *
         * @param connectTimeOutMs 超时时间，默认：{@link #DEFAULT_CONNECT_TIMEOUT_MS}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setConnectTimeOutMs(int connectTimeOutMs) {
            mConnectTimeOutMs = connectTimeOutMs;
            return this;
        }

        /**
         * 设置字符编码，默认：UTF-8
         *
         * @param charset 字符编码，默认：{@link #CHARSET_UTF8}，可选：{@link #CHARSET_GBK}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCharset(String charset) {
            mCharset = charset;
            return this;
        }

        /**
         * 设置分配缓存空间，单位：字节
         *
         * @param bucket 缓存空间，默认：{@link #DEFAULT_BUCKET}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBucket(int bucket) {
            mBucket = bucket;
            return this;
        }

        /**
         * 设置是否使用缓存
         *
         * @param useCaches 是否使用缓存，默认：{@link #DEFAULT_USE_CACHES}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setUseCaches(boolean useCaches) {
            mUseCaches = useCaches;
            return this;
        }

    }

}
