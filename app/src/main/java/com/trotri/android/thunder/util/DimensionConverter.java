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

package com.trotri.android.thunder.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * DimensionConverter class file
 * 单位转换类，Dp to Px、Px to Dp、Sp to Px、Px to Sp
 * 精确获取需要在主Activity的onWindowFocusChanged(boolean hasFocus)中调用
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DimensionConverter.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class DimensionConverter {

    public static final String TAG = "DimensionConverter";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static DimensionConverter sInstance;

    /**
     * 屏幕信息描述器
     */
    private final DisplayMetrics mDisplayMetrics;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、屏幕信息描述器
     */
    private DimensionConverter(Context c) {
        mAppContext = c.getApplicationContext();
        mDisplayMetrics = getAppContext().getResources().getDisplayMetrics();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static DimensionConverter getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new DimensionConverter(c);
            }

            return sInstance;
        }
    }

    /**
     * Device Independent Pixels 转 Pixels
     *
     * @param value Device Independent Pixels
     * @return a Pixels
     */
    public float dp2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getDisplayMetrics());
    }

    /**
     * Pixels 转 Device Independent Pixels
     *
     * @param value Pixels
     * @return a Device Independent Pixels, or -1
     */
    public float px2dp(float value) {
        float scale = getDisplayMetrics().density;
        return (scale > 0) ? (value / scale) : -1;
    }

    /**
     * Scaled Pixels 转 Pixels
     *
     * @param value Scaled Pixels
     * @return a Pixels
     */
    public float sp2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, getDisplayMetrics());
    }

    /**
     * Pixels 转 Scaled Pixels
     *
     * @param value Pixels
     * @return a Scaled Pixels, or -1
     */
    public float px2sp(float value) {
        float scale = getDisplayMetrics().scaledDensity;
        return (scale > 0) ? (value / scale) : -1;
    }

    /**
     * 获取屏幕信息描述器
     *
     * @return a DisplayMetrics Object
     */
    public DisplayMetrics getDisplayMetrics() {
        return mDisplayMetrics;
    }

    /**
     * 获取上下文环境
     *
     * @return an Application Context Object
     */
    public Context getAppContext() {
        return mAppContext;
    }

}
