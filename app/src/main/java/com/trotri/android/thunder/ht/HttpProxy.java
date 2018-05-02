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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * HttpProxy class file
 * 网络连接类，移动运营商类型，选择合适的代理
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: HttpProxy.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class HttpProxy {

    public static final String TAG = "HttpProxy";

    /**
     * 移动代理IP
     */
    public static final String MOBILE_IP = "10.0.0.172";

    /**
     * 联通代理IP
     */
    public static final String UNICOM_IP = "10.0.0.172";

    /**
     * 电信代理IP
     */
    public static final String TELECOM_IP = "10.0.0.200";

    /**
     * 移动代理
     */
    public static final Proxy MOBILE_PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(MOBILE_IP, 80));

    /**
     * 联通代理
     */
    public static final Proxy UNICOM_PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(UNICOM_IP, 80));

    /**
     * 电信代理
     */
    public static final Proxy TELECOM_PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(TELECOM_IP, 80));

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static HttpProxy sInstance;

    /**
     * 运营商类型
     */
    private final CarrierType mCarrierType;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、运营商类型
     */
    private HttpProxy(Context c) {
        mAppContext = c.getApplicationContext();
        mCarrierType = CarrierType.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static HttpProxy getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new HttpProxy(c);
            }

            return sInstance;
        }
    }

    /**
     * 代理方式连接网络
     *
     * @param spec a URL String
     * @return a HttpURLConnection Object
     * @throws IOException 如果连接网络失败，抛出异常
     */
    public HttpURLConnection openConnection(String spec) throws IOException {
        URL url = new URL(spec);

        URLConnection urlConn;
        int type = getCarrierType();
        switch (type) {
            case CarrierType.TYPE_MOBILE:
                urlConn = url.openConnection(MOBILE_PROXY);
                break;
            case CarrierType.TYPE_UNICOM:
                urlConn = url.openConnection(UNICOM_PROXY);
                break;
            case CarrierType.TYPE_TELECOM:
                urlConn = url.openConnection(TELECOM_PROXY);
                break;
            default:
                urlConn = url.openConnection();
                break;
        }

        if (urlConn == null) {
            throw new IOException("Open Connection Failure, type: " + type + ", url: " + spec);
        }

        return (HttpURLConnection) urlConn;
    }

    /**
     * 获取移动运营商类型
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 移动运营商类型，0：未知、1：移动、2：联通、3：电信
     */
    public int getCarrierType() {
        return mCarrierType.getType();
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
