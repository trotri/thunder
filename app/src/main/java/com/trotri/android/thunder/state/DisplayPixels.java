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

package com.trotri.android.thunder.state;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * DisplayPixels class file
 * 屏幕信息类，宽、高、密度等，单位：像素
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DisplayPixels.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class DisplayPixels {

    public static final String TAG = "DisplayPixels";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static DisplayPixels sInstance;

    /**
     * 屏幕信息描述器
     */
    private final DisplayMetrics mDisplayMetrics;

    /**
     * 屏幕方向信息描述器
     */
    private final Configuration mConfiguration;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、屏幕信息描述器、屏幕方向信息描述器
     */
    private DisplayPixels(Context c) {
        mAppContext = c.getApplicationContext();
        mDisplayMetrics = getAppContext().getResources().getDisplayMetrics();
        mConfiguration = getAppContext().getResources().getConfiguration();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static DisplayPixels getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new DisplayPixels(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取屏幕X轴像素点个数
     *
     * @return 屏幕宽度，单位：像素
     */
    public int getWidth() {
        return getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕Y轴像素点个数
     *
     * @return 屏幕高度，单位：像素
     */
    public int getHeight() {
        return getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕逻辑密度
     * 160 dpi 屏幕（如：240x320, 1.5"x2"），逻辑密度 = 1；120 dpi 屏幕，逻辑密度 = 0.75;
     *
     * @return 逻辑密度
     */
    public float getDensity() {
        return getDisplayMetrics().density;
    }

    /**
     * 获取屏幕每英寸物理像素点的个数
     *
     * @return The screen density expressed as dots-per-inch
     */
    public float getDPI() {
        return getDisplayMetrics().densityDpi;
    }

    /**
     * 获取屏幕X轴每英寸物理像素点的个数
     *
     * @return The exact physical pixels per inch of the screen in the X dimension.
     */
    public float getXDPI() {
        return getDisplayMetrics().xdpi;
    }

    /**
     * 获取屏幕Y轴每英寸物理像素点的个数
     *
     * @return The exact physical pixels per inch of the screen in the Y dimension.
     */
    public float getYDPI() {
        return getDisplayMetrics().ydpi;
    }

    /**
     * 获取屏幕方向，是否横屏
     *
     * @return Returns True, or False
     */
    public boolean isLandscape() {
        return getOrientation() == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 获取屏幕方向，是否竖屏
     *
     * @return Returns True, or False
     */
    public boolean isPortrait() {
        return getOrientation() == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 获取屏幕方向
     *
     * @return 横屏：Configuration.ORIENTATION_LANDSCAPE、竖屏：Configuration.ORIENTATION_PORTRAIT
     */
    public int getOrientation() {
        return mConfiguration.orientation;
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

    /**
     * 测试输出
     *
     * @param c 上下文环境
     */
    public static void log(Context c) {
        if (Constants.DEBUG) {
            DisplayPixels o = DisplayPixels.getInstance(c);
            Logger.d(Constants.TAG_LOG, TAG + " width: " + o.getWidth()
                    + ", height: " + o.getHeight()
                    + ", density: " + o.getDensity()
                    + ", DPI: " + o.getDPI()
                    + ", xDPI: " + o.getXDPI()
                    + ", yDPI: " + o.getYDPI()
                    + ", isLandscape: " + o.isLandscape()
                    + ", isPortrait: " + o.isPortrait()
                    + ", orientation: " + o.getOrientation());
        }
    }

}
