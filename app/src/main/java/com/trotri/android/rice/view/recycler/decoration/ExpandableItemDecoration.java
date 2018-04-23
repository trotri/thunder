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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.trotri.android.rice.bean.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.util.DimensionConverter;

import java.util.List;

/**
 * ExpandableItemDecoration class file
 * RecyclerView Expandable ItemDecoration 类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ExpandableItemDecoration.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class ExpandableItemDecoration extends LinearItemDecoration {

    public static final String TAG = "ExpandableItemDecoration";

    /**
     * 同父区域宽和高
     */
    public static final int MATCH_PARENT = -1;

    /**
     * 垂直居中
     */
    public static final int CENTER_VERTICAL = -1;

    /**
     * 水平居中
     */
    public static final int CENTER_HORIZONTAL = -1;

    /**
     * 默认背景颜色
     */
    public static final int DEFAULT_BG_COLOR = android.R.color.background_light;

    /**
     * 默认高度，单位：dp
     */
    public static final int DEFAULT_HEIGHT = 48;

    /**
     * 默认颜色
     */
    public static final int DEFAULT_COLOR = android.R.color.background_light;

    /**
     * 默认文字大小，单位：sp
     */
    public static final float DEFAULT_TEXT_SIZE = 14;

    /**
     * 默认文字颜色
     */
    public static final int DEFAULT_TEXT_COLOR = android.R.color.darker_gray;

    /**
     * 背景高度，高度 + 上边距 + 下边距，单位：px
     */
    private int mBgHeight;

    /**
     * 背景颜色
     */
    @ColorInt
    private int mBgColor;

    /**
     * 宽度，单位：px
     */
    private int mWidth;

    /**
     * 高度，单位：px
     */
    private int mHeight;

    /**
     * 颜色
     */
    @ColorInt
    private int mColor;

    /**
     * 左边距，单位：px
     */
    private int mMarginLeft;

    /**
     * 上边距，单位：px
     */
    private int mMarginTop;

    /**
     * 下边距，单位：px
     */
    private int mMarginBottom;

    /**
     * 文字大小，单位：px
     */
    private float mTextSize;

    /**
     * 文字颜色
     */
    @ColorInt
    private int mTextColor;

    /**
     * 文字左边距，单位：px
     */
    private int mTextMarginLeft;

    /**
     * 文字下边距，单位：px
     */
    private int mTextMarginBottom;

    /**
     * 背景区域画笔
     */
    private Paint mBgPaint;

    /**
     * 画笔
     */
    private Paint mBannerPaint;

    /**
     * 文字画笔
     */
    private TextPaint mTextPaint;

    /**
     * 分组信息，Index => Position, Value => Group Name
     */
    private List<String> mGroups;

    /**
     * 构造方法：禁止被实例化
     *
     * @param c 上下文环境
     */
    protected ExpandableItemDecoration(Context c) {
    }

    /**
     * 初始化画笔
     */
    protected void onInitialize() {
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);

        mBannerPaint = new Paint();
        mBannerPaint.setColor(mColor);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextAlign(((mTextMarginLeft == CENTER_HORIZONTAL) ? Paint.Align.CENTER : Paint.Align.LEFT));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (mGroups == null) {
            return;
        }

        int position = parent.getChildPosition(view);
        outRect.top = isEqualPrevGroupName(position) ? 0 : mBgHeight;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        if (mGroups == null) {
            return;
        }

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        int bannerLeft = left + mMarginLeft;
        int bannerRight = (mWidth == MATCH_PARENT) ? right : (bannerLeft + mWidth);
        int textMarginLeft = (mTextMarginLeft == CENTER_HORIZONTAL) ? ((bannerRight - bannerLeft) / 2) : mTextMarginLeft;

        int count = parent.getChildCount();

        View firstViewInGroup = null; // 当前分组的第一个视图
        String firstGroupNameInGroup = ""; // 当前分组的第一个组名

        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            if (view == null) {
                continue;
            }

            int position = parent.getChildPosition(view);
            String groupName = getGroupName(position);
            if (TextUtils.isEmpty(groupName)) {
                continue;
            }

            if (!groupName.equals(firstGroupNameInGroup)) {
                firstViewInGroup = view;
                firstGroupNameInGroup = groupName;
            }

            if (firstViewInGroup == null || TextUtils.isEmpty(firstGroupNameInGroup)) {
                continue;
            }

            int bottom = (firstViewInGroup.getTop() < mBgHeight) ? mBgHeight : firstViewInGroup.getTop();
            if ((i < (count - 1)) && !isEqualNextGroupName(position)) {
                View firstViewInNextGroup = parent.getChildAt(i + 1);
                if (firstViewInNextGroup != null) {
                    int bottomInNextGroup = (firstViewInNextGroup.getTop() < mBgHeight) ? mBgHeight : firstViewInNextGroup.getTop();
                    int topInNextGroup = bottomInNextGroup - mBgHeight;
                    if (topInNextGroup <= mBgHeight) {
                        bottom = topInNextGroup;
                    }
                }
            }

            int top = bottom - mBgHeight;
            int bannerTop = top + mMarginTop;
            int bannerBottom = bottom - mMarginBottom;

            c.drawRect(left, top, right, bottom, mBgPaint);
            c.drawRect(bannerLeft, bannerTop, bannerRight, bannerBottom, mBannerPaint);
            c.drawText(firstGroupNameInGroup, bannerLeft + textMarginLeft, bannerBottom - mTextMarginBottom, mTextPaint);
        }
    }

    /**
     * 获取当前位置的组名和上一个位置的组名是否相同
     *
     * @param position 当前位置
     * @return Returns True, or False
     */
    public boolean isEqualPrevGroupName(int position) {
        if (mGroups == null) {
            return true;
        }

        if (position <= 0) {
            return false;
        }

        String groupName = getGroupName(position);
        if (TextUtils.isEmpty(groupName)) {
            return true;
        }

        String prevGroupName = getGroupName(position - 1);
        return groupName.equals(prevGroupName);
    }

    /**
     * 获取当前位置的组名和下一个位置的组名是否相同
     *
     * @param position 当前位置
     * @return Returns True, or False
     */
    public boolean isEqualNextGroupName(int position) {
        String nextGroupName = getGroupName(position + 1);
        if (TextUtils.isEmpty(nextGroupName)) {
            return true;
        }

        String groupName = getGroupName(position);
        return nextGroupName.equals(groupName);
    }

    /**
     * 获取当前位置的组名
     *
     * @param position 位置
     * @return 组名
     */
    public String getGroupName(int position) {
        if (mGroups == null) {
            return "";
        }

        if (position < 0) {
            return "";
        }

        if (position >= mGroups.size()) {
            return "";
        }

        try {
            return mGroups.get(position);
        } catch (IndexOutOfBoundsException e) {
            Logger.e(Constants.TAG_LOG, TAG + " getGroupName()", e);
        }

        return "";
    }

    /**
     * 设置分组信息
     *
     * @param groups 分组信息，a List, Index => Position, Value => Group Name
     */
    public void setGroups(List<String> groups) {
        mGroups = groups;
    }

    /**
     * Builder final class
     * Build a new {@link ExpandableItemDecoration}.
     */
    public static final class Builder {
        /**
         * 背景颜色，默认：{@link #DEFAULT_BG_COLOR}
         */
        private int mBgColor = DEFAULT_BG_COLOR;

        /**
         * 宽度，>= 0 或 {@link #MATCH_PARENT}，单位：dp，默认：{@link #MATCH_PARENT}
         */
        private int mWidth = MATCH_PARENT;

        /**
         * 高度，>= 0，单位：dp，默认：{@link #DEFAULT_HEIGHT}
         */
        private int mHeight = DEFAULT_HEIGHT;

        /**
         * 颜色
         */
        private int mColor = DEFAULT_COLOR;

        /**
         * 左边距，>= 0，单位：dp
         */
        private int mMarginLeft = 0;

        /**
         * 上边距，>= 0，单位：dp
         */
        private int mMarginTop = 0;

        /**
         * 下边距，>= 0，单位：dp
         */
        private int mMarginBottom = 0;

        /**
         * 文字大小，> 0，单位：sp
         */
        private float mTextSize = DEFAULT_TEXT_SIZE;

        /**
         * 文字颜色
         */
        private int mTextColor = DEFAULT_TEXT_COLOR;

        /**
         * 文字左边距，>= 0 或 {@link #CENTER_HORIZONTAL}，单位：dp，默认：{@link #CENTER_HORIZONTAL}
         */
        private int mTextMarginLeft = CENTER_HORIZONTAL;

        /**
         * 文字下边距，>= 0 或 {@link #CENTER_VERTICAL}，单位：dp，默认：{@link #CENTER_VERTICAL}
         */
        private int mTextMarginBottom = CENTER_VERTICAL;

        /**
         * 资源类
         */
        private final Resources mResources;

        /**
         * 单位转换类
         */
        private final DimensionConverter mConverter;

        /**
         * 上下文环境
         */
        private final Context mContext;

        /**
         * 构造方法：初始化上下文环境、资源类、单位转换类
         *
         * @param c 上下文环境
         */
        public Builder(Context c) {
            mContext = c.getApplicationContext();
            mResources = mContext.getResources();
            mConverter = DimensionConverter.getInstance(mContext);
        }

        /**
         * 创建ExpandableItemDecoration对象
         *
         * @return a ExpandableItemDecoration Object
         */
        public ExpandableItemDecoration create() {
            ExpandableItemDecoration result = new ExpandableItemDecoration(mContext);

            setAttributes(result);

            return result;
        }

        /**
         * 设置ExpandableItemDecoration类的所有属性
         *
         * @param decoration a ExpandableItemDecoration Object
         */
        protected void setAttributes(ExpandableItemDecoration decoration) {
            if (decoration == null) {
                return;
            }

            decoration.mBgColor = mResources.getColor(mBgColor);

            decoration.mWidth = (mWidth >= 0) ? (int) mConverter.dp2px(mWidth) : MATCH_PARENT;
            decoration.mHeight = (int) mConverter.dp2px(mHeight);
            decoration.mColor = mResources.getColor(mColor);
            decoration.mMarginLeft = (int) mConverter.dp2px(mMarginLeft);
            decoration.mMarginTop = (int) mConverter.dp2px(mMarginTop);
            decoration.mMarginBottom = (int) mConverter.dp2px(mMarginBottom);

            decoration.mTextSize = mConverter.sp2px(mTextSize);
            decoration.mTextColor = mResources.getColor(mTextColor);
            decoration.mTextMarginLeft = (mTextMarginLeft >= 0) ? (int) mConverter.dp2px(mTextMarginLeft) : CENTER_HORIZONTAL;
            decoration.mTextMarginBottom = (mTextMarginBottom >= 0) ? (int) mConverter.dp2px(mTextMarginBottom) : CENTER_VERTICAL;

            decoration.mBgHeight = decoration.mHeight + decoration.mMarginTop + decoration.mMarginBottom;
            if (decoration.mTextMarginBottom == CENTER_VERTICAL) {
                decoration.mTextMarginBottom = (decoration.mHeight - ((int) decoration.mTextSize)) / 2;
            }

            decoration.onInitialize();
        }

        /**
         * 设置背景颜色
         *
         * @param color 背景颜色，默认：{@link #DEFAULT_BG_COLOR}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setBgColor(int color) {
            mBgColor = color;
            return this;
        }

        /**
         * 设置宽度
         *
         * @param width 宽度，>= 0 或 {@link #MATCH_PARENT}，单位：dp，默认：{@link #MATCH_PARENT}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setWidth(int width) {
            if (width >= 0 || width == MATCH_PARENT) {
                mWidth = width;
            } else {
                Logger.e(Constants.TAG_LOG, TAG + " Builder setWidth() width '" + width + "' is wrong, must be >= 0 or ExpandableItemDecoration.MATCH_PARENT");
            }

            return this;
        }

        /**
         * 设置高度
         *
         * @param height 高度，>= 0，单位：dp，默认：{@link #DEFAULT_HEIGHT}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setHeight(int height) {
            if (height >= 0) {
                mHeight = height;
            } else {
                Logger.e(Constants.TAG_LOG, TAG + " Builder setHeight() height '" + height + "' is wrong, must be >= 0");
            }

            return this;
        }

        /**
         * 设置颜色
         *
         * @param color 颜色，默认：{@link #DEFAULT_COLOR}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setColor(int color) {
            mColor = color;
            return this;
        }

        /**
         * 设置左边距
         *
         * @param marginLeft 左边距，>= 0，单位：dp，默认：0
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMarginLeft(int marginLeft) {
            if (marginLeft >= 0) {
                mMarginLeft = marginLeft;
            } else {
                Logger.e(Constants.TAG_LOG, TAG + " Builder setMarginLeft() marginLeft '" + marginLeft + "' is wrong, must be >= 0");
            }

            return this;
        }

        /**
         * 设置上边距
         *
         * @param marginTop 上边距，>= 0，单位：dp，默认：0
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMarginTop(int marginTop) {
            if (marginTop >= 0) {
                mMarginTop = marginTop;
            } else {
                Logger.e(Constants.TAG_LOG, TAG + " Builder setMarginTop() marginTop '" + marginTop + "' is wrong, must be >= 0");
            }

            return this;
        }

        /**
         * 设置下边距
         *
         * @param marginBottom 下边距，>= 0，单位：dp，默认：0
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setMarginBottom(int marginBottom) {
            if (marginBottom >= 0) {
                mMarginBottom = marginBottom;
            } else {
                Logger.e(Constants.TAG_LOG, TAG + " Builder setMarginBottom() marginBottom '" + marginBottom + "' is wrong, must be >= 0");
            }

            return this;
        }

        /**
         * 设置文字大小
         *
         * @param size 文字大小，> 0，单位：sp，默认：{@link #DEFAULT_TEXT_SIZE}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextSize(float size) {
            if (size > 0) {
                mTextSize = size;
            } else {
                Logger.e(Constants.TAG_LOG, TAG + " Builder setTextSize() size '" + size + "' is wrong, must be > 0");
            }

            return this;
        }

        /**
         * 设置文字颜色
         *
         * @param color 文字颜色，默认：{@link #DEFAULT_TEXT_COLOR}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextColor(int color) {
            mTextColor = color;
            return this;
        }

        /**
         * 设置文字左边距
         *
         * @param marginLeft 文字左边距，>= 0 或 {@link #CENTER_HORIZONTAL}，单位：dp，默认：{@link #CENTER_HORIZONTAL}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextMarginLeft(int marginLeft) {
            if (marginLeft >= 0 || marginLeft == CENTER_HORIZONTAL) {
                mTextMarginLeft = marginLeft;
            } else {
                Logger.e(Constants.TAG_LOG, TAG + " Builder setTextMarginLeft() marginLeft '" + marginLeft + "' is wrong, must be >= 0 or ExpandableItemDecoration.CENTER_HORIZONTAL");
            }

            return this;
        }

        /**
         * 设置文字下边距
         *
         * @param marginBottom 文字下边距，>= 0 或 {@link #CENTER_VERTICAL}，单位：dp，默认：{@link #CENTER_VERTICAL}
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setTextMarginBottom(int marginBottom) {
            if (marginBottom >= 0 || marginBottom == CENTER_VERTICAL) {
                mTextMarginBottom = marginBottom;
            } else {
                Logger.e(Constants.TAG_LOG, TAG + " Builder setTextMarginBottom() marginBottom '" + marginBottom + "' is wrong, must be >= 0 or ExpandableItemDecoration.CENTER_VERTICAL");
            }

            return this;
        }

    }

}
