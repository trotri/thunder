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

package com.trotri.android.thunder.ap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Network class file
 * 网络辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Network.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Network {

    public static final String TAG = "Network";

    /**
     * 网络类型：未知
     */
    public static final int TYPE_UNKNOWN = 0;

    /**
     * 网络类型：Wifi
     */
    public static final int TYPE_WIFI = 1;

    /**
     * 网络类型：Mobile
     */
    public static final int TYPE_MOBILE = 2;

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static Network sInstance;

    /**
     * 系统服务管理类
     */
    private final SystemService mSystemService;

    /**
     * 权限检查类
     */
    private final CheckPermission mCheckPermission;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、系统服务管理类、权限检查类
     */
    private Network(Context c) {
        mAppContext = c.getApplicationContext();
        mSystemService = SystemService.getInstance(getAppContext());
        mCheckPermission = CheckPermission.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static Network getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new Network(c);
            }

            return sInstance;
        }
    }

    /**
     * 检查是否联网
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean isConnected() {
        NetworkInfo nInfo = getConnectedInfo();
        return (nInfo != null);
    }

    /**
     * 检查是否是Wifi联网
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean isWifi() {
        return getType() == TYPE_WIFI;
    }

    /**
     * 检查是否是Mobile联网
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean isMobile() {
        return getType() == TYPE_MOBILE;
    }

    /**
     * 获取网络类型
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 网络类型，0：未知、1：Wifi、2：Mobile
     */
    public int getType() {
        NetworkInfo nInfo = getConnectedInfo();
        if (nInfo == null) {
            return TYPE_UNKNOWN;
        }

        int type = nInfo.getType();
        if (type == ConnectivityManager.TYPE_WIFI) {
            return TYPE_WIFI;
        }

        if (type == ConnectivityManager.TYPE_MOBILE) {
            return TYPE_MOBILE;
        }

        return TYPE_UNKNOWN;
    }

    /**
     * 获取子网络类型
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 子网络类型
     */
    public int getSubtype() {
        NetworkInfo nInfo = getConnectedInfo();
        if (nInfo == null) {
            return TYPE_UNKNOWN;
        }

        return nInfo.getSubtype();
    }

    /**
     * 获取子网络类型名
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 子网络类型名，或""
     */
    public String getSubtypeName() {
        NetworkInfo nInfo = getConnectedInfo();
        if (nInfo == null) {
            return "";
        }

        String typeName = nInfo.getSubtypeName();
        return (typeName == null) ? "" : typeName;
    }

    /**
     * 获取网络扩展信息
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 网络扩展信息，或""
     */
    public String getExtra() {
        NetworkInfo nInfo = getConnectedInfo();
        if (nInfo == null) {
            return "";
        }

        String eInfo = nInfo.getExtraInfo();
        return (eInfo == null) ? "" : eInfo;
    }

    /**
     * 获取已联网的网络信息类
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return a Connected NetworkInfo Object, or null
     */
    public NetworkInfo getConnectedInfo() {
        NetworkInfo nInfo = getAvailableInfo();
        if (nInfo == null || !nInfo.isConnected()) {
            return null;
        }

        return nInfo;
    }

    /**
     * 获取有效的网络信息类
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return a Available NetworkInfo Object, or null
     */
    public NetworkInfo getAvailableInfo() {
        NetworkInfo nInfo = getInfo();
        if (nInfo == null || !nInfo.isAvailable()) {
            return null;
        }

        return nInfo;
    }

    /**
     * 获取网络信息类
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return a Active NetworkInfo Object, or null
     */
    @SuppressLint("MissingPermission")
    public NetworkInfo getInfo() {
        ConnectivityManager cManager = getConnectivityManager();
        if (cManager == null) {
            return null;
        }

        try {
            hasAccessNetworkState(true);
            return cManager.getActiveNetworkInfo();
        } catch (SecurityException e) {
            Logger.e(Constants.TAG_LOG, TAG + " getInfo()", e);
        }

        return null;
    }

    /**
     * 获取网络状态服务类
     *
     * @return a ConnectivityManager Object, or null
     */
    public ConnectivityManager getConnectivityManager() {
        return getSystemService().getConnectivityManager();
    }

    /**
     * 获取是否有“访问网络状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasAccessNetworkState(boolean throwException) throws SecurityException {
        return getCheckPermission().hasAccessNetworkState(throwException);
    }

    /**
     * 获取是否有“访问网络状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean hasAccessNetworkState() {
        return getCheckPermission().hasAccessNetworkState();
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
     * 获取权限检查类
     *
     * @return a CheckPermission Object
     */
    public CheckPermission getCheckPermission() {
        return mCheckPermission;
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
