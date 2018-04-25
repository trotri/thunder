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
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.trotri.android.rice.view.recycler.decoration.LinearItemDecoration;

/**
 * StandardItemDecoration class file
 * 标准的 RecyclerView LinearLayout ItemDecoration 类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: StandardItemDecoration.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public class StandardItemDecoration extends LinearItemDecoration {
    /**
     * 分割线类型
     */
    private final int[] ATTRS = new int[]{android.R.attr.listDivider};

    /**
     * 分割线资源
     */
    private Drawable mDivider;

    /**
     * 构造方法：初始化分割线资源
     *
     * @param c 上下文环境
     */
    public StandardItemDecoration(Context c) {
        final TypedArray a = c.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawVerticalDivider(mDivider, c, parent);
    }

}
