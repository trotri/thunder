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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * GridItemDecoration class file
 * RecyclerView GridLayout StaggeredGrid ItemDecoration 类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: GridItemDecoration.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class GridItemDecoration extends ItemDecoration {
    /**
     * 绘制垂直分隔线
     *
     * @param divider 分隔线
     * @param c       a Canvas Object
     * @param parent  a RecyclerView Object
     */
    public void drawVerticalDivider(Drawable divider, Canvas c, RecyclerView parent) {
        int top, bottom, left, right;
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
            top = v.getTop() - params.topMargin;
            bottom = v.getBottom() + params.bottomMargin;
            left = v.getRight() + params.rightMargin;
            right = left + divider.getIntrinsicWidth();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    /**
     * 绘制水平分隔线
     *
     * @param divider 分隔线
     * @param c       a Canvas Object
     * @param parent  a RecyclerView Object
     */
    public void drawHorizontalDivider(Drawable divider, Canvas c, RecyclerView parent) {
        int top, bottom, left, right;
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
            left = v.getLeft() - params.leftMargin;
            right = v.getRight() + params.rightMargin + divider.getIntrinsicWidth();
            top = v.getBottom() + params.bottomMargin;
            bottom = top + divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        super.getItemOffsets(outRect, itemPosition, parent);
    }

}
