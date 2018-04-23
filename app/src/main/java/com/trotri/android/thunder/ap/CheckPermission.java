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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * CheckPermission class file
 * 权限检查类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: CheckPermission.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class CheckPermission {

    public static final String TAG = "CheckPermission";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static CheckPermission sInstance;

    /**
     * 包名
     */
    private String mPackageName;

    /**
     * AndroidManifest信息类
     */
    private final PackageManager mPackageManager;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境、包名、AndroidManifest信息类
     */
    private CheckPermission(Context c) {
        mAppContext = c.getApplicationContext();
        mPackageName = getAppContext().getPackageName();
        mPackageManager = getAppContext().getPackageManager();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static CheckPermission getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new CheckPermission(c);
            }

            return sInstance;
        }
    }

    /**
     * 获取是否有“访问手机状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasReadPhoneState(boolean throwException) throws SecurityException {
        if (hasReadPhoneState()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No READ_PHONE_STATE Permission");
        }

        return false;
    }

    /**
     * 获取是否有“访问手机状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean hasReadPhoneState() {
        return isGranted(Manifest.permission.READ_PHONE_STATE);
    }

    /**
     * 获取是否有“网络”权限
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasInternet(boolean throwException) throws SecurityException {
        if (hasInternet()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No INTERNET Permission");
        }

        return false;
    }

    /**
     * 获取是否有“网络”权限
     * 需要权限：
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return Returns True, or False
     */
    public boolean hasInternet() {
        return isGranted(Manifest.permission.INTERNET);
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
        if (hasAccessNetworkState()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No ACCESS_NETWORK_STATE Permission");
        }

        return false;
    }

    /**
     * 获取是否有“访问网络状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean hasAccessNetworkState() {
        return isGranted(Manifest.permission.ACCESS_NETWORK_STATE);
    }

    /**
     * 获取是否有“访问Wifi状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasAccessWifiState(boolean throwException) throws SecurityException {
        if (hasAccessWifiState()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No ACCESS_WIFI_STATE Permission");
        }

        return false;
    }

    /**
     * 获取是否有“访问Wifi状态”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @return Returns True, or False
     */
    public boolean hasAccessWifiState() {
        return isGranted(Manifest.permission.ACCESS_WIFI_STATE);
    }

    /**
     * 获取是否有“通过GPS定位”或“支持位置更新监测器”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasAccessFineLocation(boolean throwException) throws SecurityException {
        if (hasAccessFineLocation()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No ACCESS_FINE_LOCATION Permission");
        }

        return false;
    }

    /**
     * 获取是否有“通过GPS定位”或“支持位置更新监测器”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
     *
     * @return Returns True, or False
     */
    public boolean hasAccessFineLocation() {
        return isGranted(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * 获取是否有“通过基站或Wifi定位”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasAccessCoarseLocation(boolean throwException) throws SecurityException {
        if (hasAccessCoarseLocation()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No ACCESS_COARSE_LOCATION Permission");
        }

        return false;
    }

    /**
     * 获取是否有“通过基站或Wifi定位”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
     *
     * @return Returns True, or False
     */
    public boolean hasAccessCoarseLocation() {
        return isGranted(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * 获取是否有“读取通讯录”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_CONTACTS" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasReadContacts(boolean throwException) throws SecurityException {
        if (hasReadContacts()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No READ_CONTACTS Permission");
        }

        return false;
    }

    /**
     * 获取是否有“读取通讯录”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_CONTACTS" />
     *
     * @return Returns True, or False
     */
    public boolean hasReadContacts() {
        return isGranted(Manifest.permission.READ_CONTACTS);
    }

    /**
     * 获取是否有“读取Sd卡”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     * 或
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasReadExternalStorage(boolean throwException) throws SecurityException {
        if (hasReadExternalStorage()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No READ_EXTERNAL_STORAGE or WRITE_EXTERNAL_STORAGE Permission");
        }

        return false;
    }

    /**
     * 获取是否有“读取Sd卡”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     * 或
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *
     * @return Returns True, or False
     */
    public boolean hasReadExternalStorage() {
        return hasWriteExternalStorage()
                || Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN
                || isGranted(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    /**
     * 获取是否有“写入Sd卡”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasWriteExternalStorage(boolean throwException) throws SecurityException {
        if (hasWriteExternalStorage()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No WRITE_EXTERNAL_STORAGE Permission");
        }

        return false;
    }

    /**
     * 获取是否有“写入Sd卡”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *
     * @return Returns True, or False
     */
    public boolean hasWriteExternalStorage() {
        return isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 获取是否有“接受手机启动广播”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
     *
     * @param throwException 无权限时，True：抛出异常、False：返回False
     * @return Returns True, or False
     * @throws SecurityException 如果无权限，返回False，或抛出异常
     */
    public boolean hasReceiveBootCompleted(boolean throwException) throws SecurityException {
        if (hasReceiveBootCompleted()) {
            return true;
        }

        if (throwException) {
            throw new SecurityException("No RECEIVE_BOOT_COMPLETED Permission");
        }

        return false;
    }

    /**
     * 获取是否有“接受手机启动广播”的权限
     * 需要权限：
     * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
     *
     * @return Returns True, or False
     */
    public boolean hasReceiveBootCompleted() {
        return isGranted(Manifest.permission.RECEIVE_BOOT_COMPLETED);
    }

    /**
     * 获取是否有某个权限
     *
     * @param value 权限名 android.permission.*
     * @return Returns True, or False
     */
    public boolean isGranted(String value) {
        return PackageManager.PERMISSION_GRANTED == getPackageManager().checkPermission(value, getPackageName());
    }

    /**
     * 获取包名
     *
     * @return 包名
     */
    public String getPackageName() {
        return mPackageName;
    }

    /**
     * 获取AndroidManifest信息类
     *
     * @return a PackageManager Object
     */
    public PackageManager getPackageManager() {
        return mPackageManager;
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
            CheckPermission o = CheckPermission.getInstance(c);
            Logger.d(Constants.TAG_LOG, TAG + " READ_PHONE_STATE: " + o.hasReadPhoneState()
                    + ", INTERNET: " + o.hasInternet()
                    + ", ACCESS_NETWORK_STATE: " + o.hasAccessNetworkState()
                    + ", ACCESS_WIFI_STATE: " + o.hasAccessWifiState()
                    + ", ACCESS_FINE_LOCATION: " + o.hasAccessFineLocation()
                    + ", ACCESS_COARSE_LOCATION: " + o.hasAccessCoarseLocation()
                    + ", READ_CONTACTS: " + o.hasReadContacts()
                    + ", READ_EXTERNAL_STORAGE: " + o.hasReadExternalStorage()
                    + ", WRITE_EXTERNAL_STORAGE: " + o.hasWriteExternalStorage()
                    + ", RECEIVE_BOOT_COMPLETED: " + o.hasReceiveBootCompleted());
        }
    }

}
