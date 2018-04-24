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

package com.trotri.android.library;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * App final class file
 * 程序启动的时候，初始化的类，替代Application类，主要目的：获取全局的Context、初始化日志的处理接口等
 * AndroidManifest.xml文件，application中配置：
 * <application android:name="com.trotri.android.library.App" ...> ... </application>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: App.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public final class App extends Application {

    public static final String TAG = "App";

    /**
     * 上下文环境
     */
    private static Context sContext;

    /**
     * 获取上下文环境
     *
     * @return an Application Context Object
     */
    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();
    }

    /**
     * 获取是否是Debug方式打包
     *
     * @return Returns True, or False
     */
    public static boolean debug() {
        ApplicationInfo appInfo = getContext().getApplicationInfo();
        return (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

}
