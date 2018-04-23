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

package com.trotri.android.rice.js;

import android.support.annotation.NonNull;

import com.trotri.android.rice.js.handlers.ToastHandler;
import com.trotri.android.rice.js.handlers.VersionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * HandlerFactory class file
 * Js处理器工厂
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: HandlerFactory.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class HandlerFactory {
    /**
     * Js处理器寄存器
     */
    private static Map<String, BaseHandler> sHandlers = new HashMap<>();

    /**
     * 获取Js处理器
     *
     * @param method Js方法名
     * @return Js处理器, or null
     */
    public static BaseHandler getHandler(@NonNull String method) {
        BaseHandler handler = sHandlers.get(method);
        if (handler != null) {
            return handler;
        }

        handler = newHandler(method);
        if (handler != null) {
            sHandlers.put(method, handler);
        }

        return handler;
    }

    /**
     * 实例化一个Js处理器
     *
     * @param method Js方法名
     * @return Js处理器, or null
     */
    public static BaseHandler newHandler(@NonNull String method) {
        BaseHandler handler = null;

        if (ToastHandler.METHOD_NAME.equalsIgnoreCase(method)) {
            handler = new ToastHandler();
        } else if (VersionHandler.METHOD_NAME.equalsIgnoreCase(method)) {
            handler = new VersionHandler();
        }

        return handler;
    }

    /**
     * BaseHandler interface
     * Js处理器基类
     *
     * @since 1.0
     */
    public interface BaseHandler {
        /**
         * 执行处理
         *
         * @param bridge    a Bridge Object
         * @param key       回调函数Key
         * @param parameter a Json String, or null
         */
        void run(Bridge bridge, String key, String parameter);
    }

}
