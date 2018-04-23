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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * Db class file
 * 数据库操作类
 * <p>
 * 避免混淆，proguard-rules.pro:
 * -keepclassmembers class 包名.Db { *; }
 * </p>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Db.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Db extends SQLiteOpenHelper {

    private static final String TAG = "Db";

    /**
     * 数据库名
     */
    private String mDbName;

    /**
     * 版本
     */
    private int mVersion;

    /**
     * 表名
     */
    private String mTblName;

    /**
     * 创建表命令
     */
    private String mCommand;

    /**
     * 构造方法：初始化上下文环境、数据库名、版本、表名、创建表命令
     *
     * @param c       上下文环境
     * @param dbName  数据库名
     * @param version 版本
     * @param tblName 表名
     * @param command 创建表命令
     */
    public Db(Context c, String dbName, int version, String tblName, String command) {
        super(c, dbName, null, version);

        mDbName = dbName;
        mVersion = version;
        mTblName = tblName;
        mCommand = command;

        Logger.d(Constants.TAG_LOG, TAG + " Constructor() dbName: '" + getDbName() + "', version: " + getVersion() + ", tblName: '" + getTblName() + "', command: '" + getCommand() + "'");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getCommand());

        Logger.d(Constants.TAG_LOG, TAG + " onCreate() command: '" + getCommand() + "'");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + getTblName());
        onCreate(db);

        Logger.d(Constants.TAG_LOG, TAG + " onUpgrade() command: 'DROP TABLE IF EXISTS " + getTblName() + "', oldVersion: " + oldVersion + ", newVersion: " + newVersion);
    }

    /**
     * 执行一条SQL
     *
     * @param sql SQL Command
     */
    public void execSQL(String sql) {
        SQLiteDatabase db;

        db = getWritableDatabase();
        db.execSQL(sql);
        db.close();

        Logger.d(Constants.TAG_LOG, TAG + " execSQL() command: '" + sql + "'");
    }

    /**
     * 执行一条SQL
     *
     * @param sql      SQL Command
     * @param bindArgs an Object Array
     */
    public void execSQL(String sql, Object[] bindArgs) {
        SQLiteDatabase db;

        db = getWritableDatabase();
        db.execSQL(sql, bindArgs);
        db.close();

        Logger.d(Constants.TAG_LOG, TAG + " execSQL() command: '" + sql + "', bindArgs: '" + joinString(bindArgs) + "'");
    }

    /**
     * 插入一条记录
     * <pre>
     * ContentValues values = new ContentValues();
     * values.put("title", "标题");
     * values.put("content", "内容");
     * values.put("dt_created", "2015-10-18");
     * Db db = new Db(context, DB_NAME, VERSION, TABLE_NAME, COMMAND);
     * long lastInsertId = db.insert(null, values);
     * </pre>
     *
     * @param nullColumnHack 空列
     * @param values         行数据集合
     * @return 最后一次插入行的Id，如果出错，返回-1
     */
    public long insert(String nullColumnHack, ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        long lastInsertId = db.insert(getTblName(), nullColumnHack, values);
        db.close();

        Logger.d(Constants.TAG_LOG, TAG + " insert() tblName: '" + getTblName() + "', nullColumnHack: '" + nullColumnHack + "', values: '" + values + "'");
        return lastInsertId;
    }

    /**
     * 更新记录
     *
     * @param values      行数据集合
     * @param whereClause 更新条件
     * @param whereArgs   条件参数，a String Array
     * @return 更新操作影响的行数
     */
    public int update(ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        int rowCount = db.update(getTblName(), values, whereClause, whereArgs);
        db.close();

        Logger.d(Constants.TAG_LOG, TAG + " update() tblName: '" + getTblName() + "', values: '" + values + "', whereClause: '" + whereClause + "', whereArgs: '" + joinString(whereArgs) + "'");
        return rowCount;
    }

    /**
     * 删除记录
     *
     * @param whereClause 删除条件
     * @param whereArgs   条件参数，a String Array
     * @return 删除操作影响的行数
     */
    public int delete(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = getWritableDatabase();
        int rowCount = db.delete(getTblName(), whereClause, whereArgs);
        db.close();

        Logger.d(Constants.TAG_LOG, TAG + " delete() tblName: '" + getTblName() + "', whereClause: '" + whereClause + "', whereArgs: '" + joinString(whereArgs) + "'");
        return rowCount;
    }

    /**
     * 获取数据库名
     *
     * @return 数据库名，a String, or null
     */
    public String getDbName() {
        return mDbName;
    }

    /**
     * 获取版本
     *
     * @return 版本
     */
    public int getVersion() {
        return mVersion;
    }

    /**
     * 获取表名
     *
     * @return 表名，a String, or null
     */
    public String getTblName() {
        return mTblName;
    }

    /**
     * 获取创建表命令
     *
     * @return 创建表命令，a String, or null
     */
    public String getCommand() {
        return mCommand;
    }

    /**
     * 将对象数组用", "拼接成字符串
     *
     * @param params a Object Array
     * @return 拼接后的字符串，a String, or ""
     */
    public String joinString(Object[] params) {
        if (params == null) {
            return "";
        }

        StringBuilder data = new StringBuilder();

        String joinStr = "";
        for (Object value : params) {
            data.append(joinStr).append(value);
            joinStr = ", ";
        }

        return data.toString();
    }

}
