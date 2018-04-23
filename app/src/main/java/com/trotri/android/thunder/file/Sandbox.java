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

package com.trotri.android.thunder.file;

import android.content.Context;

import com.trotri.android.thunder.ap.ThreadPool;
import com.trotri.android.thunder.ap.UiThread;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Sandbox class file
 * 应用沙盒目录中文件读写类
 * 沙盒目录：/data/data/<package name>/files
 * 沙盒目录可阻止其他应用的访问，甚至是其他用户的私自窥探（如果设备被root了，则用户可以随意获取任何数据）
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Sandbox.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Sandbox {

    public static final String TAG = "Sandbox";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static Sandbox sInstance;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境
     */
    private Sandbox(Context c) {
        mAppContext = c.getApplicationContext();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static Sandbox getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new Sandbox(c);
            }

            return sInstance;
        }
    }

    /**
     * 读取文件
     * 在后台线程中读取文件，执行完后，在主线程中回调Listener接口
     *
     * @param fileName 文件名，不需要路径
     * @param l        回执线程的处理接口
     */
    public void read(String fileName, Listener l) {
        ThreadPool.execute(new AsyncCommand(fileName, l));
    }

    /**
     * 写入文件
     * 在后台线程中写入文件，执行完后，在主线程中回调Listener接口
     *
     * @param fileName 文件名，不需要路径
     * @param data     需要写入或追加的数据
     * @param append   操作模式，如果文件已存在，False：覆盖原文件内容、True：往文件中追加内容
     * @param l        回执线程的处理接口
     */
    public void write(String fileName, String data, boolean append, Listener l) {
        ThreadPool.execute(new AsyncCommand(fileName, data, append, l));
    }

    /**
     * 删除文件和目录（递归方式）
     * 在后台线程中删除文件，执行完后，在主线程中回调Listener接口
     *
     * @param fileName 文件或目录名，不需要路径
     * @param l        回执线程的处理接口
     */
    public void remove(String fileName, Listener l) {
        ThreadPool.execute(new AsyncCommand(fileName, l, true));
    }

    /**
     * 读取文件
     *
     * @param fileName 文件名，不需要路径
     * @return 文件内容，或null
     * @throws NullPointerException 如果参数为null，抛出异常
     * @throws IOException          如果读取文件失败，抛出异常
     */
    public String read(String fileName) throws NullPointerException, IOException {
        FileInputStream in = openFileInput(fileName);
        return FileHelper.read(in);
    }

    /**
     * 写入文件
     *
     * @param fileName 文件名，不需要路径
     * @param data     需要写入或追加的数据
     * @param append   操作模式，如果文件已存在，False：覆盖原文件内容、True：往文件中追加内容
     * @throws NullPointerException 如果参数为null，抛出异常
     * @throws IOException          如果写入文件失败，抛出异常
     */
    public void write(String fileName, String data, boolean append) throws NullPointerException, IOException {
        FileOutputStream out = openFileOutput(fileName, append);
        FileHelper.write(out, data);
    }

    /**
     * 删除文件和目录（递归方式）
     *
     * @param fileName 文件或目录名，不需要路径
     * @return Returns True, or False
     * @throws NullPointerException  如果参数为null，抛出异常
     * @throws FileNotFoundException 如果沙盒不存在，抛出异常
     */
    public boolean remove(String fileName) throws NullPointerException, FileNotFoundException {
        fileName = FileHelper.sandboxFileName(getAppContext(), fileName);
        return FileHelper.remove(fileName);
    }

    /**
     * 打开文件输入流
     *
     * @param fileName 文件名，不需要路径
     * @return 文件输入流，a FileInputStream Object
     * @throws FileNotFoundException 如果打开失败，抛出异常
     */
    public FileInputStream openFileInput(String fileName) throws FileNotFoundException {
        return getAppContext().openFileInput(fileName);
    }

    /**
     * 打开文件输出流
     *
     * @param fileName 文件名，不需要路径
     * @param append   操作模式，如果文件已存在，False：覆盖原文件内容、True：往文件中追加内容
     * @return 文件输出流，a FileOutputStream Object
     * @throws FileNotFoundException 如果打开失败，抛出异常
     */
    public FileOutputStream openFileOutput(String fileName, boolean append) throws FileNotFoundException {
        return getAppContext().openFileOutput(fileName, (append ? Context.MODE_APPEND : Context.MODE_PRIVATE));
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
     * 【后台线程】执行命令，发送操作请求
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.Sandbox$AsyncCommand { *; }
     * </p>
     */
    class AsyncCommand extends ThreadPool.AbstractCommand {
        /**
         * 操作类型：读取文件
         */
        public static final int TYPE_READ = 1;

        /**
         * 操作类型：写入文件
         */
        public static final int TYPE_WRITE = 2;

        /**
         * 操作类型：删除文件和目录
         */
        public static final int TYPE_REMOVE = 3;

        /**
         * 文件名，不需要路径
         */
        private String mFileName;

        /**
         * 需要写入或追加的数据
         */
        private String mData;

        /**
         * 操作模式，如果文件已存在，False：覆盖原文件内容、True：往文件中追加内容
         */
        private boolean mAppend;

        /**
         * 操作类型，1：读取文件、2：写入文件、3：删除文件和目录
         */
        private int mType;

        /**
         * 回执线程的处理接口
         */
        private Listener mListener;

        /**
         * 构造方法：初始化文件名、回执线程的处理接口
         *
         * @param fileName 文件名，不需要路径
         * @param l        回执线程的处理接口
         */
        public AsyncCommand(String fileName, Listener l) {
            this(fileName, l, false);
        }

        /**
         * 构造方法：初始化文件名、回执线程的处理接口、是否是删除文件和目录
         *
         * @param fileName 文件名，不需要路径
         * @param l        回执线程的处理接口
         * @param remove   是否是删除文件和目录
         */
        public AsyncCommand(String fileName, Listener l, boolean remove) {
            mType = remove ? TYPE_REMOVE : TYPE_READ;

            mFileName = fileName;
            mListener = l;
        }

        /**
         * 构造方法：初始化文件名、需要写入或追加的数据、操作模式、回执线程的处理接口
         *
         * @param fileName 文件名，不需要路径
         * @param data     需要写入或追加的数据
         * @param append   操作模式，如果文件已存在，False：覆盖原文件内容、True：往文件中追加内容
         * @param l        回执线程的处理接口
         */
        public AsyncCommand(String fileName, String data, boolean append, Listener l) {
            mType = TYPE_WRITE;

            mFileName = fileName;
            mData = data;
            mAppend = append;
            mListener = l;
        }

        @Override
        public void exec() {
            try {
                onExecute();
            } catch (NullPointerException | IOException e) {
                UiThread.exec(new UiCommand(e, mListener));
            }
        }

        /**
         * 执行操作
         */
        void onExecute() throws NullPointerException, IOException {
            switch (mType) {
                case TYPE_READ:
                    String data = read(mFileName);
                    if (data == null) {
                        UiThread.exec(new UiCommand(new Throwable("File Read Failure, fileName: " + mFileName), mListener));
                    } else {
                        UiThread.exec(new UiCommand(data, mListener));
                    }

                    break;
                case TYPE_WRITE:
                    write(mFileName, mData, mAppend);
                    UiThread.exec(new UiCommand("", mListener));
                    break;
                case TYPE_REMOVE:
                    boolean result = remove(mFileName);
                    if (result) {
                        UiThread.exec(new UiCommand("", mListener));
                    } else {
                        UiThread.exec(new UiCommand(new Throwable("File Remove Failure, fileName: " + mFileName), mListener));
                    }

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 【主线程】执行命令，通知操作结果
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.Sandbox$UiCommand { *; }
     * </p>
     */
    class UiCommand extends UiThread.AbstractCommand {
        /**
         * 结果类型：操作完成
         */
        public static final int TYPE_COMPLETE = 1;

        /**
         * 结果类型：操作失败
         */
        public static final int TYPE_ERROR = 2;

        /**
         * 文件内容（读取文件）、""（写入文件和删除文件）
         */
        private String mData;

        /**
         * 失败原因，a Throwable Object
         */
        private Throwable mThr;

        /**
         * 结果类型，1：操作完成、2：操作失败
         */
        private int mType;

        /**
         * 回执线程的处理接口
         */
        private Listener mListener;

        /**
         * 构造方法：初始化文件内容、回执线程的处理接口
         *
         * @param data 文件内容（读取文件）、""（写入文件和删除文件）
         * @param l    回执线程的处理接口
         */
        public UiCommand(String data, Listener l) {
            mType = TYPE_COMPLETE;

            mData = data;
            mListener = l;
        }

        /**
         * 构造方法：初始化失败原因、回执线程的处理接口
         *
         * @param tr 失败原因，a Throwable Object
         * @param l  回执线程的处理接口
         */
        public UiCommand(Throwable tr, Listener l) {
            mType = TYPE_ERROR;

            mThr = tr;
            mListener = l;
        }

        @Override
        public void exec() {
            switch (mType) {
                case TYPE_COMPLETE:
                    mListener.onComplete(mData);
                    break;
                case TYPE_ERROR:
                    mListener.onError(mThr);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Listener interface
     * 回调接口
     *
     * @since 1.0
     */
    public interface Listener {
        /**
         * 执行完成后回调方法
         *
         * @param data 文件内容（读取文件）、""（写入文件和删除文件）
         */
        void onComplete(String data);

        /**
         * 打开或操作文件失败后回调方法
         *
         * @param tr 失败原因，a Throwable Object
         */
        void onError(Throwable tr);
    }

}
