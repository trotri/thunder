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

package com.trotri.android.thunder.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.TypeCast;

/**
 * Registry class file
 * 全局数据寄存类
 * 使用Db寄存，默认的Db名：包名_thunder_db_setting，table名：setting
 * <pre>
 * 表概要：
 * id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
 * setting_key VARCHAR(50) UNIQUE NOT NULL
 * setting_value TEXT NOT NULL
 * </pre>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Registry.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Registry {

    public static final String TAG = "Registry";

    /**
     * 默认的Db名的后缀
     */
    private static final String DEFAULT_DB_POSTFIX = "_" + Constants.TAG_LOWER + "_db_setting";

    /**
     * 版本
     */
    private static final int VERSION = 1;

    /**
     * 表名
     */
    private static final String TABLE_NAME = "setting";

    /**
     * 主键名
     */
    private static final String SETTING_PK = "id";

    /**
     * Key的键名
     */
    private static final String SETTING_KEY = "setting_key";

    /**
     * Value的键名
     */
    private static final String SETTING_VALUE = "setting_value";

    /**
     * 包名
     */
    private final String mPackageName;

    /**
     * Db名，包名 + 后缀名
     */
    private final String mDbName;

    /**
     * Db对象
     */
    private final Db mDb;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 初始化上下文环境、包名、Db名、Db对象
     */
    public Registry(Context c) {
        this(c, null);
    }

    /**
     * 初始化上下文环境、包名、Db名、Db对象
     *
     * @param c         上下文环境
     * @param dbPostfix Db名的后缀，Db名：包名 + 后缀名
     */
    public Registry(Context c, String dbPostfix) {
        dbPostfix = (dbPostfix == null) ? "" : dbPostfix.trim();
        dbPostfix = ("".equals(dbPostfix)) ? DEFAULT_DB_POSTFIX : dbPostfix;

        mAppContext = c.getApplicationContext();
        mPackageName = getAppContext().getPackageName();
        mDbName = getPackageName() + dbPostfix;
        mDb = new Db(getAppContext(), getDbName(), VERSION, TABLE_NAME, getCommand());
    }

    /**
     * 通过名称获取整数值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue
     */
    public int getInt(String key, int defaultValue) {
        String value = getString(key, null);
        return TypeCast.toInt(value, defaultValue);
    }

    /**
     * 设置名称和整数值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns True, or False
     */
    public boolean putInt(String key, int value) {
        return putString(key, String.valueOf(value));
    }

    /**
     * 通过名称获取长整数值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue
     */
    public long getLong(String key, long defaultValue) {
        String value = getString(key, null);
        return TypeCast.toLong(value, defaultValue);
    }

    /**
     * 设置名称和长整数值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns True, or False
     */
    public boolean putLong(String key, long value) {
        return putString(key, String.valueOf(value));
    }

    /**
     * 通过名称获取浮点值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue
     */
    public float getFloat(String key, float defaultValue) {
        String value = getString(key, null);
        return TypeCast.toFloat(value, defaultValue);
    }

    /**
     * 设置名称和浮点值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns True, or False
     */
    public boolean putFloat(String key, float value) {
        return putString(key, String.valueOf(value));
    }

    /**
     * 通过名称获取布尔值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return (getInt(key, (defaultValue ? 1 : 0)) == 1);
    }

    /**
     * 设置名称和布尔值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns True, or False
     */
    public boolean putBoolean(String key, boolean value) {
        return putInt(key, (value ? 1 : 0));
    }

    /**
     * 通过名称获取字符串值
     *
     * @param key          The name of the preference to retrieve.
     * @param defaultValue Value to return if this preference does not exist.
     * @return Returns the preference value if it exists, or defaultValue
     */
    public String getString(String key, String defaultValue) {
        String value = defaultValue;

        if (TextUtils.isEmpty(key)) {
            return value;
        }

        SQLiteDatabase dbReadable = getDb().getReadableDatabase();
        Cursor cursor = dbReadable.query(TABLE_NAME, new String[]{SETTING_VALUE},
                SETTING_KEY + " = ?", new String[]{key}, null, null, null);
        if (cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(SETTING_VALUE));
        }

        cursor.close();
        dbReadable.close();
        return value;
    }

    /**
     * 设置名称和字符串值
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns True, or False
     */
    public boolean putString(String key, String value) {
        if (TextUtils.isEmpty(key) || value == null) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(SETTING_KEY, key);
        values.put(SETTING_VALUE, value);

        boolean isNewRecord = (getString(key, null) == null);

        if (isNewRecord) {
            long lastInsertId = getDb().insert(null, values);
            if (lastInsertId > 0) {
                return true;
            }
        } else {
            int rowCount = getDb().update(values, SETTING_KEY + " = ?", new String[]{key});
            if (rowCount > 0) {
                return true;
            }
        }

        return false;
    }

    /**
     * 通过名称删除数据
     *
     * @param key The name of the preference to remove.
     * @return Returns True, or False
     */
    public boolean remove(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        int rowCount = getDb().delete(SETTING_KEY + " = ?", new String[]{key});
        return (rowCount > 0);
    }

    /**
     * 获取创建表命令
     *
     * @return 创建表命令
     */
    public String getCommand() {
        String data = "";

        data += "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ";
        data += "(";
        data += SETTING_PK + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ";
        data += SETTING_KEY + " VARCHAR(50) UNIQUE NOT NULL, ";
        data += SETTING_VALUE + " TEXT NOT NULL";
        data += ");";

        return data;
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
     * 获取Db名
     *
     * @return Db名
     */
    public String getDbName() {
        return mDbName;
    }

    /**
     * 获取Db对象
     *
     * @return Db对象，a Db Object
     */
    public Db getDb() {
        return mDb;
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
