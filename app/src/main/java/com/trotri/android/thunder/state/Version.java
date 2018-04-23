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
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * Version final class file
 * 版本信息类
 * 获取AndroidManifest.xml中manifest:package、android:versionCode、android:versionName
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Version.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public final class Version {

    public static final String TAG = "Version";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static Version sInstance;

    /**
     * 版本号，从1开始
     */
    private int mCode = -1;

    /**
     * 版本名
     */
    private String mName = null;

    /**
     * 包信息类
     */
    private PackageInfo mPkgInfo;

    /**
     * 包名
     */
    private final String mPkgName;

    /**
     * 包管理器类
     */
    private final PackageManager mPkgManager;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、包名、包管理器类
     */
    private Version(Context c) {
        mAppContext = c.getApplicationContext();
        mPkgName = getAppContext().getPackageName();
        mPkgManager = getAppContext().getPackageManager();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static Version getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new Version(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取版本号
     *
     * @return 如果获取成功返回 > 0，否则返回0
     */
    public int getCode() {
        if (mCode < 0) {
            PackageInfo pkgInfo = getPkgInfo();
            if (pkgInfo != null) {
                mCode = pkgInfo.versionCode;
            }

            if (mCode < 0) {
                mCode = 0;
            }
        }

        return mCode;
    }

    /**
     * 获取版本名
     *
     * @return 如果获取成功返回非空字符串，否则返回空字符串
     */
    public String getName() {
        if (mName == null) {
            PackageInfo pkgInfo = getPkgInfo();
            if (pkgInfo != null) {
                mName = pkgInfo.versionName;
            }

            if (mName == null) {
                mName = "";
            }
        }

        return mName;
    }

    /**
     * 获取包信息类
     *
     * @return 包信息类，a PackageInfo Object, or null
     */
    public PackageInfo getPkgInfo() {
        if (mPkgInfo == null) {
            try {
                mPkgInfo = getPkgManager().getPackageInfo(getPkgName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getPkgInfo()", e);
            }
        }

        return mPkgInfo;
    }

    /**
     * 获取包名
     *
     * @return 包名
     */
    public String getPkgName() {
        return mPkgName;
    }

    /**
     * 获取包管理器
     *
     * @return 包管理器，a PackageManager Object
     */
    public PackageManager getPkgManager() {
        return mPkgManager;
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
            Version o = Version.getInstance(c);
            Logger.d(Constants.TAG_LOG, TAG + " pkgName: " + o.getPkgName()
                    + ", code: " + o.getCode()
                    + ", name: " + o.getName());
        }
    }

}
