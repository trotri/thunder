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

package com.trotri.android.rice.util;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;

/**
 * GsonHelper class file
 * Gson辅助类，适配m前缀的属性名
 * 需要包：
 * compile 'com.google.code.gson:gson:2.8.0'
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: GsonHelper.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class GsonHelper {
    /**
     * 属性名前缀
     */
    public static final String FIELD_NAME_PREFIX = "m";

    /**
     * Gson属性名处理器
     */
    public static FieldNamingStrategy sFieldNameStrategy = new FieldNamingStrategy() {
        @Override
        public String translateName(Field f) {
            String fieldName = f.getName();
            return removeFieldNamePrefix(fieldName);
        }
    };

    /**
     * a Gson Object
     */
    private static Gson sGson;

    /**
     * Create the {@link Gson} instance.
     *
     * @return a Gson Object
     */
    public static Gson create() {
        if (sGson == null) {
            sGson = (new GsonBuilder()).setFieldNamingStrategy(sFieldNameStrategy).create();
        }

        return sGson;
    }

    /**
     * 删除属性名前缀，前缀：m
     *
     * @param fieldName 原始属性名
     * @return 删除前缀的属性名
     */
    public static String removeFieldNamePrefix(String fieldName) {
        if (fieldName != null && fieldName.startsWith(FIELD_NAME_PREFIX) && fieldName.length() > 1) {
            fieldName = fieldName.substring(1);
            fieldName = firstToLowerCase(fieldName);
        }

        return fieldName;
    }

    /**
     * 将字符串首字母小写
     *
     * @param s 原始字符串
     * @return 首字母小写字符串
     */
    public static String firstToLowerCase(String s) {
        if (s != null && s.length() > 0) {
            s = s.substring(0, 1).toLowerCase() + s.substring(1);
        }

        return s;
    }

}
