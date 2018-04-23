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

package com.trotri.android.rice.db;

import android.content.Context;

import com.trotri.android.thunder.db.Registry;

/**
 * DbStorage class file
 * 将网络数据保存到本地数据库
 * 使用Db寄存，默认的Db名：包名_http_db_setting，table名：setting
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DbStorage.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class DbStorage {

    public static final String TAG = "DbStorage";

    /**
     * 默认的Db名后缀
     */
    private static final String DEFAULT_DB_POSTFIX = "_http_db_setting";

    /**
     * 全局数据寄存类，Db寄存
     */
    private Registry mRegistry;

    /**
     * 构造方法：初始化上下文环境、Db寄存类
     */
    public DbStorage(Context c) {
        this(c, null);
    }

    /**
     * 构造方法：初始化上下文环境、Db名的后缀、Db寄存类
     *
     * @param c         上下文环境
     * @param dbPostfix Db名的后缀，Db名：包名 + 后缀名
     */
    public DbStorage(Context c, String dbPostfix) {
        dbPostfix = (dbPostfix == null) ? "" : dbPostfix.trim();
        dbPostfix = ("".equals(dbPostfix)) ? DEFAULT_DB_POSTFIX : dbPostfix;

        mRegistry = new Registry(c, dbPostfix);
    }

    /**
     * 通过名称获取字符串值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue
     */
    public String get(String key, String defaultValue) {
        return mRegistry.getString(key, defaultValue);
    }

    /**
     * 设置名称和字符串值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns True, or False
     */
    public boolean put(String key, String value) {
        return mRegistry.putString(key, value);
    }

    /**
     * 通过名称删除数据
     *
     * @param key The name of the preference to remove.
     * @return Returns True, or False
     */
    public boolean remove(String key) {
        return mRegistry.remove(key);
    }

}
