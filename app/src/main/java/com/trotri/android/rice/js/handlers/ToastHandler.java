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

package com.trotri.android.rice.js.handlers;

import android.widget.Toast;

import com.trotri.android.rice.js.Bridge;
import com.trotri.android.rice.js.HandlerFactory;

/**
 * ToastHandler class file
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ToastHandler.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class ToastHandler implements HandlerFactory.BaseHandler {
    /**
     * Js方法名
     */
    public static final String METHOD_NAME = "toast";

    @Override
    public void run(Bridge bridge, String key, String parameter) {
        Parameter p = bridge.convertParameterThrowException(key, parameter, Parameter.class);
        if (p == null) {
            return;
        }

        Toast.makeText(bridge.getContext(), p.mText, (p.mIsLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT)).show();
        bridge.loadListen(key, "");
    }

    /**
     * Js参数
     */
    class Parameter {
        /**
         * 提示内容
         */
        private String mText;

        /**
         * 展示时长
         */
        private boolean mIsLong;
    }

}
