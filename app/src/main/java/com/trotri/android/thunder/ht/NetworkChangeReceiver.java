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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * NetworkChangeReceiver class file
 * 接受手机网络改变时的广播类
 * 广播类在主线程中运行，不可执行耗时的操作
 * 执行之前需要register()，执行之后需要unregister()
 * 建议在AndroidManifest.xml文件中注册
 * <p>
 * 避免混淆，proguard-rules.pro:
 * -keep class 包名.NetworkChangeReceiver { *; }
 * -keep class 包名.NetworkChangeReceiverImpl { *; }
 * </p>
 * 需要权限：
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * AndroidManifest.xml文件，application中配置，NetworkChangeReceiverImpl是NetworkChangeReceiver子类：
 * <receiver android:name=".NetworkChangeReceiverImpl">
 * <intent-filter>
 * <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
 * </intent-filter>
 * </receiver>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: NetworkChangeReceiver.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "NetworkChangeReceiver";

    /**
     * 广播类Filter的Action名
     */
    public static final String INTENT_FILTER_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    /**
     * 回调接口
     */
    private Listener mListener;

    /**
     * 上下文环境
     */
    private Context mContext;

    /**
     * 构造方法：AndroidManifest.xml文件中配置时使用
     */
    public NetworkChangeReceiver() {
    }

    /**
     * 构造方法：初始化上下文环境、回调接口
     *
     * @param c 上下文环境
     * @param l 回调接口
     */
    public NetworkChangeReceiver(Context c, Listener l) {
        mContext = c;
        mListener = l;
    }

    /**
     * 将广播接收器注册到当前的上下文环境中
     */
    public void register() {
        Logger.d(Constants.TAG_LOG, TAG + " register()");

        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_FILTER_ACTION);

        mContext.registerReceiver(this, filter);
    }

    /**
     * 注销广播接收器
     */
    public void unregister() {
        Logger.d(Constants.TAG_LOG, TAG + " unregister()");

        mContext.unregisterReceiver(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context c, Intent i) {
        if (mListener == null) {
            Logger.e(Constants.TAG_LOG, TAG + " onReceive() mListener is null");
            return;
        }

        mListener.onReceive(c, i);
    }

    /**
     * 获取上下文环境
     *
     * @return a Context Object
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * Listener interface
     * 回调接口
     *
     * @since 1.0
     */
    public interface Listener {
        /**
         * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
         *
         * @param c The Context in which the receiver is running.
         * @param i The Intent being received
         */
        void onReceive(Context c, Intent i);
    }

}
