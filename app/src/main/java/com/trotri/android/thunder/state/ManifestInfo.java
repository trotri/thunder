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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * ManifestInfo class file
 * Manifest信息类
 * 获取AndroidManifest.xml中信息
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ManifestInfo.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class ManifestInfo {

    public static final String TAG = "ManifestInfo";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static ManifestInfo sInstance;

    /**
     * App名，Application android:label
     */
    private String mAppName;

    /**
     * App信息类
     */
    private final ApplicationInfo mAppInfo;

    /**
     * 版本信息类
     */
    private final Version mVersion;

    /**
     * MetaData数据类
     */
    private final MetaData mMetaData;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、App信息类、版本信息类、MetaData数据类
     */
    private ManifestInfo(Context c) {
        mAppContext = c.getApplicationContext();
        mAppInfo = getAppContext().getApplicationInfo();
        mVersion = Version.getInstance(getAppContext());
        mMetaData = MetaData.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static ManifestInfo getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new ManifestInfo(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取App名
     *
     * @return App名，Application android:label
     */
    public String getAppName() {
        if (mAppName == null) {
            CharSequence label = getAppInfo().loadLabel(getPkgManager());
            if (label != null) {
                mAppName = label.toString();
            }
        }

        return mAppName;
    }

    /**
     * 获取包名
     *
     * @return 包名
     */
    public String getPkgName() {
        return getVersion().getPkgName();
    }

    /**
     * 获取版本号
     *
     * @return 如果获取成功返回 > 0，否则返回0
     */
    public int getVersionCode() {
        return getVersion().getCode();
    }

    /**
     * 获取版本名
     *
     * @return 如果获取成功返回非空字符串，否则返回空字符串
     */
    public String getVersionName() {
        return getVersion().getName();
    }

    /**
     * 获取包管理器
     *
     * @return 包管理器，a PackageManager Object
     */
    public PackageManager getPkgManager() {
        return getVersion().getPkgManager();
    }

    /**
     * 获取App信息类
     *
     * @return App信息类，an ApplicationInfo Object
     */
    public ApplicationInfo getAppInfo() {
        return mAppInfo;
    }

    /**
     * 获取版本信息类
     *
     * @return 版本信息类，a Version Object
     */
    public Version getVersion() {
        return mVersion;
    }

    /**
     * 获取MetaData数据类
     *
     * @return MetaData数据类，a MetaData Object
     */
    public MetaData getMetaData() {
        return mMetaData;
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
