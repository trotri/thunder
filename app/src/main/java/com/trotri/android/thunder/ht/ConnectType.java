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

package com.trotri.android.thunder.ht;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.trotri.android.thunder.ap.Network;

/**
 * ConnectType class file
 * 联网类型，0：未知、1：Wifi、2：2G、3：3G、4：4G
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ConnectType.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class ConnectType {

    public static final String TAG = "ConnectType";

    /**
     * 联网类型：未知
     */
    public static final int TYPE_UNKNOWN = 0;

    /**
     * 联网类型：Wifi
     */
    public static final int TYPE_WIFI = 1;

    /**
     * 联网类型：2G
     */
    public static final int TYPE_MOBILE_2G = 2;

    /**
     * 联网类型：3G
     */
    public static final int TYPE_MOBILE_3G = 3;

    /**
     * 联网类型：4G
     */
    public static final int TYPE_MOBILE_4G = 4;

    /**
     * 3G标识1
     */
    public static final String KEYWORD_3G_TD_SCDMA = "TD-SCDMA";

    /**
     * 3G标识2
     */
    public static final String KEYWORD_3G_WCDMA = "WCDMA";

    /**
     * 3G标识3
     */
    public static final String KEYWORD_3G_CDMA2000 = "CDMA2000";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static ConnectType sInstance;

    /**
     * 网络辅助类
     */
    private final Network mNetwork;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、网络辅助类
     */
    private ConnectType(Context c) {
        mAppContext = c.getApplicationContext();
        mNetwork = Network.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static ConnectType getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new ConnectType(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取联网类型
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 联网类型，0：未知、1：Wifi、2：2G、3：3G、4：4G
     */
    public int getType() {
        Network network = getNetwork();

        int type = network.getType();
        if (type == Network.TYPE_WIFI) {
            return TYPE_WIFI;
        }

        if (type != Network.TYPE_MOBILE) {
            return TYPE_UNKNOWN;
        }

        int subtype = network.getSubtype();
        switch (subtype) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return TYPE_MOBILE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return TYPE_MOBILE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return TYPE_MOBILE_4G;
            default:
                break;
        }

        String typeName = network.getSubtypeName().toUpperCase();
        if (typeName.equals(KEYWORD_3G_TD_SCDMA)
                || typeName.equals(KEYWORD_3G_WCDMA)
                || typeName.equals(KEYWORD_3G_CDMA2000)) {
            return TYPE_MOBILE_3G;
        }

        return TYPE_UNKNOWN;
    }

    /**
     * 获取网络辅助类
     *
     * @return a Network Object
     */
    public Network getNetwork() {
        return mNetwork;
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
