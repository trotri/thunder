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

package com.trotri.android.library.util;

import com.trotri.android.library.App;
import com.trotri.android.rice.util.ResourcesManager;
import com.trotri.android.thunder.ap.CheckPermission;
import com.trotri.android.thunder.ap.Network;
import com.trotri.android.thunder.ap.SystemService;
import com.trotri.android.thunder.file.Sandbox;
import com.trotri.android.thunder.file.SdCard;
import com.trotri.android.thunder.ht.CarrierType;
import com.trotri.android.thunder.ht.ConnectType;
import com.trotri.android.thunder.ht.HttpProxy;
import com.trotri.android.thunder.state.Contacts;
import com.trotri.android.thunder.state.DisplayPixels;
import com.trotri.android.thunder.state.LocationState;
import com.trotri.android.thunder.state.ManifestInfo;
import com.trotri.android.thunder.state.MetaData;
import com.trotri.android.thunder.state.NetworkState;
import com.trotri.android.thunder.state.PhoneState;
import com.trotri.android.thunder.state.Version;
import com.trotri.android.thunder.util.ClipboardHelper;
import com.trotri.android.thunder.util.DexHelper;
import com.trotri.android.thunder.util.DimensionConverter;

/**
 * SingletonManager class file
 * Thunder库的单例类管理器
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: SingletonManager.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public class SingletonManager {

    public static final String TAG = "SingletonManager";

    /**
     * 获取权限检查类
     *
     * @return 权限检查类，a CheckPermission Object
     */
    public static CheckPermission getCheckPermission() {
        return CheckPermission.getInstance(App.getContext());
    }

    /**
     * 获取版本信息类
     *
     * @return 版本信息类，a Version Object
     */
    public static Version getVersion() {
        return Version.getInstance(App.getContext());
    }

    /**
     * 获取MetaData数据类
     *
     * @return MetaData数据类，a MetaData Object
     */
    public static MetaData getMetaData() {
        return MetaData.getInstance(App.getContext());
    }

    /**
     * 获取Manifest信息类
     *
     * @return Manifest信息类，a ManifestInfo Object
     */
    public static ManifestInfo getManifestInfo() {
        return ManifestInfo.getInstance(App.getContext());
    }

    /**
     * 获取应用沙盒目录中文件读写类
     *
     * @return 应用沙盒目录中文件读写类，a Sandbox Object
     */
    public static Sandbox getSandbox() {
        return Sandbox.getInstance(App.getContext());
    }

    /**
     * 获取Secure Digital Memory Card 读写类
     *
     * @return SD卡读写类，a SdCard Object
     */
    public static SdCard getSdCard() {
        return SdCard.getInstance(App.getContext());
    }

    /**
     * 获取手机信息类
     *
     * @return 手机信息类，a PhoneState Object
     */
    public static PhoneState getPhoneState() {
        return PhoneState.getInstance(App.getContext());
    }

    /**
     * 获取位置信息类
     *
     * @return 位置信息类，a LocationState Object
     */
    public static LocationState getLocationState() {
        return LocationState.getInstance(App.getContext());
    }

    /**
     * 获取网络信息类
     *
     * @return 网络信息类，a NetworkState Object
     */
    public static NetworkState getNetworkState() {
        return NetworkState.getInstance(App.getContext());
    }

    /**
     * 获取网络辅助类
     *
     * @return 网络辅助类，a Network Object
     */
    public static Network getNetwork() {
        return Network.getInstance(App.getContext());
    }

    /**
     * 获取网络连接类
     *
     * @return 网络连接类，a HttpProxy Object
     */
    public static HttpProxy getHttpProxy() {
        return HttpProxy.getInstance(App.getContext());
    }

    /**
     * 获取移动运营商类型
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 移动运营商类型，0：未知、1：移动、2：联通、3：电信
     */
    public static int getCarrierType() {
        return CarrierType.getInstance(App.getContext()).getType();
    }

    /**
     * 获取联网类型
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return 联网类型，0：未知、1：Wifi、2：2G、3：3G、4：4G
     */
    public static int getConnectType() {
        return ConnectType.getInstance(App.getContext()).getType();
    }

    /**
     * 获取通讯录管理类
     *
     * @return 通讯录管理类，a Contacts Object
     */
    public static Contacts getContacts() {
        return Contacts.getInstance(App.getContext());
    }

    /**
     * 获取屏幕信息类
     *
     * @return 屏幕信息类，a DisplayPixels Object
     */
    public static DisplayPixels getDisplayPixels() {
        return DisplayPixels.getInstance(App.getContext());
    }

    /**
     * 获取资源管理类
     *
     * @return 资源管理类，a ResourcesManager Object
     */
    public static ResourcesManager getResourcesManager() {
        return ResourcesManager.getInstance(App.getContext());
    }

    /**
     * 获取单位转换类
     *
     * @return 单位转换类，a DimensionConverter Object
     */
    public static DimensionConverter getDimensionConverter() {
        return DimensionConverter.getInstance(App.getContext());
    }

    /**
     * 获取剪贴板辅助类
     *
     * @return 剪贴板辅助类，a ClipboardHelper Object
     */
    public static ClipboardHelper getClipboardHelper() {
        return ClipboardHelper.getInstance(App.getContext());
    }

    /**
     * 获取DexClassLoader辅助类
     *
     * @return DexClassLoader辅助类，a DexHelper Object
     */
    public static DexHelper getDexHelper() {
        return DexHelper.getInstance(App.getContext());
    }

    /**
     * 获取系统服务管理类，通过名字获取系统服务
     *
     * @return 系统服务管理类，a SystemService Object
     */
    public static SystemService getSystemService() {
        return SystemService.getInstance(App.getContext());
    }

}
