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

package com.trotri.android.rice.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.trotri.android.rice.bean.Constants;
import com.trotri.android.rice.util.FragmentHelper;
import com.trotri.android.thunder.ap.Logger;

import java.util.Map;

/**
 * AbstractActivity abstract class file
 * Activity基类，Fragment完全取代Activity处理View业务，Activity只处理Fragment业务
 * 需要包：
 * Android Support Library V4
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: AbstractActivity.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class AbstractActivity extends FragmentActivity {

    public static final String TAG = "AbstractActivity";

    /**
     * Fragment辅助类
     */
    private FragmentHelper mFragmentHelper;

    /**
     * Activity中是否包含多个Fragment，需要通过子类重写
     *
     * @return Returns True, or False
     */
    protected abstract boolean hasMultiFragments();

    /**
     * 获取一个Fragment对象，仅在Activity中包含一个Fragment时起作用，需要通过子类重写
     *
     * @return a Fragment Object
     */
    protected abstract Fragment newFragment();

    /**
     * 获取多个Fragment对象，仅在Activity中包含多个Fragment时起作用，需要通过子类重写
     *
     * @return a Map Key => Fragment Container View Id, Value => a Fragment
     */
    protected abstract Map<Integer, Fragment> newFragments();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentHelper = new FragmentHelper(getSupportFragmentManager(), getClass().getName());
        Logger.d(Constants.TAG_LOG, TAG + " " + mFragmentHelper.getActivityName() + "::onCreate()");

        if (preContentView()) {
            Logger.d(Constants.TAG_LOG, TAG + " " + mFragmentHelper.getActivityName() + "::onCreate() setContentView, activity layout id: " + getActivityLayoutId());
            setContentView(getActivityLayoutId());
        }

        run();
    }

    @Override
    public Resources getResources() {
        Resources r = super.getResources();

        // 文字大小不受操作系统影响
        Configuration c = new Configuration();
        c.setToDefaults();
        r.updateConfiguration(c, r.getDisplayMetrics());

        return r;
    }

    @Override
    public void onBackPressed() {
        Logger.d(Constants.TAG_LOG, TAG + " " + mFragmentHelper.getActivityName() + "::onBackPressed()");

        super.onBackPressed();
        finish();
    }

    /**
     * 需要子类重写此方法
     * 在Activity调用setContentView方法前调用的方法
     * 调用此方法后一定要返回true，后面方法才会执行
     *
     * @return Returns True, or False
     */
    protected boolean preContentView() {
        Logger.d(Constants.TAG_LOG, TAG + " " + mFragmentHelper.getActivityName() + "::preContentView()");
        return true;
    }

    /**
     * 执行Fragment业务
     */
    protected void run() {
        if (!preRun()) {
            return;
        }

        Logger.d(Constants.TAG_LOG, TAG + " " + mFragmentHelper.getActivityName() + "::run()");

        processFragment();
        postRun();
    }

    /**
     * 需要子类重写此方法
     * 在Activity调用run方法前调用的方法
     * 调用此方法后一定要返回true，后面方法才会执行
     *
     * @return Returns True, or False
     */
    protected boolean preRun() {
        Logger.d(Constants.TAG_LOG, TAG + " " + mFragmentHelper.getActivityName() + "::preRun()");
        return true;
    }

    /**
     * 需要子类重写此方法
     * 在Activity调用run方法后调用的方法
     */
    protected void postRun() {
        Logger.d(Constants.TAG_LOG, TAG + " " + mFragmentHelper.getActivityName() + "::postRun()");
    }

    /**
     * 在Activity中仅包含一个Fragment时，添加Fragment
     */
    protected void processFragment() {
        if (hasMultiFragments()) {
            batchReplaceFragments();
        } else {
            replaceFragment();
        }
    }

    /**
     * 在Activity中仅包含一个Fragment时，添加Fragment
     */
    protected void replaceFragment() {
        getFragmentHelper().replace(getFragmentContainerId(), newFragment());
    }

    /**
     * 在Activity中包含多个Fragment时，添加Fragment
     */
    protected void batchReplaceFragments() {
        getFragmentHelper().batchReplace(newFragments());
    }

    /**
     * 获取Fragment辅助类
     *
     * @return a FragmentHelper Object
     */
    protected FragmentHelper getFragmentHelper() {
        return mFragmentHelper;
    }

    /**
     * 获取Activity名
     *
     * @return Activity名
     */
    protected String getActivityName() {
        return getFragmentHelper().getActivityName();
    }

    /**
     * 获取Activity Layout Id
     *
     * @return Activity Layout Id
     */
    protected abstract int getActivityLayoutId();

    /**
     * 获取Fragment Container View Id，仅有一个Fragment时使用
     *
     * @return Fragment Container View Id
     */
    protected abstract int getFragmentContainerId();

}
