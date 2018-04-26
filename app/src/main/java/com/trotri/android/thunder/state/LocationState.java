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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.TextUtils;

import com.trotri.android.thunder.ap.CheckPermission;
import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.ap.SystemService;

import java.util.ArrayList;
import java.util.List;

/**
 * LocationState class file
 * 位置信息类
 * 需要权限：
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 * 或
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: LocationState.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class LocationState {

    public static final String TAG = "LocationState";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static LocationState sInstance;

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
    private LocationState(Context c) {
        mAppContext = c.getApplicationContext();
        mSystemService = SystemService.getInstance(getAppContext());
        mCheckPermission = CheckPermission.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static LocationState getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new LocationState(c);
            }

            return sInstance;
        }
    }

    /**
     * 打开位置信息设置页面
     *
     * @param context a Activity Object
     */
    public static void toSetting(Activity context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 获取位置信息
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     * 或
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     *
     * @return a Location Object, or null
     */
    @SuppressLint("MissingPermission")
    public Location getLastKnownLocation() {
        LocationManager lManager = getSystemService().getLocationManager();
        if (lManager == null) {
            return null;
        }

        if (!hasAccessFineLocation() && !hasAccessCoarseLocation()) {
            return null;
        }

        String provider = getProvider();
        if (TextUtils.isEmpty(provider)) {
            return null;
        }

        try {
            return lManager.getLastKnownLocation(provider);
        } catch (SecurityException e) {
            Logger.e(Constants.TAG_LOG, TAG + " getLastKnownLocation() No ACCESS_FINE_LOCATION or ACCESS_COARSE_LOCATION Permission");
        }

        return null;
    }

    /**
     * 获取一个可用的位置服务提供者
     *
     * @return a Provider String, "gps"、"network"、""
     */
    public String getProvider() {
        List<String> providers = getProviders();

        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }

        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        }

        return "";
    }

    /**
     * 获取所有的位置服务提供者
     *
     * @return a Provider List, "gps"、"network"
     */
    public List<String> getProviders() {
        LocationManager lManager = getSystemService().getLocationManager();
        if (lManager == null) {
            return new ArrayList<>();
        }

        List<String> providers = lManager.getProviders(true);
        if (providers == null) {
            return new ArrayList<>();
        }

        return providers;
    }

    /**
     * 检查是否开启GPS
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     *
     * @return Returns True, or False
     */
    public boolean isGpsEnabled() {
        LocationManager lManager = getSystemService().getLocationManager();
        return (lManager != null)
                && hasAccessFineLocation()
                && lManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 检查是否支持通过基站或者Wifi获取位置信息
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     * 或
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     *
     * @return Returns True, or False
     */
    public boolean isNetworkEnabled() {
        LocationManager lManager = getSystemService().getLocationManager();
        return (lManager != null)
                && (hasAccessCoarseLocation() || hasAccessFineLocation())
                && lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * 检查是否支持位置更新监测器
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     *
     * @return Returns True, or False
     */
    public boolean isPassiveEnabled() {
        LocationManager lManager = getSystemService().getLocationManager();
        return (lManager != null)
                && hasAccessFineLocation()
                && lManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
    }

    /**
     * 获取是否有“通过GPS定位”或者“支持位置更新监测器”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     *
     * @return Returns True, or False
     */
    public boolean hasAccessFineLocation() {
        return getCheckPermission().hasAccessFineLocation();
    }

    /**
     * 获取是否有“通过基站或者Wifi定位”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     *
     * @return Returns True, or False
     */
    public boolean hasAccessCoarseLocation() {
        return getCheckPermission().hasAccessCoarseLocation();
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

    /**
     * 测试输出
     *
     * @param c 上下文环境
     */
    public static void log(Context c) {
        if (Constants.DEBUG) {
            LocationState o = LocationState.getInstance(c);

            StringBuilder providers = new StringBuilder();
            String joinStr = "";
            for (String value : o.getProviders()) {
                providers.append(joinStr).append(value);
                joinStr = "|";
            }

            Logger.d(Constants.TAG_LOG, TAG + " hasAccessFineLocation: " + o.hasAccessFineLocation()
                    + ", hasAccessCoarseLocation: " + o.hasAccessCoarseLocation()
                    + ", isGpsEnabled: " + o.isGpsEnabled()
                    + ", isNetworkEnabled: " + o.isNetworkEnabled()
                    + ", isPassiveEnabled: " + o.isPassiveEnabled()
                    + ", providers: " + providers.toString()
                    + ", provider: " + o.getProvider());

            Location l = o.getLastKnownLocation();
            if (l == null) {
                Logger.d(Constants.TAG_LOG, TAG + " LastKnownLocation is null");
            } else {
                Logger.d(Constants.TAG_LOG, TAG + " LastKnownLocation, Latitude: " + l.getLatitude() + ", Longitude: " + l.getLongitude() + ", Provider: " + l.getProvider());
            }
        }
    }

}
