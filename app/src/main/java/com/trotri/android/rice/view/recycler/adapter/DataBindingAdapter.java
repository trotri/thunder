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
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * DataBindingAdapter abstract class file
 * 标准的 RecyclerView Adapter 基类，用于 DataBinding，View包含一个Header和Footer
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DataBindingAdapter.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class DataBindingAdapter<DB extends ViewDataBinding> extends BaseAdapter {
    /**
     * 构造方法：初始化上下文环境
     *
     * @param c 上下文环境
     */
    public DataBindingAdapter(Context c) {
        super(c);
    }

    /**
     * ViewHolder上绑定数据
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    public abstract void bindData(ViewHolder holder, int position);

    /**
     * 创建DataBinding
     *
     * @param root Optional view to be the parent of the generated hierarchy (if
     *             <em>attachToRoot</em> is true), or else simply an object that
     *             provides a set of LayoutParams values for root of the returned
     *             hierarchy (if <em>attachToRoot</em> is false.)
     * @return a ViewDataBinding Object
     */
    public abstract DB newDataBinding(ViewGroup root);

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return new ViewHolder(getHeaderView());
        }

        if (viewType == VIEW_TYPE_FOOTER) {
            return new ViewHolder(getFooterView());
        }

        return new ViewHolder(newDataBinding(parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (hasHeaderView()) {
            // position == 0 是 Header View
            position--;
        }

        if (isNormalPos(position)) {
            bindData((ViewHolder) holder, position);
        }
    }

    /**
     * A ViewHolder describes an item view, registry View or ViewDataBinding.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Item View
         */
        private View mView;

        /**
         * Item ViewDataBinding
         */
        private DB mDataBinding;

        /**
         * 构造方法：初始化 Item View
         *
         * @param v Item View
         */
        public ViewHolder(View v) {
            super(v);
            mView = v;
        }

        /**
         * 构造方法：初始化 Item ViewDataBinding
         *
         * @param dataBinding Item ViewDataBinding
         */
        public ViewHolder(DB dataBinding) {
            super(dataBinding.getRoot());
            mDataBinding = dataBinding;
        }

        /**
         * 获取 Item View
         *
         * @return Item View
         */
        public View getView() {
            return mView;
        }

        /**
         * 获取 Item ViewDataBinding
         *
         * @return Item ViewDataBinding
         */
        public DB getDataBinding() {
            return mDataBinding;
        }

    }

}
