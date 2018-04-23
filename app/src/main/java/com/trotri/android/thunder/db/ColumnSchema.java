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

import android.text.TextUtils;

/**
 * ColumnSchema class file
 * 寄存数据库列的概要描述，包含列名、类型、长度、默认值、是否是主键、是否自增、是否允许为空等
 * <ul>
 * <li>{@link #mName}</li>
 * <li>{@link #mType}</li>
 * <li>{@link #mSize}</li>
 * <li>{@link #mIsPrimaryKey}</li>
 * <li>{@link #mIsAutoIncrement}</li>
 * <li>{@link #mIsAllowNull}</li>
 * </ul>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ColumnSchema.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class ColumnSchema {
    /**
     * 列名
     */
    private String mName = null;

    /**
     * 类型
     */
    private String mType = null;

    /**
     * 长度
     */
    private int mSize = 0;

    /**
     * 是否是主键
     */
    private boolean mIsPrimaryKey = false;

    /**
     * 是否自增
     */
    private boolean mIsAutoIncrement = false;

    /**
     * 是否允许为空
     */
    private boolean mIsAllowNull = false;

    /**
     * 获取列名
     *
     * @return a String, or null
     */
    public String getName() {
        return mName;
    }

    /**
     * 设置列名
     *
     * @param name 列名
     * @return Returns a reference to the same ColumnSchema object, so you can chain put calls together.
     */
    public ColumnSchema setName(String name) {
        mName = ((name == null || "".equals(name)) ? null : name);
        return this;
    }

    /**
     * 获取类型
     *
     * @return an Upper String, or null
     */
    public String getType() {
        return mType;
    }

    /**
     * 设置类型
     *
     * @param type 类型，Convert to Upper String
     * @return Returns a reference to the same ColumnSchema object, so you can chain put calls together.
     */
    public ColumnSchema setType(String type) {
        mType = (TextUtils.isEmpty(type)) ? null : type.toUpperCase();
        return this;
    }

    /**
     * 获取长度
     *
     * @return 长度 >= 0
     */
    public int getSize() {
        return mSize;
    }

    /**
     * 设置长度
     *
     * @param size 长度 >= 0
     * @return Returns a reference to the same ColumnSchema object, so you can chain put calls together.
     */
    public ColumnSchema setSize(int size) {
        mSize = (size > 0) ? size : 0;
        return this;
    }

    /**
     * 获取是否是主键
     *
     * @return Returns True, or False
     */
    public boolean isPrimaryKey() {
        return mIsPrimaryKey;
    }

    /**
     * 设置是否是主键
     *
     * @param isPrimaryKey 默认：False
     * @return Returns a reference to the same ColumnSchema object, so you can chain put calls together.
     */
    public ColumnSchema setPrimaryKey(boolean isPrimaryKey) {
        mIsPrimaryKey = isPrimaryKey;
        return this;
    }

    /**
     * 获取是否自增
     *
     * @return Returns True, or False
     */
    public boolean isAutoIncrement() {
        return mIsAutoIncrement;
    }

    /**
     * 设置是否自增
     *
     * @param isAutoIncrement 默认：False
     * @return Returns a reference to the same ColumnSchema object, so you can chain put calls together.
     */
    public ColumnSchema setAutoIncrement(boolean isAutoIncrement) {
        mIsAutoIncrement = isAutoIncrement;
        return this;
    }

    /**
     * 获取是否允许为空
     *
     * @return Returns True, or False
     */
    public boolean isAllowNull() {
        return mIsAllowNull;
    }

    /**
     * 设置是否允许为空
     *
     * @param isAllowNull 默认：False
     * @return Returns a reference to the same ColumnSchema object, so you can chain put calls together.
     */
    public ColumnSchema setAllowNull(boolean isAllowNull) {
        mIsAllowNull = isAllowNull;
        return this;
    }

}
