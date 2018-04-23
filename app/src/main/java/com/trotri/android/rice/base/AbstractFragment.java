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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trotri.android.rice.bean.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.base.AbstractViewModel;
import com.trotri.android.thunder.inj.ViewModelInject;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AbstractFragment abstract class file
 * Fragment基类
 * 需要包：
 * Android Support Library V4
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: AbstractFragment.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class AbstractFragment<VM extends AbstractViewModel> extends Fragment {

    public static final String TAG = "AbstractFragment";

    /**
     * 重绘前，持久视图Id值的Key
     */
    private static final String LOADER_ID_SAVED_STATE = "loader_id_state";

    /**
     * 视图Id生成器
     */
    private static final AtomicInteger VIEW_COUNTER = new AtomicInteger(0);

    /**
     * View Model
     */
    @Nullable
    protected VM mViewModel;

    /**
     * 视图Id
     */
    private int mUniqueLoaderIdentifier;

    /**
     * Fragment名
     */
    private String mFragmentName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentName = getClass().getName();
        mUniqueLoaderIdentifier = (savedInstanceState == null) ? VIEW_COUNTER.incrementAndGet() : savedInstanceState.getInt(LOADER_ID_SAVED_STATE);

        Logger.d(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::onCreate() loader identifier: " + mUniqueLoaderIdentifier + ", fragment layout id: " + getFragmentLayoutId());
        injectDependencies();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logger.d(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::onCreateView()");

        return inflater.inflate(getFragmentLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Logger.d(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::onViewCreated()");

        if (mViewModel != null) {
            mViewModel.onCreate(getArguments());
        }
    }

    @Override
    public void onDestroyView() {
        Logger.d(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::onDestroyView()");

        if (mViewModel != null) {
            mViewModel.onDestroyed();
        }

        super.onDestroyView();
    }

    /**
     * 执行{@link #injectDependencies}后调用该方法
     * 此时onViewCreated还未执行，不可处理与View相关的操作
     */
    public void onViewModelCreated() {
        Logger.d(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::onViewModelCreated()");

        if (mViewModel == null) {
            throw new RuntimeException(getFragmentName() + " mViewModel is null");
        }
    }

    /**
     * 依赖注入，初始化mViewModel
     */
    protected void injectDependencies() {
        try {
            processViewModelInject();
        } catch (IllegalArgumentException e) {
            Logger.e(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::injectDependencies()", e);
        }

        try {
            onViewModelCreated();
        } catch (RuntimeException e) {
            Logger.e(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::injectDependencies()", e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Logger.d(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::onStart()");

        if (mViewModel != null) {
            mViewModel.onStart();
        }
    }

    @Override
    public void onStop() {
        Logger.d(Constants.TAG_LOG, TAG + " " + getFragmentName() + "::onStop()");

        if (mViewModel != null) {
            mViewModel.onStop();
        }

        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(LOADER_ID_SAVED_STATE, mUniqueLoaderIdentifier);
    }

    /**
     * 处理ViewModelInject注解
     * 注解方式，从属性注释中获取ViewModel的Class，并从Class获取ViewModel对象，最终将ViewModel对象设置为属性值
     *
     * @throws IllegalArgumentException the ViewModelInject is null
     */
    protected void processViewModelInject() throws IllegalArgumentException {
        ViewModelInject annotation = getClass().getAnnotation(ViewModelInject.class);
        if (annotation == null) {
            throw new IllegalArgumentException("ViewModelInject is null");
        }

        Class<VM> cls = (Class<VM>) annotation.value();
        try {
            mViewModel = cls.newInstance();
        } catch (java.lang.InstantiationException e) {
            Logger.e(Constants.TAG_LOG, TAG + " processViewModelInject() process failure", e);
        } catch (IllegalAccessException e) {
            Logger.e(Constants.TAG_LOG, TAG + " processViewModelInject() process failure", e);
        }
    }

    /**
     * 获取视图Id
     *
     * @return 视图Id
     */
    protected int getUniqueLoaderIdentifier() {
        return mUniqueLoaderIdentifier;
    }

    /**
     * 获取Fragment名
     *
     * @return Fragment名
     */
    protected String getFragmentName() {
        return mFragmentName;
    }

    /**
     * 获取Fragment Layout Id
     *
     * @return Fragment Layout Id
     */
    protected abstract int getFragmentLayoutId();

}
