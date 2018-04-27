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
import com.trotri.android.thunder.util.Registry;

/**
 * Cookie class file
 * Cookie数据寄存类
 * 使用SharedPreferences寄存，默认的SharedPreferences名：cookie_setting、模式：PRIVATE
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Cookie.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public class Cookie extends Registry {

    public static final String TAG = "Cookie";

    /**
     * 默认的SharedPreferences名
     */
    private static final String SETTING_NAME = "cookie_setting";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object sInstanceLock = new Object();

    private static Cookie sInstance;

    /**
     * 构造方法：初始化上下文环境、SharedPreferences对象
     */
    public Cookie() {
        super(App.getContext(), SETTING_NAME);
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static Cookie getInstance() {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new Cookie();
            }

            return sInstance;
        }
    }

    /**
     * 获取用于寄存全局数据的SharedPreferences对象名
     *
     * @return SharedPreferences对象名
     */
    public String getSettingName() {
        return SETTING_NAME;
    }

}
