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

package com.trotri.android.thunder.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.trotri.android.library.data.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * SwipeUpRefreshLayout class file
 * 上拉加载更多提示布局
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: SwipeUpRefreshLayout.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class SwipeUpRefreshLayout extends FrameLayout {

    public static final String TAG = "SwipeUpRefreshLayout";

    /**
     * 状态：加载中
     */
    public static final int STATUS_LOADING = 1;

    /**
     * 状态：加载更多
     */
    public static final int STATUS_LOAD_MORE = 2;

    /**
     * 状态：没有数据
     */
    public static final int STATUS_NO_DATA = 3;

    /**
     * 提示语：加载中
     */
    private String mLoadingText;

    /**
     * 提示语：加载更多
     */
    private String mLoadMoreText;

    /**
     * 提示语：没有数据
     */
    private String mNoDataText;

    /**
     * 布局高度，单位：px
     */
    private int mLayoutHeight;

    /**
     * 进度条高度，单位：px
     */
    private int mPrgBarHeight;

    /**
     * 字体大小，单位：sp
     */
    private float mTextSize;

    /**
     * 监听“加载更多”按钮点击事件接口
     */
    private OnLoadMoreClickListener mLoadMoreClickListener = null;

    /**
     * 提示视图：加载中
     */
    private LinearLayout mLLayLoading;

    /**
     * 提示视图：加载更多
     */
    private TextView mTvLoadMore;

    /**
     * 提示视图：没有数据
     */
    private TextView mTvNoData;

    /**
     * 当前状态
     */
    private int mStatus;

    /**
     * 构造方法：初始化上下文环境
     *
     * @param c 上下文环境
     */
    protected SwipeUpRefreshLayout(Context c) {
        super(c);
    }

    /**
     * 渲染视图
     */
    protected void render() {
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getLayoutHeight()));

        mLLayLoading = new LinearLayout(getContext());
        mLLayLoading.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLLayLoading.setGravity(Gravity.CENTER);
        mLLayLoading.setOrientation(LinearLayout.HORIZONTAL);
        mLLayLoading.setVisibility(View.VISIBLE);
        addView(mLLayLoading);

        ProgressBar prgBarLoading = new ProgressBar(getContext());
        prgBarLoading.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getPrgBarHeight()));
        mLLayLoading.addView(prgBarLoading);

        TextView tvLoading = new TextView(getContext());
        tvLoading.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvLoading.setGravity(Gravity.CENTER);
        tvLoading.setTextSize(TypedValue.COMPLEX_UNIT_SP, getTextSize());
        tvLoading.setText(getLoadingText());
        mLLayLoading.addView(tvLoading);

        mTvLoadMore = new TextView(getContext());
        mTvLoadMore.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTvLoadMore.setGravity(Gravity.CENTER);
        mTvLoadMore.setTextSize(TypedValue.COMPLEX_UNIT_SP, getTextSize());
        mTvLoadMore.setText(getLoadMoreText());
        mTvLoadMore.setVisibility(View.VISIBLE);
        mTvLoadMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadMoreClickListener != null) {
                    mLoadMoreClickListener.onClick(v);
                }
            }
        });
        addView(mTvLoadMore);

        mTvNoData = new TextView(getContext());
        mTvNoData.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mTvNoData.setGravity(Gravity.CENTER);
        mTvNoData.setTextSize(TypedValue.COMPLEX_UNIT_SP, getTextSize());
        mTvNoData.setText(getNoDataText());
        mTvNoData.setVisibility(View.VISIBLE);
        addView(mTvNoData);

        setStatus(STATUS_LOAD_MORE);
    }

    /**
     * 准备加载，如果返回false，则不允许加载。
     *
     * @param layoutManager a LinearLayoutManager object
     * @return Returns True, or False
     */
    public boolean loadReady(LinearLayoutManager layoutManager) {
        if (isNoData()) {
            return false;
        }

        if (isLoading()) {
            return false;
        }

        if (layoutManager == null) {
            Logger.e(Constants.TAG_LOG, TAG + " loadReady() LinearLayoutManager is null");
            return false;
        }

        int showCount = layoutManager.getChildCount();
        int lastPos = layoutManager.findLastVisibleItemPosition();
        int totalCount = layoutManager.getItemCount();

        Logger.d(Constants.TAG_LOG, TAG + " loadReady() totalCount: " + totalCount + ", lastPos: " + lastPos + ", showCount: " + showCount);
        return (lastPos + 1) >= totalCount;
    }

    /**
     * 获取提示语：加载中
     *
     * @return 提示语
     */
    public String getLoadingText() {
        return mLoadingText;
    }

    /**
     * 获取提示语：加载更多
     *
     * @return 提示语
     */
    public String getLoadMoreText() {
        return mLoadMoreText;
    }

    /**
     * 获取提示语：没有数据
     *
     * @return 提示语
     */
    public String getNoDataText() {
        return mNoDataText;
    }

    /**
     * 获取布局高度，单位：px
     *
     * @return 布局高度
     */
    public int getLayoutHeight() {
        return mLayoutHeight;
    }

    /**
     * 获取进度条高度，单位：px
     *
     * @return 进度条高度
     */
    public int getPrgBarHeight() {
        return mPrgBarHeight;
    }

    /**
     * 获取字体大小，单位：sp
     *
     * @return 字体大小
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * 获取当前状态：是否“加载中”
     *
     * @return Returns True, or False
     */
    public boolean isLoading() {
        return getStatus() == STATUS_LOADING;
    }

    /**
     * 获取当前状态：是否“加载更多”
     *
     * @return Returns True, or False
     */
    public boolean isLoadMore() {
        return getStatus() == STATUS_LOAD_MORE;
    }

    /**
     * 获取当前状态：是否“没有数据”
     *
     * @return Returns True, or False
     */
    public boolean isNoData() {
        return getStatus() == STATUS_NO_DATA;
    }

    /**
     * 获取当前状态
     *
     * @return 当前状态
     */
    public int getStatus() {
        return mStatus;
    }

    /**
     * 设置当前状态
     *
     * @param status 当前状态
     * @return Returns True, or False
     */
    public boolean setStatus(int status) {
        if (status != STATUS_LOADING && status != STATUS_LOAD_MORE && status != STATUS_NO_DATA) {
            return false;
        }

        mStatus = status;

        if (isLoading()) {
            mLLayLoading.setVisibility(View.VISIBLE);
            mTvLoadMore.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.GONE);
        }

        if (isLoadMore()) {
            mTvLoadMore.setVisibility(View.VISIBLE);
            mLLayLoading.setVisibility(View.GONE);
            mTvNoData.setVisibility(View.GONE);
        }

        if (isNoData()) {
            mTvNoData.setVisibility(View.VISIBLE);
            mLLayLoading.setVisibility(View.GONE);
            mTvLoadMore.setVisibility(View.GONE);
        }

        return true;
    }

    /**
     * OnLoadMoreClickListener interface
     * Interface definition for a callback to be invoked when a view is clicked.
     *
     * @since 1.0
     */
    public interface OnLoadMoreClickListener {
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        void onClick(View v);
    }

    /**
     * Builder final class
     * Build a new {@link SwipeUpRefreshLayout}.
     */
    public static final class Builder {
        /**
         * 默认提示语：加载中
         */
        public static final String DEFAULT_LOADING_TEXT = "Loading";

        /**
         * 默认提示语：加载更多
         */
        public static final String DEFAULT_LOAD_MORE_TEXT = "Load More";

        /**
         * 默认提示语：没有数据
         */
        public static final String DEFAULT_NO_DATA_TEXT = "No Data";

        /**
         * 默认布局高度，单位：dp
         */
        public static final int DEFAULT_LAYOUT_HEIGHT = 48;

        /**
         * 默认进度条高度，单位：dp
         */
        public static final int DEFAULT_PRGBAR_HEIGHT = 32;

        /**
         * 默认字体大小，单位：sp
         */
        public static final float DEFAULT_TEXT_SIZE = 16;

        /**
         * 上下文环境
         */
        private final Context mContext;

        /**
         * 提示语：加载中
         */
        private String mLoadingText = DEFAULT_LOADING_TEXT;

        /**
         * 提示语：加载更多
         */
        private String mLoadMoreText = DEFAULT_LOAD_MORE_TEXT;

        /**
         * 提示语：没有数据
         */
        private String mNoDataText = DEFAULT_NO_DATA_TEXT;

        /**
         * 布局高度，单位：dp
         */
        private int mLayoutHeight = DEFAULT_LAYOUT_HEIGHT;

        /**
         * 进度条高度，单位：dp
         */
        private int mPrgBarHeight = DEFAULT_PRGBAR_HEIGHT;

        /**
         * 字体大小，单位：sp
         */
        private float mTextSize = DEFAULT_TEXT_SIZE;

        /**
         * 监听“加载更多”按钮点击事件接口
         */
        private OnLoadMoreClickListener mLoadMoreClickListener = null;

        /**
         * The resource's current display metrics.
         */
        private DisplayMetrics mDisplayMetrics;

        /**
         * 构造方法：初始化 Resource Metrics
         *
         * @param c 上下文环境
         */
        public Builder(Context c) {
            mContext = c;
            mDisplayMetrics = mContext.getResources().getDisplayMetrics();
        }

        /**
         * 创建SwipeUpRefreshLayout对象
         *
         * @return a SwipeUpRefreshLayout Object
         */
        public SwipeUpRefreshLayout create() {
            SwipeUpRefreshLayout result = new SwipeUpRefreshLayout(mContext);

            result.mLoadingText = mLoadingText;
            result.mLoadMoreText = mLoadMoreText;
            result.mNoDataText = mNoDataText;
            result.mLayoutHeight = dp2px(mLayoutHeight);
            result.mPrgBarHeight = dp2px(mPrgBarHeight);
            result.mTextSize = mTextSize;
            result.mLoadMoreClickListener = mLoadMoreClickListener;

            result.render();
            return result;
        }

        /**
         * 设置提示语：加载中
         *
         * @param text 提示语，默认：{@link #DEFAULT_LOADING_TEXT}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setLoadingText(String text) {
            mLoadingText = text;
            return this;
        }

        /**
         * 设置提示语：加载更多
         *
         * @param text 提示语，默认：{@link #DEFAULT_LOAD_MORE_TEXT}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setLoadMoreText(String text) {
            mLoadMoreText = text;
            return this;
        }

        /**
         * 设置提示语：没有数据
         *
         * @param text 提示语，默认：{@link #DEFAULT_NO_DATA_TEXT}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setNoDataText(String text) {
            mNoDataText = text;
            return this;
        }

        /**
         * 设置布局高度，单位：dp
         *
         * @param height 布局高度，默认：{@link #DEFAULT_LAYOUT_HEIGHT}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setLayoutHeight(int height) {
            mLayoutHeight = height;
            return this;
        }

        /**
         * 设置进度条高度，单位：dp
         *
         * @param height 进度条高度，默认：{@link #DEFAULT_PRGBAR_HEIGHT}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setPrgBarHeight(int height) {
            mPrgBarHeight = height;
            return this;
        }

        /**
         * 设置字体大小，单位：sp
         *
         * @param size 字体大小，默认：{@link #DEFAULT_TEXT_SIZE}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextSize(float size) {
            mTextSize = size;
            return this;
        }

        /**
         * 设置监听“加载更多”按钮点击事件接口
         *
         * @param listener a OnLoadMoreClickListener to listen user click
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnLoadMoreClickListener(OnLoadMoreClickListener listener) {
            mLoadMoreClickListener = listener;
            return this;
        }

        /**
         * Device Independent Pixels 转 Pixels
         *
         * @param value Device Independent Pixels
         * @return a Pixels
         */
        private int dp2px(int value) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, mDisplayMetrics);
        }
    }

}
