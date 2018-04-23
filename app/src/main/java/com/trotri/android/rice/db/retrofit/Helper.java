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

package com.trotri.android.rice.db.retrofit;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.trotri.android.rice.db.DbStorage;

import java.lang.reflect.Type;

/**
 * Helper class file
 * DbRetrofit辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Helper.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class Helper {

    public static final String TAG = "Helper";

    /**
     * Gson类
     */
    private Gson mGson;

    /**
     * Db数据寄存类
     */
    private DbStorage mStorage;

    /**
     * 构造方法：初始化Gson类、Db数据寄存类
     *
     * @param gson    a Gson Object
     * @param storage a DbStorage Object
     */
    public Helper(@NonNull Gson gson, @NonNull DbStorage storage) {
        mGson = gson;
        mStorage = storage;
    }

    /**
     * 从Db中获取数据
     *
     * @param prefix  键名前缀，键名：键名前缀 + 参数列表
     * @param args    参数列表
     * @param typeOfT 返回数据类型，用于Gson
     * @param <T>     返回数据类型，泛型
     * @return Db中的数据，转换成泛型指定的对象
     * @throws Exception 如果获取键名失败，抛出异常
     * @throws Exception 如果Json转换失败，抛出异常
     */
    public <T> T get(String prefix, Object[] args, Type typeOfT) throws Exception {
        String key = Util.getKey(prefix, args, false);
        if (TextUtils.isEmpty(key)) {
            throw new Exception("@GET(key prefix), prefix is empty");
        }

        String value = mStorage.get(key, "");
        if (TextUtils.isEmpty(value)) {
            return null;
        }

        return fromJson(value, typeOfT);
    }

    /**
     * 保存数据到Db中
     *
     * @param prefix 键名前缀，键名：键名前缀 + 参数列表（除了最后一个参数，最后一个参数是Value）
     * @param args   参数列表，最后一个参数是Value
     * @return Returns True, or False
     * @throws Exception 如果获取键名失败，抛出异常
     */
    public boolean set(String prefix, Object[] args) throws Exception {
        String key = Util.getKey(prefix, args, true);
        if (TextUtils.isEmpty(key)) {
            throw new Exception("@SET(key prefix), prefix is empty");
        }

        Object value = Util.getValue(args);
        return (value != null) && mStorage.put(key, getGson().toJson(value));
    }

    /**
     * Json字符串转Bean对象
     *
     * @param json    the string from which the object is to be deserialized
     * @param typeOfT The specific genericized type of src
     * @param <T>     the type of the desired object
     * @return an object of type T from the string
     * @throws Exception 如果Json转换失败，抛出异常
     */
    public <T> T fromJson(String json, Type typeOfT) throws Exception {
        try {
            return getGson().fromJson(json, typeOfT);
        } catch (JsonSyntaxException e) {
            throw new Exception("json convert failure, json: " + json);
        } catch (JsonParseException e) {
            throw new Exception("json convert failure, json: " + json);
        }
    }

    /**
     * 获取Gson对象
     *
     * @return Gson对象，a Gson Object
     */
    public Gson getGson() {
        return mGson;
    }

    /**
     * 获取Db数据寄存类
     *
     * @return Db寄存类，a DbStorage Object
     */
    public DbStorage getStorage() {
        return mStorage;
    }

}
