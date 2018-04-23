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
import android.os.Bundle;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * MetaData final class file
 * MetaData数据类
 * 获取AndroidManifest.xml中<meta-data android:name="" android:value="" />
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: MetaData.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public final class MetaData {

    public static final String TAG = "MetaData";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static MetaData sInstance;

    /**
     * ApplicationInfo.metaData
     */
    private Bundle mData;

    /**
     * App信息类
     */
    private ApplicationInfo mAppInfo;

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
    private MetaData(Context c) {
        mAppContext = c.getApplicationContext();
        mPkgName = getAppContext().getPackageName();
        mPkgManager = getAppContext().getPackageManager();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static MetaData getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new MetaData(c);
            }

            return sInstance;
        }
    }

    /**
     * 通过名称获取整数值
     *
     * @param name         The android:name of the meta-data to retrieve.
     * @param defaultValue Value to return if this meta-data does not exist.
     * @return Returns the android:value if it exists, or defaultValue
     */
    public int getInt(String name, int defaultValue) {
        Bundle data = getData();
        if (data == null) {
            return defaultValue;
        }

        return data.getInt(name, defaultValue);
    }

    /**
     * 通过名称获取长整数值
     *
     * @param name         The android:name of the meta-data to retrieve.
     * @param defaultValue Value to return if this meta-data does not exist.
     * @return Returns the android:value if it exists, or defaultValue
     */
    public long getLong(String name, long defaultValue) {
        Bundle data = getData();
        if (data == null) {
            return defaultValue;
        }

        return data.getLong(name, defaultValue);
    }

    /**
     * 通过名称获取浮点值
     *
     * @param name         The android:name of the meta-data to retrieve.
     * @param defaultValue Value to return if this meta-data does not exist.
     * @return Returns the android:value if it exists, or defaultValue
     */
    public float getFloat(String name, float defaultValue) {
        Bundle data = getData();
        if (data == null) {
            return defaultValue;
        }

        return data.getFloat(name, defaultValue);
    }

    /**
     * 通过名称获取布尔值
     *
     * @param name         The android:name of the meta-data to retrieve.
     * @param defaultValue Value to return if this meta-data does not exist.
     * @return Returns the android:value if it exists, or defaultValue
     */
    public boolean getBoolean(String name, boolean defaultValue) {
        Bundle data = getData();
        if (data == null) {
            return defaultValue;
        }

        return data.getBoolean(name, defaultValue);
    }

    /**
     * 通过名称获取字符串值
     *
     * @param name         The android:name of the meta-data to retrieve.
     * @param defaultValue Value to return if this meta-data does not exist.
     * @return Returns the android:value if it exists, or defaultValue
     */
    public String getString(String name, String defaultValue) {
        Bundle data = getData();
        if (data == null) {
            return defaultValue;
        }

        return data.getString(name, defaultValue);
    }

    /**
     * 通过名称获取对象值
     *
     * @param name The android:name of the meta-data to retrieve.
     * @return Returns the android:value if it exists, or null
     */
    public Object getObject(String name) {
        Bundle data = getData();
        if (data == null) {
            return null;
        }

        return data.get(name);
    }

    /**
     * 获取ApplicationInfo.metaData数据
     *
     * @return AndroidManifest.xml中application: meta-data, or null
     */
    public Bundle getData() {
        if (mData == null) {
            ApplicationInfo appInfo = getAppInfo();
            if (appInfo != null) {
                mData = appInfo.metaData;
            }
        }

        return mData;
    }

    /**
     * 获取App信息类
     *
     * @return App信息类，an ApplicationInfo Object, or null
     */
    public ApplicationInfo getAppInfo() {
        if (mAppInfo == null) {
            try {
                mAppInfo = getPkgManager().getApplicationInfo(getPkgName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getAppInfo()", e);
            }
        }

        return mAppInfo;
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

}
