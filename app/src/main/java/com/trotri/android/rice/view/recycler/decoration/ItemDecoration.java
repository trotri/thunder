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

package com.trotri.android.rice.view.recycler.decoration;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * ItemDecoration class file
 * RecyclerView ItemDecoration 类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ItemDecoration.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class ItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 是否水平滚动
     */
    private boolean mIsHorizontal;

    /**
     * 获取是否水平滚动
     *
     * @return Returns True, or False
     */
    public boolean isHorizontal() {
        return mIsHorizontal;
    }

    /**
     * 设置是否水平滚动
     *
     * @param isHorizontal 是否水平滚动
     */
    public void setHorizontal(boolean isHorizontal) {
        mIsHorizontal = isHorizontal;
    }

    /**
     * 检查当前item是否是最后一行
     *
     * @param pos     item位置
     * @param columns 列数
     * @param size    总记录数
     * @return Returns True, or False
     */
    public boolean isLastRow(int pos, int columns, int size) {
        return isLastRow(isHorizontal(), pos, columns, size);
    }

    /**
     * 检查当前item是否是最后一列
     *
     * @param pos     item位置
     * @param columns 列数
     * @param size    总记录数
     * @return Returns True, or False
     */
    public boolean isLastColumn(int pos, int columns, int size) {
        return isLastColumn(isHorizontal(), pos, columns, size);
    }

    /**
     * 检查当前item是否是最后一行
     *
     * @param isHorizontal 是否水平滚动
     * @param pos          item位置
     * @param columns      列数
     * @param size         总记录数
     * @return Returns True, or False
     */
    public boolean isLastRow(boolean isHorizontal, int pos, int columns, int size) {
        if (isHorizontal) {
            return isLastColumn(false, pos, columns, size);
        }

        size -= size % columns;
        return (pos >= size);
    }

    /**
     * 检查当前item是否是最后一列
     *
     * @param isHorizontal 是否水平滚动
     * @param pos          item位置
     * @param columns      列数
     * @param size         总记录数
     * @return Returns True, or False
     */
    public boolean isLastColumn(boolean isHorizontal, int pos, int columns, int size) {
        if (isHorizontal) {
            return isLastRow(false, pos, columns, size);
        }

        return ((pos + 1) % columns == 0);
    }

    /**
     * 获取列数
     *
     * @param view a RecyclerView Object
     * @return 列数
     */
    public int getColumns(RecyclerView view) {
        RecyclerView.LayoutManager layoutManager = view.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }

        return 1;
    }

}
