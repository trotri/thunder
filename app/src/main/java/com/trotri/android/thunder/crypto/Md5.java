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

package com.trotri.android.thunder.crypto;

import android.text.TextUtils;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.ap.ThreadPool;
import com.trotri.android.thunder.ap.TypeCast;
import com.trotri.android.thunder.ap.UiThread;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Md5 class file
 * MD5加密类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Md5.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Md5 {

    public static final String TAG = "Md5";

    /**
     * 算法
     */
    public static final String ALGORITHM = "MD5";

    /**
     * 默认的分配缓存空间，单位：字节
     */
    public static final int BUCKET_LEN = 1024;

    /**
     * 消息摘要类
     */
    public static MessageDigest sExecutor;

    /**
     * MD5加密，转为16进制字符串
     *
     * @param value 明文
     * @return 密文，16进制字符串，或""
     */
    public static String toHex(String value) {
        byte[] data = toBytes(value);
        if (data == null) {
            Logger.e(Constants.TAG_LOG, TAG + " toHex() toBytes() data is null");
            return "";
        }

        String result = TypeCast.toHex(data);
        if (result == null) {
            Logger.e(Constants.TAG_LOG, TAG + " toHex() TypeCast.toHex() result is null");
            return "";
        }

        return result;
    }

    /**
     * 文件MD5加密，转为16进制字符串
     * 在后台线程中执行加密，执行完后，在主线程中回调Listener接口
     *
     * @param f 文件对象，a File Object
     * @param l 回执线程的处理接口
     */
    public static void toHex(File f, final Listener<String> l) {
        toBytes(f, new Listener<byte[]>() {
            @Override
            public void onComplete(byte[] data) {
                String result = TypeCast.toHex(data);
                if (result == null) {
                    l.onError(new Throwable("TypeCast.toHex() result is null"));
                } else {
                    l.onComplete(result);
                }
            }

            @Override
            public void onError(Throwable tr) {
                l.onError(tr);
            }
        });
    }

    /**
     * 文件MD5加密，转为16进制字符串
     *
     * @param f 文件对象，a File Object
     * @return 密文，16进制字符串，或""
     */
    public static String toHex(File f) {
        byte[] data = null;

        try {
            data = toBytes(f);
        } catch (NullPointerException | ArithmeticException | IOException e) {
            Logger.e(Constants.TAG_LOG, TAG + " toHex()", e);
        }

        if (data == null) {
            Logger.e(Constants.TAG_LOG, TAG + " toHex() data is null");
            return "";
        }

        String result = TypeCast.toHex(data);
        if (result == null) {
            Logger.e(Constants.TAG_LOG, TAG + " toHex() TypeCast.toHex() result is null");
            return "";
        }

        return result;
    }

    /**
     * MD5加密，转为字节数组
     *
     * @param value 明文
     * @return 密文，字节数组，或null
     */
    public static byte[] toBytes(String value) {
        if (TextUtils.isEmpty(value)) {
            Logger.e(Constants.TAG_LOG, TAG + " toBytes() value is null");
            return null;
        }

        byte[] data = TypeCast.toBytes(value);
        if (data == null) {
            Logger.e(Constants.TAG_LOG, TAG + " toBytes() TypeCast.toBytes() data is null");
            return null;
        }

        MessageDigest executor = getExecutor();
        if (executor == null) {
            Logger.e(Constants.TAG_LOG, TAG + " toBytes() Engine Digest Failure, MessageDigest is null");
            return null;
        }

        byte[] result = executor.digest(data);
        if (result == null) {
            Logger.e(Constants.TAG_LOG, TAG + " toBytes() Engine Digest Failure, result is null");
            return null;
        }

        return result;
    }

    /**
     * 文件MD5加密，转为字节数组
     * 在后台线程中执行加密，执行完后，在主线程中回调Listener接口
     *
     * @param f 文件对象，a File Object
     * @param l 回执线程的处理接口
     */
    public static void toBytes(File f, Listener<byte[]> l) {
        ThreadPool.execute(new AsyncCommand(f, l));
    }

    /**
     * 文件MD5加密，转为字节数组
     *
     * @param f 文件对象，a File Object
     * @return 密文，字节数组，或null
     * @throws NullPointerException 如果参数为null，抛出异常
     * @throws ArithmeticException  如果加密失败，抛出异常
     * @throws IOException          如果文件对象为null，抛出异常
     */
    public static byte[] toBytes(File f) throws NullPointerException, ArithmeticException, IOException {
        if (f == null) {
            throw new NullPointerException();
        }

        MessageDigest executor = getExecutor();
        if (executor == null) {
            throw new ArithmeticException("Engine Digest Failure");
        }

        FileInputStream in = null;

        try {
            in = new FileInputStream(f);

            int size;
            byte[] bucket = new byte[BUCKET_LEN];
            while ((size = in.read(bucket)) != -1) {
                if (size > 0) {
                    executor.update(bucket, 0, size);
                }
            }

            byte[] result = executor.digest();
            if (result == null) {
                throw new ArithmeticException("Engine Digest Failure");
            }

            return result;
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * 获取消息摘要类
     *
     * @return a MessageDigest Object, or null
     */
    public static MessageDigest getExecutor() {
        if (sExecutor == null) {
            try {
                sExecutor = MessageDigest.getInstance(ALGORITHM);
            } catch (NoSuchAlgorithmException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getExecutor()", e);
            }
        }

        return sExecutor;
    }

    /**
     * 【后台线程】执行命令，发送加密请求
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.Md5$AsyncCommand { *; }
     * </p>
     */
    static class AsyncCommand extends ThreadPool.AbstractCommand {
        /**
         * 文件对象，a File Object
         */
        private File mFile;

        /**
         * 回执线程的处理接口
         */
        private Listener<byte[]> mListener;

        /**
         * 构造方法：初始化文件对象、回执线程的处理接口
         *
         * @param f 文件对象，a File Object
         * @param l 回执线程的处理接口
         */
        public AsyncCommand(File f, Listener<byte[]> l) {
            mFile = f;
            mListener = l;
        }

        @Override
        public void exec() {
            try {
                byte[] data = toBytes(mFile);
                if (data == null) {
                    UiThread.exec(new UiCommand(new Throwable("Engine Digest Failure"), mListener));
                } else {
                    UiThread.exec(new UiCommand(data, mListener));
                }
            } catch (IOException e) {
                UiThread.exec(new UiCommand(e, mListener));
            }
        }
    }

    /**
     * 【主线程】执行命令，通知加密结果
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.Md5$UiCommand { *; }
     * </p>
     */
    static class UiCommand extends UiThread.AbstractCommand {
        /**
         * 结果类型：加密完成
         */
        public static final int TYPE_COMPLETE = 1;

        /**
         * 结果类型：加密失败
         */
        public static final int TYPE_ERROR = 2;

        /**
         * 密文，字节数组
         */
        private byte[] mData;

        /**
         * 失败原因，a Throwable Object
         */
        private Throwable mThr;

        /**
         * 结果类型，1：加密完成、2：加密失败
         */
        private int mType;

        /**
         * 回执线程的处理接口
         */
        private Listener<byte[]> mListener;

        /**
         * 构造方法：初始化密文、回执线程的处理接口
         *
         * @param data 密文，字节数组
         * @param l    回执线程的处理接口
         */
        public UiCommand(byte[] data, Listener<byte[]> l) {
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
        public UiCommand(Throwable tr, Listener<byte[]> l) {
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
    public interface Listener<T> {
        /**
         * 加密完成后回调方法
         *
         * @param data 密文，字节数组或16进制字符串
         */
        void onComplete(T data);

        /**
         * 加密失败后回调方法
         *
         * @param tr 失败原因，a Throwable Object
         */
        void onError(Throwable tr);
    }

}
