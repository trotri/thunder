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

package com.trotri.android.library.data;

import com.trotri.android.thunder.ap.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * ListProvider class file
 * 列表数据提供者类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ListProvider.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public class ListProvider<T> {

    public static final String TAG = "ListProvider";

    /**
     * 默认每次查询记录数，SELECT * FROM table LIMIT offset, [limit];
     */
    public static final int DEFAULT_LIMIT = 8;

    /**
     * 总记录数
     */
    private long mTotal;

    /**
     * Null数
     */
    private int mNull;

    /**
     * 每次查询记录数，SELECT * FROM table LIMIT offset, [limit];
     */
    private int mLimit = DEFAULT_LIMIT;

    /**
     * 每次查询起始位置，SELECT * FROM table LIMIT [offset], limit;
     */
    private int mOffset;

    /**
     * 列表数据
     */
    private List<T> mRows;

    /**
     * 获取详情数据
     *
     * @param position 位置
     * @return 详情数据 a T Object, or null
     */
    public T getItem(int position) {
        if (mRows == null) {
            return null;
        }

        try {
            return mRows.get(position);
        } catch (IndexOutOfBoundsException e) {
            String errMsg = "getItem() size: " + getSize() + ", position: " + position + ", null: " + getNull();
            if (hasNull()) {
                errMsg += ", http data has null";
            }

            Logger.e(Constants.TAG_LOG, TAG + " " + errMsg, e);
        }

        return null;
    }

    /**
     * 添加数据列表
     *
     * @param rows 数据列表
     */
    public void addRows(List<T> rows) {
        if (rows == null) {
            return;
        }

        if (mRows == null) {
            mRows = new ArrayList<>();
        }

        if (firstPage()) {
            mRows.clear();
        }

        for (T value : rows) {
            if (value != null) {
                mRows.add(value);
            } else {
                mNull++;
                Logger.w(Constants.TAG_LOG, TAG + " addRows() value is null, Null counter: " + mNull);
            }
        }

        mOffset += mLimit;
    }

    /**
     * 获取当前记录数
     *
     * @return 当前记录数
     */
    public int getSize() {
        if (mRows == null) {
            return 0;
        }

        return mRows.size();
    }

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    public long getTotal() {
        return mTotal;
    }

    /**
     * 设置总记录数
     *
     * @param total 总记录数
     * @return Returns True, or False
     */
    public boolean setTotal(long total) {
        if (total >= 0) {
            mTotal = total;
            return true;
        }

        return false;
    }

    /**
     * 获取Null数
     *
     * @return Null数
     */
    public int getNull() {
        return mNull;
    }

    /**
     * 是否有Null数据
     *
     * @return Returns True, or False
     */
    public boolean hasNull() {
        return getNull() > 0;
    }

    /**
     * 获取每次查询记录数
     * SELECT * FROM table LIMIT offset, [limit];
     *
     * @return 每次查询记录数
     */
    public int getLimit() {
        return mLimit;
    }

    /**
     * 设置每次查询记录数
     * SELECT * FROM table LIMIT offset, [limit];
     *
     * @param limit 每次查询记录数
     * @return Returns True, or False
     */
    public boolean setLimit(int limit) {
        if (limit > 0) {
            mLimit = limit;
            return true;
        }

        return false;
    }

    /**
     * 获取每次查询起始位置
     * SELECT * FROM table LIMIT [offset], limit;
     *
     * @return 每次查询起始位置
     */
    public int getOffset() {
        return mOffset;
    }

    /**
     * 设置每次查询起始位置
     * SELECT * FROM table LIMIT [offset], limit;
     *
     * @param offset 每次查询起始位置
     * @return Returns True, or False
     */
    public boolean setOffset(int offset) {
        if (offset >= 0) {
            mOffset = offset;
            return true;
        }

        return false;
    }

    /**
     * 设置首页页查询起始位置
     * SELECT * FROM table LIMIT [offset], limit;
     */
    public void setOffsetToFirstPage() {
        mOffset = 0;
    }

    /**
     * 设置下一页查询起始位置
     * SELECT * FROM table LIMIT [offset], limit;
     *
     * @return Returns true if has next page, or false
     */
    public boolean setOffsetToNextPage() {
        if (hasMoreData()) {
            int offset = getOffset() + getLimit();
            setOffset(offset);
            return true;
        }

        return false;
    }

    /**
     * 获取当前页码
     *
     * @return 当前页码
     */
    public int getPageNo() {
        if (getLimit() <= 0) {
            return 1;
        }

        int pageNo = (getOffset() / getLimit()) + 1;
        if (pageNo <= 0) {
            return 1;
        }

        return pageNo;
    }

    /**
     * 获取是否是第一页
     *
     * @return Returns True, or False
     */
    public boolean firstPage() {
        return (getPageNo() == 1);
    }

    /**
     * 重置数据，清空当前数据，重新从第一页开始加载
     */
    public void clear() {
        setTotal(0);
        mNull = 0;
        setOffsetToFirstPage();
        mRows = null;
    }

    /**
     * 是否有数据
     *
     * @return Returns True, or False
     */
    public boolean hasData() {
        return getSize() > 0;
    }

    /**
     * 是否还可以再次请求更多数据
     *
     * @return Returns True, or False
     */
    public boolean hasMoreData() {
        return (getSize() + getNull()) < getTotal();
    }

    /**
     * 通过数据适配器设置所有数据
     *
     * @param data a RequestAdapter.DataList<T> Object
     */
    public void setData(RequestAdapter.DataList<T> data) {
        if (data == null) {
            return;
        }

        setTotal(data.getTotal());
        setLimit(data.getLimit());
        setOffset(data.getOffset());

        // 如果是第一页，会清数据。
        // 判断第一页，需要limit和offset，必须在setLimit和setOffset后面执行。
        addRows(data.getRows());
    }

}
