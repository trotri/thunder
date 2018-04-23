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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.ap.SystemService;

/**
 * ClipboardHelper class file
 * 剪贴板辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ClipboardHelper.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class ClipboardHelper {

    public static final String TAG = "ClipboardHelper";

    /**
     * User-visible label for the clip data.
     */
    public static final String CLIP_LABEL = "";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static ClipboardHelper sInstance;

    /**
     * 系统服务管理类
     */
    private final SystemService mSystemService;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、系统服务管理类
     */
    private ClipboardHelper(Context c) {
        mAppContext = c.getApplicationContext();
        mSystemService = SystemService.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static ClipboardHelper getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new ClipboardHelper(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取剪贴板内容
     *
     * @return 剪贴板第一条内容，或者""
     */
    public String getText() {
        ClipboardManager cManager = getSystemService().getClipboardManager();
        if (cManager == null) {
            return "";
        }

        ClipData clip = cManager.getPrimaryClip();
        if (clip == null) {
            return "";
        }

        ClipData.Item item = null;
        try {
            item = clip.getItemAt(0);
        } catch (IndexOutOfBoundsException e) {
            Logger.e(Constants.TAG_LOG, TAG + " getText()", e);
        }

        if (item == null) {
            return "";
        }

        CharSequence text = item.getText();
        return (text == null) ? "" : text.toString();
    }

    /**
     * 向剪贴板设置内容
     *
     * @param text 内容
     */
    public void setText(String text) {
        ClipboardManager cManager = getSystemService().getClipboardManager();
        if (cManager == null) {
            return;
        }

        if (text == null) {
            text = "";
        }

        ClipData clip = ClipData.newPlainText(CLIP_LABEL, text);
        cManager.setPrimaryClip(clip);
    }

    /**
     * 获取系统服务管理类
     *
     * @return a SystemService Object
     */
    public SystemService getSystemService() {
        return mSystemService;
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
