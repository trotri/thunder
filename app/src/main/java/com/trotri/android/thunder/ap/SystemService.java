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

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * SystemService class file
 * 系统服务管理类，通过名字获取系统服务
 * 手机信息服务类、网络状态服务类、Wifi状态服务类、定位系统服务类和剪贴板服务类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: SystemService.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class SystemService {

    public static final String TAG = "SystemService";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static SystemService sInstance;

    /**
     * 手机信息服务类
     */
    private TelephonyManager mTelephonyManager;

    /**
     * 网络状态服务类
     */
    private ConnectivityManager mConnectivityManager;

    /**
     * Wifi状态服务类
     */
    private WifiManager mWifiManager;

    /**
     * 定位系统服务类
     */
    private LocationManager mLocationManager;

    /**
     * 剪贴板服务类
     */
    private ClipboardManager mClipboardManager;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境
     */
    private SystemService(Context c) {
        mAppContext = c.getApplicationContext();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static SystemService getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new SystemService(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取手机信息服务类
     *
     * @return a TelephonyManager Object, or null
     */
    public TelephonyManager getTelephonyManager() {
        if (mTelephonyManager == null) {
            Object service = getAppContext().getSystemService(Activity.TELEPHONY_SERVICE);
            if (service != null) {
                mTelephonyManager = (TelephonyManager) service;
            }
        }

        return mTelephonyManager;
    }

    /**
     * 获取网络状态服务类
     *
     * @return a ConnectivityManager Object, or null
     */
    public ConnectivityManager getConnectivityManager() {
        if (mConnectivityManager == null) {
            Object service = getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (service != null) {
                mConnectivityManager = (ConnectivityManager) service;
            }
        }

        return mConnectivityManager;
    }

    /**
     * 获取Wifi状态服务类
     *
     * @return a WifiManager Object, or null
     */
    public WifiManager getWifiManager() {
        if (mWifiManager == null) {
            Object service = getAppContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (service != null) {
                mWifiManager = (WifiManager) service;
            }
        }

        return mWifiManager;
    }

    /**
     * 获取定位系统服务类
     *
     * @return a LocationManager Object, or null
     */
    public LocationManager getLocationManager() {
        if (mLocationManager == null) {
            Object service = getAppContext().getSystemService(Context.LOCATION_SERVICE);
            if (service != null) {
                mLocationManager = (LocationManager) service;
            }
        }

        return mLocationManager;
    }

    /**
     * 获取剪贴板服务类
     *
     * @return a ClipboardManager Object, or null
     */
    public ClipboardManager getClipboardManager() {
        if (mClipboardManager == null) {
            Object service = getAppContext().getSystemService(Context.CLIPBOARD_SERVICE);
            if (service != null) {
                mClipboardManager = (ClipboardManager) service;
            }
        }

        return mClipboardManager;
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
            SystemService o = SystemService.getInstance(c);
            Logger.d(Constants.TAG_LOG, TAG + " TelephonyManager: " + o.getTelephonyManager()
                    + ", ConnectivityManager: " + o.getConnectivityManager()
                    + ", WifiManager: " + o.getWifiManager()
                    + ", LocationManager: " + o.getLocationManager()
                    + ", ClipboardManager: " + o.getClipboardManager());
        }
    }

}
