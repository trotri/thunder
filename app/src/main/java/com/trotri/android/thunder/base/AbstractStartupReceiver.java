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

package com.trotri.android.thunder.base;

import android.content.BroadcastReceiver;

/**
 * AbstractStartupReceiver abstract class file
 * 接受手机启动时的广播基类，需要子类继承后使用，子类名：StartupReceiver
 * 广播类在主线程中运行，不可执行耗时的操作
 * <p>
 * 避免混淆，proguard-rules.pro:
 * -keep class 包名.AbstractStartupReceiver { *; }
 * -keep class 包名.StartupReceiver { *; }
 * </p>
 * 需要权限：
 * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
 * AndroidManifest.xml文件，application中配置：
 * <receiver android:name=".StartupReceiver" >
 * <intent-filter>
 * <action android:name="android.intent.action.BOOT_COMPLETED" />
 * </intent-filter>
 * </receiver>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: AbstractStartupReceiver.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class AbstractStartupReceiver extends BroadcastReceiver {
}
