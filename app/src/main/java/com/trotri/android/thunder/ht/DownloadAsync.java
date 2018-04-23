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

/**
 * DownloadAsync class file
 * 下载异步类，在后台线程中执行下载，执行完后，在主线程中回调Listener接口
 * 需要权限：
 * <uses-permission android:name="android.permission.INTERNET" />
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DownloadAsync.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class DownloadAsync {

    private static final String TAG = "DownloadAsync";

    /**
     * 下载类
     */
    private final Download mDownload;

    /**
     * 构造方法：初始化下载类
     *
     * @param download 下载类
     */
    public DownloadAsync(Download download) {
        mDownload = download;
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
    public void exec(String url, String fileName, Download.Listener l) {
        ThreadPool.execute(new AsyncCommand(url, fileName, l));
    }

    /**
     * 【后台线程】执行命令，发送下载请求
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.DownloadAsync$AsyncCommand { *; }
     * </p>
     */
    class AsyncCommand extends ThreadPool.AbstractCommand {
        /**
         * 访问链接，a URL String
         */
        private String mUrl;

        /**
         * 文件名，包括目录路径
         */
        private String mFileName;

        /**
         * 回执线程的处理接口
         */
        private Download.Listener mListener;

        /**
         * 构造方法：初始化访问链接、文件名、回执线程的处理接口
         *
         * @param url      访问链接，a URL String
         * @param fileName 文件名，包括目录路径
         * @param l        回执线程的处理接口
         */
        public AsyncCommand(String url, String fileName, Download.Listener l) {
            mUrl = url;
            mFileName = fileName;
            mListener = l;
        }

        @Override
        public void exec() {
            mDownload.exec(mUrl, mFileName, new Download.Listener() {
                @Override
                public void onComplete(long totalSize) {
                    UiThread.exec(new UiCommand(totalSize, mListener));
                }

                @Override
                public void onPartial(long totalSize, long downloadSize) {
                    UiThread.exec(new UiCommand(totalSize, downloadSize, mListener));
                }

                @Override
                public void onError(int statusCode, Throwable tr) {
                    UiThread.exec(new UiCommand(statusCode, tr, mListener));
                }
            });
        }

    }

    /**
     * 【主线程】执行命令，通知下载请求结果
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.DownloadAsync$UiCommand { *; }
     * </p>
     */
    class UiCommand extends UiThread.AbstractCommand {
        /**
         * 结果类型：下载完成
         */
        public static final int TYPE_COMPLETE = 1;

        /**
         * 结果类型：部分文件下载完成
         */
        public static final int TYPE_PARTIAL = 2;

        /**
         * 结果类型：打开URL链接失败或读写文件失败
         */
        public static final int TYPE_ERROR = 3;

        /**
         * 下载文件总大小，单位：字节
         */
        private long mTotalSize;

        /**
         * 已下载文件大小，单位：字节
         */
        private long mDownloadSize;

        /**
         * HTTP状态码
         */
        private int mStatusCode;

        /**
         * 失败原因，a Throwable Object
         */
        private Throwable mThr;

        /**
         * 结果类型，1：下载完成、2：部分文件下载完成、3：打开URL链接失败或读写文件失败
         */
        private int mType;

        /**
         * 回执线程的处理接口
         */
        private Download.Listener mListener;

        /**
         * 构造方法：初始化下载文件总大小、回执线程的处理接口
         *
         * @param totalSize 下载文件总大小，单位：字节
         * @param l         回执线程的处理接口
         */
        public UiCommand(long totalSize, Download.Listener l) {
            mType = TYPE_COMPLETE;

            mTotalSize = totalSize;
            mListener = l;
        }

        /**
         * 构造方法：初始化下载文件总大小、已下载文件大小、回执线程的处理接口
         *
         * @param totalSize    下载文件总大小，单位：字节
         * @param downloadSize 已下载文件大小，单位：字节
         * @param l            回执线程的处理接口
         */
        public UiCommand(long totalSize, long downloadSize, Download.Listener l) {
            mType = TYPE_PARTIAL;

            mTotalSize = totalSize;
            mDownloadSize = downloadSize;
            mListener = l;
        }

        /**
         * 构造方法：初始化HTTP状态码、失败原因、回执线程的处理接口
         *
         * @param statusCode HTTP状态码
         * @param tr         失败原因，a Throwable Object
         * @param l          回执线程的处理接口
         */
        public UiCommand(int statusCode, Throwable tr, Download.Listener l) {
            mType = TYPE_ERROR;

            mStatusCode = statusCode;
            mThr = tr;
            mListener = l;
        }

        @Override
        public void exec() {
            switch (mType) {
                case TYPE_COMPLETE:
                    mListener.onComplete(mTotalSize);
                    break;
                case TYPE_PARTIAL:
                    mListener.onPartial(mTotalSize, mDownloadSize);
                    break;
                case TYPE_ERROR:
                    mListener.onError(mStatusCode, mThr);
                    break;
                default:
                    break;
            }
        }

    }

}
