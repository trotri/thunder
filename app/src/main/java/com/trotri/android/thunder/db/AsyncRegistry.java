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

package com.trotri.android.thunder.db;

import com.trotri.android.thunder.ap.ThreadPool;
import com.trotri.android.thunder.ap.TypeCast;
import com.trotri.android.thunder.ap.UiThread;

/**
 * AsyncRegistry class file
 * 异步全局数据寄存类，在后台线程中执行存储，执行完后，在主线程中回调Listener接口
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: AsyncRegistry.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class AsyncRegistry {

    public static final String TAG = "AsyncRegistry";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static AsyncRegistry sInstance;

    /**
     * Registry类
     */
    private final Registry mRegistry;

    /**
     * 构造方法：初始化Registry类
     *
     * @param registry Registry类
     */
    private AsyncRegistry(Registry registry) {
        mRegistry = registry;
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static AsyncRegistry getInstance(Registry registry) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new AsyncRegistry(registry);
            }

            return sInstance;
        }
    }

    /**
     * 通过名称获取整数值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @param l            回执线程的处理接口
     */
    public void getInt(String key, final int defaultValue, final Listener<Integer> l) {
        getString(key, null, new Listener<String>() {
            @Override
            public void onComplete(String value) {
                int result = TypeCast.toInt(value, defaultValue);
                l.onComplete(result);
            }
        });
    }

    /**
     * 设置名称和整数值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @param l     回执线程的处理接口
     */
    public void putInt(String key, int value, Listener<Boolean> l) {
        putString(key, String.valueOf(value), l);
    }

    /**
     * 通过名称获取长整数值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @param l            回执线程的处理接口
     */
    public void getLong(String key, final long defaultValue, final Listener<Long> l) {
        getString(key, null, new Listener<String>() {
            @Override
            public void onComplete(String value) {
                long result = TypeCast.toLong(value, defaultValue);
                l.onComplete(result);
            }
        });
    }

    /**
     * 设置名称和长整数值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @param l     回执线程的处理接口
     */
    public void putLong(String key, long value, Listener<Boolean> l) {
        putString(key, String.valueOf(value), l);
    }

    /**
     * 通过名称获取浮点值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @param l            回执线程的处理接口
     */
    public void getFloat(String key, final float defaultValue, final Listener<Float> l) {
        getString(key, null, new Listener<String>() {
            @Override
            public void onComplete(String value) {
                float result = TypeCast.toFloat(value, defaultValue);
                l.onComplete(result);
            }
        });
    }

    /**
     * 设置名称和浮点值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @param l     回执线程的处理接口
     */
    public void putFloat(String key, float value, Listener<Boolean> l) {
        putString(key, String.valueOf(value), l);
    }

    /**
     * 通过名称获取布尔值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @param l            回执线程的处理接口
     */
    public void getBoolean(String key, final boolean defaultValue, final Listener<Boolean> l) {
        getInt(key, (defaultValue ? 1 : 0), new Listener<Integer>() {
            @Override
            public void onComplete(Integer value) {
                l.onComplete(value == 1);
            }
        });
    }

    /**
     * 设置名称和布尔值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @param l     回执线程的处理接口
     */
    public void putBoolean(String key, boolean value, Listener<Boolean> l) {
        putInt(key, (value ? 1 : 0), l);
    }

    /**
     * 通过名称获取字符串值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @param l            回执线程的处理接口
     */
    public void getString(String key, String defaultValue, Listener<String> l) {
        ThreadPool.execute(new AsyncReadCommand(key, defaultValue, l));
    }

    /**
     * 设置名称和字符串值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @param l     回执线程的处理接口
     */
    public void putString(String key, String value, Listener<Boolean> l) {
        ThreadPool.execute(new AsyncCommand(key, value, l));
    }

    /**
     * 通过名称删除数据
     *
     * @param key The name of the preference to remove.
     * @param l   回执线程的处理接口
     */
    public void remove(String key, Listener<Boolean> l) {
        ThreadPool.execute(new AsyncCommand(key, l));
    }

    /**
     * 【后台线程】执行命令，读取数据
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.AsyncRegistry$AsyncReadCommand { *; }
     * </p>
     */
    class AsyncReadCommand extends ThreadPool.AbstractCommand {
        /**
         * The name of the preference to retrieve.
         */
        private String mKey;

        /**
         * Value to return if this preference does not exist.
         */
        private String mDefaultValue;

        /**
         * 回执线程的处理接口
         */
        private Listener<String> mListener;

        /**
         * 构造方法：初始化键名、默认值、回执线程的处理接口
         *
         * @param key          The name of the preference to retrieve.
         * @param defaultValue Value to return if this preference does not exist.
         * @param l            回执线程的处理接口
         */
        public AsyncReadCommand(String key, String defaultValue, Listener<String> l) {
            mKey = key;
            mDefaultValue = defaultValue;
            mListener = l;
        }

        @Override
        public void exec() {
            String value = mRegistry.getString(mKey, mDefaultValue);
            UiThread.exec(new UiCommand<>(value, mListener));
        }

    }

    /**
     * 【后台线程】执行命令，设置和删除数据
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.AsyncRegistry$AsyncCommand { *; }
     * </p>
     */
    class AsyncCommand extends ThreadPool.AbstractCommand {
        /**
         * 结果类型：设置数据
         */
        public static final int TYPE_PUT = 1;

        /**
         * 结果类型：删除数据
         */
        public static final int TYPE_REMOVE = 2;

        /**
         * The name of the preference to retrieve.
         */
        private String mKey;

        /**
         * The new value for the preference.
         */
        private String mValue;

        /**
         * 回执线程的处理接口
         */
        private Listener<Boolean> mListener;

        /**
         * 结果类型，1：设置数据、2：删除数据
         */
        private int mType;

        /**
         * 构造方法：初始化键名、新值、回执线程的处理接口
         *
         * @param key   The name of the preference to modify.
         * @param value The new value for the preference.
         * @param l     回执线程的处理接口
         */
        public AsyncCommand(String key, String value, Listener<Boolean> l) {
            mType = TYPE_PUT;

            mKey = key;
            mValue = value;
            mListener = l;
        }

        /**
         * 构造方法：初始化键名、回执线程的处理接口
         *
         * @param key The name of the preference to remove.
         * @param l   回执线程的处理接口
         */
        public AsyncCommand(String key, Listener<Boolean> l) {
            mType = TYPE_REMOVE;

            mKey = key;
            mListener = l;
        }

        @Override
        public void exec() {
            boolean result = false;

            switch (mType) {
                case TYPE_PUT:
                    result = mRegistry.putString(mKey, mValue);
                    break;
                case TYPE_REMOVE:
                    result = mRegistry.remove(mKey);
                    break;
                default:
                    break;
            }

            UiThread.exec(new UiCommand<>(result, mListener));
        }

    }

    /**
     * 【主线程】执行命令，通知执行结果
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.AsyncRegistry$UiCommand { *; }
     * </p>
     */
    class UiCommand<T> extends UiThread.AbstractCommand {
        /**
         * 结果
         */
        private T mResult;

        /**
         * 回执线程的处理接口
         */
        private Listener<T> mListener;

        /**
         * 构造方法：初始化结果、回执线程的处理接口
         *
         * @param result 结果
         * @param l      回执线程的处理接口
         */
        public UiCommand(T result, Listener<T> l) {
            mResult = result;
            mListener = l;
        }

        @Override
        public void exec() {
            mListener.onComplete(mResult);
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
         * 执行完成后回调方法
         *
         * @param value 数据（读取）、boolean（设置和删除）
         */
        void onComplete(T value);
    }

}
