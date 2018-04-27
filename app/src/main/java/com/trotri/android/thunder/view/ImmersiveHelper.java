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

package com.trotri.android.thunder.view;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.trotri.android.thunder.state.DisplayPixels;

/**
 * ImmersiveHelper class file
 * 沉浸辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ImmersiveHelper.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class ImmersiveHelper {

    public static final String TAG = "ImmersiveHelper";

    /**
     * 设置沉浸
     *
     * @param context an Activity Object
     * @return Returns True (Support Immersive), or False
     */
    public static boolean setImmersive(Activity context) {
        if (!isSupported(context)) {
            return false;
        }

        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        return true;
    }

    /**
     * 重设标题栏高度
     *
     * @param c    上下文环境
     * @param view 标题栏布局
     * @return Returns True (Support Immersive), or False
     */
    public static boolean setTitleBar(Context c, FrameLayout view) {
        if (!isSupported(c)) {
            return false;
        }

        float statusBarHeight = getStatusBarHeight(c);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.height += statusBarHeight;
        view.setLayoutParams(layoutParams);

        view.setPadding(0, (int) statusBarHeight, 0, 0);
        return true;
    }

    /**
     * 是否支持沉浸
     * SDK_INT >= KITKAT 并且 Status Bar Height > 0
     *
     * @param c 上下文环境
     * @return Returns True, or False
     */
    public static boolean isSupported(Context c) {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) && (getStatusBarHeight(c) > 0);
    }

    /**
     * 获取状态栏高度
     *
     * @param c 上下文环境
     * @return 状态栏高度，单位：像素
     */
    public static float getStatusBarHeight(Context c) {
        return DisplayPixels.getInstance(c).getStatusBarHeight();
    }

}
