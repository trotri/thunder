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
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;

/**
 * StandardAdapter abstract class file
 * 标准的 RecyclerView Adapter 基类，View包含一个Header和Footer
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StandardAdapter.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class StandardAdapter<VH extends RecyclerView.ViewHolder> extends BaseAdapter {
    /**
     * 构造方法：初始化上下文环境
     *
     * @param c 上下文环境
     */
    public StandardAdapter(Context c) {
        super(c);
    }

    /**
     * ViewHolder上绑定数据
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public abstract void bindData(VH holder, int position);

    /**
     * 创建ViewHolder
     *
     * @param v The root View of the inflated hierarchy.
     * @return a ViewHolder Object
     */
    public abstract VH newViewHolder(View v);

    /**
     * Returns ID for an XML layout resource to load (e.g.,
     * <code>R.layout.main_page</code>)
     *
     * @return ID for an XML layout resource
     */
    @LayoutRes
    public abstract int getResource();

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflate(viewType, getResource(), parent, false);
        return newViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (hasHeaderView()) {
            // position == 0 是 Header View
            position--;
        }

        if (isNormalPos(position)) {
            bindData((VH) holder, position);
        }
    }

    /**
     * Inflate a new view hierarchy from the specified xml resource. Throws
     * {@link InflateException} if there is an error.
     *
     * @param viewType     View Type：Normal、Header or Footer
     * @param resource     ID for an XML layout resource to load (e.g.,
     *                     <code>R.layout.main_page</code>)
     * @param root         Optional view to be the parent of the generated hierarchy (if
     *                     <em>attachToRoot</em> is true), or else simply an object that
     *                     provides a set of LayoutParams values for root of the returned
     *                     hierarchy (if <em>attachToRoot</em> is false.)
     * @param attachToRoot Whether the inflated hierarchy should be attached to
     *                     the root parameter? If false, root is only used to create the
     *                     correct subclass of LayoutParams for the root view in the XML.
     * @return The root View of the inflated hierarchy. If root was supplied and
     * attachToRoot is true, this is root; otherwise it is the root of
     * the inflated XML file.
     */
    public View inflate(int viewType, @LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {
        if (viewType == VIEW_TYPE_HEADER) {
            return getHeaderView();
        }

        if (viewType == VIEW_TYPE_FOOTER) {
            return getFooterView();
        }

        return getLayoutInflater().inflate(resource, root, attachToRoot);
    }

}
