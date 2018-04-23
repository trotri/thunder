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

package com.trotri.android.rice.view.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

/**
 * BaseAdapter abstract class file
 * RecyclerView Adapter 基类，View包含一个Header和Footer
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BaseAdapter.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class BaseAdapter extends RecyclerView.Adapter {
    /**
     * View Type Normal
     */
    public static final int VIEW_TYPE_NORMAL = 0;

    /**
     * View Type Header
     */
    public static final int VIEW_TYPE_HEADER = 1;

    /**
     * View Type Footer
     */
    public static final int VIEW_TYPE_FOOTER = 2;

    /**
     * 布局辅助类
     */
    private final LayoutInflater mLayoutInflater;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 顶部视图
     */
    private View mHeaderView;

    /**
     * 底部视图
     */
    private View mFooterView;

    /**
     * 构造方法：初始化上下文环境、布局辅助类
     *
     * @param c 上下文环境
     */
    public BaseAdapter(Context c) {
        mAppContext = c.getApplicationContext();
        mLayoutInflater = LayoutInflater.from(mAppContext);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    public abstract int getSize();

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public int getItemCount() {
        int size = getSize();

        if (hasHeaderView()) {
            size++;
        }

        if (hasFooterView()) {
            size++;
        }

        return size;
    }

    /**
     * 通过视图位置获取视图类型
     *
     * @param position 视图位置
     * @return View Type：Normal、Header or Footer
     */
    public int getViewType(int position) {
        if (isHeaderPos(position)) {
            return VIEW_TYPE_HEADER;
        }

        if (isFooterPos(position)) {
            return VIEW_TYPE_FOOTER;
        }

        return VIEW_TYPE_NORMAL;
    }

    /**
     * 获取顶部视图
     *
     * @return 顶部视图
     */
    public View getHeaderView() {
        return mHeaderView;
    }

    /**
     * 设置顶部视图
     *
     * @param v 顶部视图
     */
    public void setHeaderView(View v) {
        if (v != null) {
            mHeaderView = v;
            notifyItemInserted(getHeaderPos());
        }
    }

    /**
     * 获取底部视图
     *
     * @return 底部视图
     */
    public View getFooterView() {
        return mFooterView;
    }

    /**
     * 设置底部视图
     *
     * @param v 底部视图
     */
    public void setFooterView(View v) {
        if (v != null) {
            mFooterView = v;
            notifyItemInserted(getFooterPos());
        }
    }

    /**
     * 是否是普通视图
     *
     * @param v 视图
     * @return Returns True, or False
     */
    public boolean isNormalView(View v) {
        return !(isHeaderView(v) || isFooterView(v));
    }

    /**
     * 是否是顶部视图
     *
     * @param v 视图
     * @return Returns True, or False
     */
    public boolean isHeaderView(View v) {
        return hasHeaderView() && getHeaderView() == v;
    }

    /**
     * 是否是底部视图
     *
     * @param v 视图
     * @return Returns True, or False
     */
    public boolean isFooterView(View v) {
        return hasFooterView() && getFooterView() == v;
    }

    /**
     * 是否是普通视图位置
     *
     * @param position 视图位置
     * @return Returns True, or False
     */
    public boolean isNormalPos(int position) {
        return !(isHeaderPos(position) || isFooterPos(position));
    }

    /**
     * 是否是顶部视图位置
     *
     * @param position 视图位置
     * @return Returns True, or False
     */
    public boolean isHeaderPos(int position) {
        return hasHeaderView() && position == getHeaderPos();
    }

    /**
     * 是否是底部视图位置
     *
     * @param position 视图位置
     * @return Returns True, or False
     */
    public boolean isFooterPos(int position) {
        return hasFooterView() && position == getFooterPos();
    }

    /**
     * 是否有顶部视图
     *
     * @return Returns True, or False
     */
    public boolean hasHeaderView() {
        return getHeaderView() != null;
    }

    /**
     * 是否有底部视图
     *
     * @return Returns True, or False
     */
    public boolean hasFooterView() {
        return getFooterView() != null;
    }

    /**
     * 获取顶部视图位置
     *
     * @return 视图位置
     */
    public int getHeaderPos() {
        return 0;
    }

    /**
     * 获取底部视图位置
     *
     * @return 视图位置，总视图数 - 1
     */
    public int getFooterPos() {
        return getItemCount() - 1;
    }

    /**
     * 获取布局辅助类
     *
     * @return 布局辅助类
     */
    public LayoutInflater getLayoutInflater() {
        return mLayoutInflater;
    }

    /**
     * 获取上下文环境
     *
     * @return an Application Context
     */
    public Context getAppContext() {
        return mAppContext;
    }

}
