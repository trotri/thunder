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

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.HttpStatus;
import com.trotri.android.thunder.ap.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Download class file
 * 下载类
 * 需要权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 * 注：主线程里执行Http请求会报android.os.NetworkOnMainThreadException异常
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Download.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Download {

    public static final String TAG = "Download";

    /**
     * 默认的连接超时，单位：毫秒
     */
    public static final int DEFAULT_CONNECT_TIMEOUT_MS = 30 * 1000;

    /**
     * 默认的分配缓存空间，单位：字节
     */
    public static final int DEFAULT_BUCKET = 4096;

    /**
     * 连接超时，单位：毫秒
     */
    private int mConnectTimeOutMs = DEFAULT_CONNECT_TIMEOUT_MS;

    /**
     * 分配缓存空间，单位：字节
     */
    private int mBucket = DEFAULT_BUCKET;

    /**
     * 网络连接类
     */
    private final HttpProxy mHttpProxy;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、网络连接类
     */
    private Download(Context c) {
        mAppContext = c.getApplicationContext();
        mHttpProxy = HttpProxy.getInstance(getAppContext());
    }

    /**
     * 发送下载请求
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param url      访问链接，a URL String
     * @param fileName 文件名，包括目录路径
     * @param l        回执线程的处理接口
     */
    public void exec(String url, String fileName, Listener l) {
        int statusCode = 0;

        try {
            HttpURLConnection urlConn = null;
            BufferedInputStream in = null;
            BufferedOutputStream out = null;

            try {
                urlConn = getHttpProxy().openConnection(url);
                urlConn.setRequestProperty("Connection", "Keep-Alive");
                urlConn.setRequestMethod(Http.METHOD_GET);
                urlConn.setConnectTimeout(getConnectTimeOutMs());
                urlConn.connect();

                long totalSize, downloadSize = 0;
                statusCode = urlConn.getResponseCode();
                if (statusCode == HttpStatus.SC_OK) {
                    totalSize = urlConn.getContentLength();

                    in = new BufferedInputStream(urlConn.getInputStream());
                    out = new BufferedOutputStream(new FileOutputStream(new File(fileName)));

                    int size;
                    byte[] bucket = new byte[getBucket()];
                    while ((size = in.read(bucket)) != -1) {
                        if (size > 0) {
                            out.write(bucket, 0, size);
                            out.flush();

                            downloadSize += size;
                        }
                    }

                    if (downloadSize < totalSize) {
                        l.onPartial(totalSize, downloadSize);
                    } else {
                        l.onComplete(downloadSize);
                    }
                } else {
                    String errMsg = urlConn.getResponseMessage();
                    Logger.e(Constants.TAG_LOG, TAG + " exec() failure, statusCode: " + statusCode + ", url: " + url + ", fileName: " + fileName + ", errMsg: " + errMsg);
                    l.onError(statusCode, new Throwable(errMsg));
                }
            } finally {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }

                if (urlConn != null) {
                    urlConn.disconnect();
                }
            }
        } catch (IOException e) {
            Logger.e(Constants.TAG_LOG, TAG + " exec() failure, statusCode: " + statusCode + ", url: " + url + ", fileName: " + fileName + ", errMsg: " + e.getMessage());
            l.onError(statusCode, e);
        }
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
     * 分配缓存空间，单位：字节
     *
     * @return 缓存空间，单位：字节
     */
    public int getBucket() {
        return mBucket;
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
         * 下载完成后回调方法
         *
         * @param totalSize 下载文件总大小，单位：字节
         */
        void onComplete(long totalSize);

        /**
         * 部分文件下载完成后回调方法
         *
         * @param totalSize    下载文件总大小，单位：字节
         * @param downloadSize 已下载文件大小，单位：字节
         */
        void onPartial(long totalSize, long downloadSize);

        /**
         * 打开URL链接失败或读写文件失败后回调方法
         *
         * @param statusCode HTTP状态码
         * @param tr         失败原因，a Throwable Object
         */
        void onError(int statusCode, Throwable tr);
    }

    /**
     * Builder final class
     * Build a new {@link Download}.
     *
     * @since 1.0
     */
    public static final class Builder {
        /**
         * 连接超时，单位：毫秒
         */
        private int mConnectTimeOutMs = DEFAULT_CONNECT_TIMEOUT_MS;

        /**
         * 分配缓存空间，单位：字节
         */
        private int mBucket = DEFAULT_BUCKET;

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
         * 创建下载类对象
         *
         * @return a Download Object
         */
        public Download create() {
            Download download = new Download(mAppContext);

            download.mConnectTimeOutMs = mConnectTimeOutMs;
            download.mBucket = mBucket;

            return download;
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
         * 设置分配缓存空间，单位：字节
         *
         * @param bucket 缓存空间，默认：{@link #DEFAULT_BUCKET}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBucket(int bucket) {
            mBucket = bucket;
            return this;
        }

    }

}
