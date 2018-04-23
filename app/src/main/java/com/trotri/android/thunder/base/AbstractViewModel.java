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

package com.trotri.android.thunder.base;

import android.os.Bundle;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * AbstractViewModel abstract class file
 * ViewModel基类，需要子类继承后使用
 * <p>
 * 避免混淆，proguard-rules.pro:
 * -keepclassmembers class 包名.AbstractViewModel { *; }
 * </p>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: AbstractViewModel.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class AbstractViewModel {

    public static final String TAG = "AbstractViewModel";

    /**
     * 当View执行onViewCreated()后调用该方法
     *
     * @param args The construction arguments for this fragment.
     */
    public void onCreate(Bundle args) {
        Logger.d(Constants.TAG_LOG, TAG + " " + getClassName() + "::onCreate()");
    }

    /**
     * 当View执行onStart()后调用该方法
     */
    public void onStart() {
        Logger.d(Constants.TAG_LOG, TAG + " " + getClassName() + "::onStart()");
    }

    /**
     * 当View执行onStop()前调用该方法
     */
    public void onStop() {
        Logger.d(Constants.TAG_LOG, TAG + " " + getClassName() + "::onStop()");
    }

    /**
     * 当View执行onDestroyView()前调用该方法
     * 销毁ViewModel前释放资源（cancel HTTP requests, close database connection...）
     */
    public void onDestroyed() {
        Logger.d(Constants.TAG_LOG, TAG + " " + getClassName() + "::onDestroyed()");
    }

    /**
     * 获取子类名
     *
     * @return 类名
     */
    public String getClassName() {
        return getClass().getName();
    }

}
