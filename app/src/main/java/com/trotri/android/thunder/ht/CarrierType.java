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

import com.trotri.android.thunder.ap.Network;

/**
 * CarrierType class file
 * 移动运营商类型，0：未知、1：移动、2：联通、3：电信
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: CarrierType.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class CarrierType {

    public static final String TAG = "CarrierType";

    /**
     * 移动运营商类型：未知
     */
    public static final int TYPE_UNKNOWN = 0;

    /**
     * 移动运营商类型：移动
     */
    public static final int TYPE_MOBILE = 1;

    /**
     * 移动运营商类型：联通
     */
    public static final int TYPE_UNICOM = 2;

    /**
     * 移动运营商类型：电信
     */
    public static final int TYPE_TELECOM = 3;

    /**
     * 移动标识
     */
    public static final String KEYWORD_MOBILE = "cmwap";

    /**
     * 联通标识
     */
    public static final String KEYWORD_UNICOM = "uniwap";

    /**
     * 联通标识2
     */
    public static final String KEYWORD_UNICOM_3GWAP = "3gwap";

    /**
     * 电信标识
     */
    public static final String KEYWORD_TELECOM = "ctwap";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static CarrierType sInstance;

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
    private CarrierType(Context c) {
        mAppContext = c.getApplicationContext();
        mNetwork = Network.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static CarrierType getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new CarrierType(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取移动运营商类型
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 移动运营商类型，0：未知、1：移动、2：联通、3：电信
     */
    public int getType() {
        String eInfo = getNetwork().getExtra().toLowerCase();

        if (eInfo.startsWith(KEYWORD_MOBILE)) {
            return TYPE_MOBILE;
        }

        if (eInfo.startsWith(KEYWORD_UNICOM)) {
            return TYPE_UNICOM;
        }

        if (eInfo.startsWith(KEYWORD_UNICOM_3GWAP)) {
            return TYPE_UNICOM;
        }

        if (eInfo.startsWith(KEYWORD_TELECOM)) {
            return TYPE_TELECOM;
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
