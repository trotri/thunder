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

import com.trotri.android.rice.js.Bridge;
import com.trotri.android.rice.js.HandlerFactory;
import com.trotri.android.thunder.state.Version;

/**
 * VersionHandler class file
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: VersionHandler.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class VersionHandler implements HandlerFactory.BaseHandler {
    /**
     * Js方法名
     */
    public static final String METHOD_NAME = "getVersion";

    @Override
    public void run(Bridge bridge, String key, String parameter) {
        Version v = Version.getInstance(bridge.getContext());
        String data = bridge.convertData(new Data(v.getCode(), v.getName()));
        bridge.loadListen(key, data);
    }

    /**
     * 回调数据
     */
    class Data {
        /**
         * 版本号，从1开始
         */
        private int mCode;

        /**
         * 版本名
         */
        private String mName;

        /**
         * 构造方法：初始化版本号、版本名
         *
         * @param code 版本号
         * @param name 版本名
         */
        public Data(int code, String name) {
            mCode = code;
            mName = name;
        }

    }

}
