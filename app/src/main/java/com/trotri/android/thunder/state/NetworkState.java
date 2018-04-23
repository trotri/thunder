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
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.trotri.android.thunder.ap.CheckPermission;
import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.ap.Network;
import com.trotri.android.thunder.ap.SystemService;
import com.trotri.android.thunder.ap.TypeCast;
import com.trotri.android.thunder.ht.CarrierType;
import com.trotri.android.thunder.ht.ConnectType;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * NetworkState class file
 * 网络信息类
 * 需要权限：<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: NetworkState.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class NetworkState {

    public static final String TAG = "NetworkState";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static NetworkState sInstance;

    /**
     * 网络辅助类
     */
    private final Network mNetwork;

    /**
     * 移动运营商类型类
     */
    private final CarrierType mCarrierType;

    /**
     * 联网类型类
     */
    private final ConnectType mConnectType;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、网络辅助类、移动运营商类型、联网类型
     */
    private NetworkState(Context c) {
        mAppContext = c.getApplicationContext();
        mNetwork = Network.getInstance(getAppContext());
        mCarrierType = CarrierType.getInstance(getAppContext());
        mConnectType = ConnectType.getInstance(getAppContext());
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static NetworkState getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new NetworkState(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取WIFI名称
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @return WIFI名称，或者""
     */
    @SuppressLint("MissingPermission")
    public String getWifiName() {
        WifiManager wManager = getSystemService().getWifiManager();
        if (wManager == null) {
            return "";
        }

        if (hasAccessWifiState()) {
            try {
                WifiInfo wInfo = wManager.getConnectionInfo();
                if (wInfo == null) {
                    return "";
                }

                String id = wInfo.getSSID();
                return (id == null) ? "" : id;
            } catch (SecurityException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getWifiName() No ACCESS_WIFI_STATE Permission");
            }
        } else {
            Logger.e(Constants.TAG_LOG, TAG + " getWifiName() No ACCESS_WIFI_STATE Permission");
        }

        return "";
    }

    /**
     * 获取MAC地址
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * 或
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return MAC地址，或者""
     */
    public String getMac() {
        String address = getWifiMac();
        if (TextUtils.isEmpty(address)) {
            address = getHostMac();
        }

        return address;
    }

    /**
     * 获取Wifi-MAC地址
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @return MAC地址，或者""
     */
    @SuppressLint("MissingPermission")
    public String getWifiMac() {
        WifiManager wManager = getSystemService().getWifiManager();
        if (wManager == null) {
            return "";
        }

        if (hasAccessWifiState()) {
            try {
                WifiInfo wInfo = wManager.getConnectionInfo();
                if (wInfo == null) {
                    return "";
                }

                String address = wInfo.getMacAddress();
                return (address == null) ? "" : address;
            } catch (SecurityException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getWifiMac() No ACCESS_WIFI_STATE Permission");
            }
        } else {
            Logger.e(Constants.TAG_LOG, TAG + " getWifiMac() No ACCESS_WIFI_STATE Permission");
        }

        return "";
    }

    /**
     * 通过IP，获取MAC地址
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * 或
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return MAC地址，或者""
     */
    public String getHostMac() {
        return getHostMac(getIp());
    }

    /**
     * 通过IP，获取MAC地址
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param ip IP地址
     * @return MAC地址，或者""
     */
    public String getHostMac(String ip) {
        if (TextUtils.isEmpty(ip)) {
            return "";
        }

        InetAddress hostName = null;
        try {
            hostName = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            Logger.e(Constants.TAG_LOG, TAG + " getHostMac()", e);
        }

        if (hostName == null) {
            return "";
        }

        try {
            NetworkInterface nInterface = NetworkInterface.getByInetAddress(hostName);
            if (nInterface == null) {
                return "";
            }

            byte[] data = nInterface.getHardwareAddress();
            String address = TypeCast.toMac(data);
            return (address == null) ? "" : address;
        } catch (SocketException e) {
            Logger.e(Constants.TAG_LOG, TAG + " getHostMac()", e);
        }

        return "";
    }

    /**
     * 获取IP地址
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * 或
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return IP地址，或者""
     */
    public String getIp() {
        NetworkInfo nInfo = getNetwork().getInfo();
        if (nInfo == null) {
            return "";
        }

        String address = getWifiIp();
        if (TextUtils.isEmpty(address)) {
            address = getHostIp();
        }

        if (address == null) {
            return "";
        }

        return "0.0.0.0".equals(address) ? "" : address;
    }

    /**
     * 获取Wifi-IP地址
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @return IP地址，或者""
     */
    @SuppressLint("MissingPermission")
    public String getWifiIp() {
        WifiManager wManager = getSystemService().getWifiManager();
        if (wManager == null) {
            return "";
        }

        if (hasAccessWifiState()) {
            try {
                WifiInfo wInfo = wManager.getConnectionInfo();
                if (wInfo == null) {
                    return "";
                }

                String address = TypeCast.toIp(wInfo.getIpAddress());
                return (address == null) ? "" : address;
            } catch (SecurityException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getWifiIp() No ACCESS_WIFI_STATE Permission");
            }
        } else {
            Logger.e(Constants.TAG_LOG, TAG + " getWifiIp() No ACCESS_WIFI_STATE Permission");
        }

        return "";
    }

    /**
     * 获取网关IP地址
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return IP地址，或者""
     */
    public String getHostIp() {
        Enumeration<NetworkInterface> networkInterfaces = null;

        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            Logger.e(Constants.TAG_LOG, TAG + " getHostIp() No INTERNET Permission");
        }

        if (networkInterfaces == null) {
            return "";
        }

        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface nInterface = networkInterfaces.nextElement();
            if (nInterface == null) {
                return "";
            }

            Enumeration<InetAddress> nAddresses = nInterface.getInetAddresses();
            if (nAddresses == null) {
                return "";
            }

            while (nAddresses.hasMoreElements()) {
                InetAddress address = nAddresses.nextElement();
                if (address == null) {
                    return "";
                }

                if (address.isLoopbackAddress() || address.isLinkLocalAddress()) {
                    continue;
                }

                if (address.isSiteLocalAddress() && (address instanceof Inet4Address)) {
                    String hostIp = address.getHostAddress();
                    return (hostIp == null) ? "" : hostIp;
                }
            }
        }

        return "";
    }

    /**
     * 获取联网类型
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 联网类型，0：未知、1：Wifi、2：2G、3：3G、4：4G
     */
    public int getConnectType() {
        return mConnectType.getType();
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
     * 获取是否有“访问Wifi状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean hasAccessWifiState() {
        return getCheckPermission().hasAccessWifiState();
    }

    /**
     * 获取系统服务管理类
     *
     * @return a SystemService Object
     */
    public SystemService getSystemService() {
        return getNetwork().getSystemService();
    }

    /**
     * 获取权限检查类
     *
     * @return a CheckPermission Object
     */
    public CheckPermission getCheckPermission() {
        return getNetwork().getCheckPermission();
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

    /**
     * 测试输出
     *
     * @param c 上下文环境
     */
    public static void log(Context c) {
        if (Constants.DEBUG) {
            NetworkState o = NetworkState.getInstance(c);
            Logger.d(Constants.TAG_LOG, TAG + " mac: " + o.getMac()
                    + ", wifi-mac: " + o.getWifiMac()
                    + ", host-mac: " + o.getHostMac()
                    + ", ip: " + o.getIp()
                    + ", wifi-ip: " + o.getWifiIp()
                    + ", host-ip: " + o.getHostIp()
                    + ", connectType: " + o.getConnectType()
                    + ", carrierType: " + o.getCarrierType()
                    + ", hasAccessWifiState: " + o.hasAccessWifiState());
        }
    }

}
