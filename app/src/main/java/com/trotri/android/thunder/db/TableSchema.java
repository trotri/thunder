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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * TableSchema class file
 * 寄存数据库表的概要描述，包含表名、主键、表的自增字段、字段名、字段默认值等
 * <ul>
 * <li>{@link #mName}</li>
 * <li>{@link #mPrimaryKey}</li>
 * <li>{@link #mAutoIncrement}</li>
 * <li>{@link #mColumnSchemas}</li>
 * <li>{@link #mAttributeDefaults}</li>
 * </ul>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: TableSchema.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class TableSchema {
    /**
     * 表名
     */
    private String mName = null;

    /**
     * 主键名
     */
    private String mPrimaryKey = null;

    /**
     * 自增字段名
     */
    private String mAutoIncrement = null;

    /**
     * 所有列的概要
     */
    private Map<String, ColumnSchema> mColumnSchemas = new HashMap<>();

    /**
     * 列的默认值
     */
    private ContentValues mAttributeDefaults = new ContentValues();

    /**
     * 获取表名
     *
     * @return a String, or null
     */
    public String getName() {
        return mName;
    }

    /**
     * 设置表名
     *
     * @param name 表名
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setName(String name) {
        mName = (("".equals(name)) ? null : name);
        return this;
    }

    /**
     * 获取主键名
     *
     * @return a String, or null
     */
    public String getPrimaryKey() {
        return mPrimaryKey;
    }

    /**
     * 设置主键名
     *
     * @param primaryKey 主键名
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setPrimaryKey(String primaryKey) {
        mPrimaryKey = (("".equals(primaryKey)) ? null : primaryKey);
        return this;
    }

    /**
     * 获取自增字段名
     *
     * @return a String, or null
     */
    public String getAutoIncrement() {
        return mAutoIncrement;
    }

    /**
     * 设置自增字段名
     *
     * @param autoIncrement 自增字段名
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAutoIncrement(String autoIncrement) {
        mAutoIncrement = (("".equals(autoIncrement)) ? null : autoIncrement);
        return this;
    }

    /**
     * 获取所有的列名
     *
     * @return a String Array
     */
    public String[] getColumnNames() {
        Map<String, ColumnSchema> columnSchemas = getColumnSchemas();
        String[] columnNames = new String[columnSchemas.size()];

        Iterator<Map.Entry<String, ColumnSchema>> entry = columnSchemas.entrySet().iterator();
        int p = 0;
        while (entry.hasNext()) {
            columnNames[p++] = entry.next().getKey();
        }

        return columnNames;
    }

    /**
     * 获取所有列的概要
     *
     * @return a Map Key => String, Value => ColumnSchema
     */
    public Map<String, ColumnSchema> getColumnSchemas() {
        return mColumnSchemas;
    }

    /**
     * 获取列的概要
     *
     * @param columnName 列名
     * @return a reference to the ColumnSchema object.
     */
    public ColumnSchema getColumnSchema(String columnName) {
        return mColumnSchemas.get(columnName);
    }

    /**
     * 添加列的概要
     *
     * @param columnSchema a reference to the ColumnSchema object.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema addColumnSchema(ColumnSchema columnSchema) {
        String columnName = columnSchema.getName();
        if (columnName != null) {
            mColumnSchemas.put(columnName, columnSchema);
            if (columnSchema.isPrimaryKey()) {
                setPrimaryKey(columnName);
            }

            if (columnSchema.isAutoIncrement()) {
                setAutoIncrement(columnName);
            }
        }

        return this;
    }

    /**
     * 获取所有列的默认值
     *
     * @return a reference to the ContentValues object.
     */
    public ContentValues getAttributeDefaults() {
        return mAttributeDefaults;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, boolean value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, byte value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, byte[] value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, double value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, float value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, long value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, short value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, String value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 设置列的默认值
     *
     * @param columnName The column name of the preference to modify.
     * @param value      The new value for the preference.
     * @return Returns a reference to the same TableSchema object, so you can chain put calls together.
     */
    public TableSchema setAttributeDefault(String columnName, Integer value) {
        mAttributeDefaults.put(columnName, value);
        return this;
    }

    /**
     * 获取创建表命令
     *
     * @return a String
     */
    public String getCommand() {
        StringBuilder command = new StringBuilder();

        command.append("CREATE TABLE IF NOT EXISTS ").append(getName()).append(" (");

        Map<String, ColumnSchema> columnSchemas = getColumnSchemas();
        Iterator<Map.Entry<String, ColumnSchema>> iterator = columnSchemas.entrySet().iterator();
        String joinStr = "";
        while (iterator.hasNext()) {
            ColumnSchema columnSchema = iterator.next().getValue();

            String name = columnSchema.getName();
            if (name != null) {
                command.append(joinStr).append(name);
            } else {
                continue;
            }

            String type = columnSchema.getType();
            if (type != null) {
                command.append(" ").append(type);
            }

            int size = columnSchema.getSize();
            if (size > 0) {
                command.append("(").append(size).append(")");
            }

            if (columnSchema.isPrimaryKey()) {
                command.append(" PRIMARY KEY");
            }

            if (columnSchema.isAutoIncrement()) {
                command.append(" AUTOINCREMENT");
            }

            if (!columnSchema.isAllowNull()) {
                command.append(" NOT NULL");
            }

            joinStr = ", ";
        }

        command.append(");");
        return command.toString();
    }

}
