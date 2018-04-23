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

import com.trotri.android.rice.bean.Constants;
import com.trotri.android.rice.bean.ErrorMsg;
import com.trotri.android.rice.bean.ErrorNo;
import com.trotri.android.rice.util.ActionManager;
import com.trotri.android.thunder.ap.Logger;

import io.reactivex.functions.Consumer;

/**
 * AbstractDataProcessor abstract class file
 * 数据处理器基类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: AbstractDataProcessor.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class AbstractDataProcessor {

    public static final String TAG = "AbstractDataProcessor";

    /**
     * MVVM模式：RxJava方法管理类
     */
    private ActionManager mActionManager = new ActionManager();

    /**
     * 绑定监听方法
     *
     * @param name 方法名
     * @param func RxJava方法，a Consumer<ActionManager.Result> Object
     */
    public void bind(String name, Consumer<ActionManager.Result> func) {
        mActionManager.add(name, func);
    }

    /**
     * 执行RxJava方法：错误码 = 成功码、错误消息 = 成功消息
     *
     * @param name 方法名
     */
    public void post(String name) {
        post(name, ErrorNo.SUCCESS_NUM, ErrorMsg.SUCCESS_MSG);
    }

    /**
     * 通知监听执行
     *
     * @param name   方法名
     * @param errNo  错误码
     * @param errMsg 错误消息
     */
    public void post(String name, int errNo, String errMsg) {
        try {
            mActionManager.exec(name, errNo, errMsg);
        } catch (ClassNotFoundException | RuntimeException e) {
            Logger.e(Constants.TAG_LOG, TAG + " post() name: '" + name + "' not found", e);
        }
    }

}
