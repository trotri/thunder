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
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.trotri.android.thunder.ap.CheckPermission;
import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.ap.SystemService;

import java.util.Locale;

/**
 * PhoneState class file
 * 手机信息类
 * 需要权限：
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: PhoneState.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class PhoneState {

    public static final String TAG = "PhoneState";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static PhoneState sInstance;

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
    private PhoneState(Context c) {
        mAppContext = c.getApplicationContext();
        mSystemService = SystemService.getInstance(getAppContext());
        mCheckPermission = CheckPermission.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static PhoneState getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new PhoneState(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取设备识别码，GSM：IMEI、CDMA：MEID
     * 待解决：双SIM卡和无权限
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @return 设备识别码，或者""
     */
    @SuppressLint("MissingPermission")
    public String getDeviceId() {
        TelephonyManager tManager = getTelephonyManager();
        if (tManager == null) {
            return "";
        }

        if (hasReadPhoneState()) {
            try {
                String deviceId;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    deviceId = tManager.getImei();
                } else {
                    deviceId = tManager.getDeviceId();
                }

                return (deviceId == null) ? "" : deviceId;
            } catch (SecurityException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getDeviceId() No READ_PHONE_STATE Permission");
            }
        } else {
            Logger.e(Constants.TAG_LOG, TAG + " getDeviceId() No READ_PHONE_STATE Permission");
        }

        return "";
    }

    /**
     * 获取设备ID
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @return 设备ID，或者""
     */
    @SuppressLint("MissingPermission")
    public String getSubscriberId() {
        TelephonyManager tManager = getTelephonyManager();
        if (tManager == null) {
            return "";
        }

        if (hasReadPhoneState()) {
            try {
                String subscriberId = tManager.getSubscriberId();
                return (subscriberId == null) ? "" : subscriberId;
            } catch (SecurityException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getSubscriberId() No READ_PHONE_STATE Permission");
            }
        } else {
            Logger.e(Constants.TAG_LOG, TAG + " getSubscriberId() No READ_PHONE_STATE Permission");
        }

        return "";
    }

    /**
     * 获取网络运营商类型，如：46000
     *
     * @return 网络运营商类型，或者""
     */
    public String getNetworkOperator() {
        TelephonyManager tManager = getTelephonyManager();
        if (tManager == null) {
            return "";
        }

        String networkOperator = tManager.getNetworkOperator();
        return (networkOperator == null) ? "" : networkOperator;
    }

    /**
     * 获取网络运营商类型名称，如：中国移动
     *
     * @return 网络运营商类型名称，或者""
     */
    public String getNetworkOperatorName() {
        TelephonyManager tManager = getTelephonyManager();
        if (tManager == null) {
            return "";
        }

        String networkOperatorName = tManager.getNetworkOperatorName();
        return (networkOperatorName == null) ? "" : networkOperatorName;
    }

    /**
     * 获取Android Id
     *
     * @return Android Id
     */
    public String getAndroidId() {
        return Settings.System.getString(getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getLanguage() {
        String language = Locale.getDefault().getLanguage();
        return (language == null) ? "" : language;
    }

    /**
     * 获取手机信息服务类
     *
     * @return a TelephonyManager Object, or null
     */
    public TelephonyManager getTelephonyManager() {
        return getSystemService().getTelephonyManager();
    }

    /**
     * 获取是否有“访问手机状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean hasReadPhoneState() {
        return getCheckPermission().hasReadPhoneState();
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
            PhoneState o = PhoneState.getInstance(c);
            Logger.d(Constants.TAG_LOG, TAG + " deviceId: " + o.getDeviceId()
                    + ", subscriberId" + o.getSubscriberId()
                    + ", androidId: " + o.getAndroidId()
                    + ", language: " + PhoneState.getLanguage()
                    + ", hasReadPhoneState: " + o.hasReadPhoneState());
        }
    }

}
