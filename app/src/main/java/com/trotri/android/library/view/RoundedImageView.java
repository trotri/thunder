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

package com.trotri.android.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.TextView;

import com.trotri.android.java.sample.R;
import com.trotri.android.thunder.util.DimensionConverter;

/**
 * RoundedImageView class file
 * 圆角图片，可分别设置四圆角半径，只适用于ScaleType.CENTER_CROP
 * 自定义：xmlns:app="http://schemas.android.com/apk/res-auto"
 * attrs.xml文件，resources中配置：
 * <declare-styleable name="RoundedImageView">
 * <attr name="cornerRadius_leftTop" format="dimension"/>
 * <attr name="cornerRadius_rightTop" format="dimension"/>
 * <attr name="cornerRadius_rightBottom" format="dimension"/>
 * <attr name="cornerRadius_leftBottom" format="dimension"/>
 * <attr name="isCircular" format="boolean"/>
 * </declare-styleable>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: RoundedImageView.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public class RoundedImageView extends TextView {

    public static final String TAG = "RoundedImageView";

    /**
     * 最小的圆角半径
     */
    public static final float MIN_RADIUS = 0.0f;

    /**
     * 是否是圆形，如果是圆形，则四圆角半径数值无效
     */
    private boolean mCircular;

    /**
     * 四圆角半径集合，左上、左上、右上、右上、右下、右下、左下、左下
     */
    private float[] mCornerRadii = new float[]{0, 0, 0, 0, 0, 0, 0, 0};

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 形状
     */
    private RoundRectShape mShape;

    /**
     * 构造方法：通过Builder方式创建
     */
    private RoundedImageView(Context c) {
        super(c);
    }

    /**
     * 构造方法：通过资源方式创建
     */
    public RoundedImageView(Context c, AttributeSet attrs) {
        super(c, attrs);

        TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.RoundedImageView, 0, 0);

        mCircular = a.getBoolean(R.styleable.RoundedImageView_isCircular, false);

        float leftTopCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius_leftTop, 0);
        float rightTopCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius_rightTop, 0);
        float rightBottomCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius_rightBottom, 0);
        float leftBottomCornerRadius = a.getDimensionPixelSize(R.styleable.RoundedImageView_cornerRadius_leftBottom, 0);

        if (leftTopCornerRadius < MIN_RADIUS || rightTopCornerRadius < MIN_RADIUS
                || rightBottomCornerRadius < MIN_RADIUS || leftBottomCornerRadius < MIN_RADIUS) {
            throw new IllegalArgumentException("radius values cannot be negative.");
        }

        mCornerRadii = new float[]{
                leftTopCornerRadius, leftTopCornerRadius,
                rightTopCornerRadius, rightTopCornerRadius,
                rightBottomCornerRadius, rightBottomCornerRadius,
                leftBottomCornerRadius, leftBottomCornerRadius};

        a.recycle();
        onInitialize();
    }

    /**
     * 初始化画笔
     */
    private void onInitialize() {
        // Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
        setLayerType(LAYER_TYPE_HARDWARE, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    /**
     * 获取是否是圆形
     * 如果是圆形，则四圆角半径数值无效
     *
     * @return Returns True, or False
     */
    public boolean isCircular() {
        return mCircular;
    }

    /**
     * Builder final class
     * Build a new {@link RoundedImageView}.
     */
    public static final class Builder {
        /**
         * 是否是圆形，如果是圆形，则四圆角半径数值无效
         */
        private boolean mCircular;

        /**
         * 左上圆角半径，单位：px
         */
        private float mLeftTopCornerRadius = 0.0f;

        /**
         * 右上圆角半径，单位：px
         */
        private float mRightTopCornerRadius = 0.0f;

        /**
         * 右下圆角半径，单位：px
         */
        private float mRightBottomCornerRadius = 0.0f;

        /**
         * 左下圆角半径，单位：px
         */
        private float mLeftBottomCornerRadius = 0.0f;

        /**
         * 单位转换类
         */
        private final DimensionConverter mConverter;

        /**
         * 上下文环境
         */
        private final Context mContext;

        /**
         * 构造方法：初始化上下文环境、单位转换类
         *
         * @param c 上下文环境
         */
        public Builder(Context c) {
            mContext = c;
            mConverter = DimensionConverter.getInstance(mContext);
        }

        /**
         * 创建RoundedImageView对象
         *
         * @return a RoundedImageView Object
         */
        public RoundedImageView create() {
            RoundedImageView result = new RoundedImageView(mContext);

            result.mCircular = mCircular;

            result.mCornerRadii = new float[]{
                    mLeftTopCornerRadius, mLeftTopCornerRadius,
                    mRightTopCornerRadius, mRightTopCornerRadius,
                    mRightBottomCornerRadius, mRightBottomCornerRadius,
                    mLeftBottomCornerRadius, mLeftBottomCornerRadius};

            result.onInitialize();
            return result;
        }

        /**
         * 设置是否是圆形，如果是圆形，则四圆角半径数值无效
         *
         * @param value 是否是圆形
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCircular(boolean value) {
            mCircular = value;
            return this;
        }

        /**
         * 设置左上圆角半径，单位：dp
         *
         * @param radius 半径，单位：dp
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setLeftTopCornerRadiusDp(float radius) {
            checkRadius(radius);

            mLeftTopCornerRadius = mConverter.dp2px(radius);
            return this;
        }

        /**
         * 设置左上圆角半径，单位：px
         *
         * @param radius 半径，单位：px
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setLeftTopCornerRadius(float radius) {
            checkRadius(radius);

            mLeftTopCornerRadius = radius;
            return this;
        }

        /**
         * 设置右上圆角半径，单位：dp
         *
         * @param radius 半径，单位：dp
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setRightTopCornerRadiusDp(float radius) {
            checkRadius(radius);

            mRightTopCornerRadius = mConverter.dp2px(radius);
            return this;
        }

        /**
         * 设置右上圆角半径，单位：px
         *
         * @param radius 半径，单位：px
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setRightTopCornerRadius(float radius) {
            checkRadius(radius);

            mRightTopCornerRadius = radius;
            return this;
        }

        /**
         * 设置右下圆角半径，单位：dp
         *
         * @param radius 半径，单位：dp
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setRightBottomCornerRadiusDp(float radius) {
            checkRadius(radius);

            mRightBottomCornerRadius = mConverter.dp2px(radius);
            return this;
        }

        /**
         * 设置右下圆角半径，单位：px
         *
         * @param radius 半径，单位：px
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setRightBottomCornerRadius(float radius) {
            checkRadius(radius);

            mRightBottomCornerRadius = radius;
            return this;
        }

        /**
         * 设置左下圆角半径，单位：dp
         *
         * @param radius 半径，单位：dp
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setLeftBottomCornerRadiusDp(float radius) {
            checkRadius(radius);

            mLeftBottomCornerRadius = mConverter.dp2px(radius);
            return this;
        }

        /**
         * 设置左下圆角半径，单位：px
         *
         * @param radius 半径，单位：px
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setLeftBottomCornerRadius(float radius) {
            checkRadius(radius);

            mLeftBottomCornerRadius = radius;
            return this;
        }

        /**
         * 检查圆角半径值是否合法
         *
         * @param radius 半径，单位：dp或px
         * @throws IllegalArgumentException 如果半径值小于0.0f，抛出异常
         */
        private void checkRadius(float radius) throws IllegalArgumentException {
            if (radius < MIN_RADIUS) {
                throw new IllegalArgumentException("radius value cannot be negative.");
            }
        }

    }

}
