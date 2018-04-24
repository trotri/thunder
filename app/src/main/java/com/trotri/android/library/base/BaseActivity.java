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

package com.trotri.android.library.base;

import com.trotri.android.java.sample.R;
import com.trotri.android.rice.base.AbstractActivity;

/**
 * BaseActivity abstract class file
 * Activity基类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BaseActivity.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class BaseActivity extends AbstractActivity {

    public static final String TAG = "BaseActivity";

    /**
     * 默认的Activity Layout Id
     */
    public static final int DEFAULT_ACTIVITY_LAYOUT_ID = R.layout.activity_fragment_container;

    /**
     * 默认的Fragment Container View Id，仅有一个Fragment时使用
     */
    public static final int DEFAULT_FRAGMENT_CONTAINER_VIEW_ID = R.id.flay_fragment_container;

    @Override
    protected int getActivityLayoutId() {
        return DEFAULT_ACTIVITY_LAYOUT_ID;
    }

    @Override
    protected int getFragmentContainerId() {
        return DEFAULT_FRAGMENT_CONTAINER_VIEW_ID;
    }

}
