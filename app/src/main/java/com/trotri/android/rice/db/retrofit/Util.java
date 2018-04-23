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

import android.text.TextUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Util class file
 * DbRetrofit工具类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Util.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class Util {

    public static final String TAG = "Util";

    /**
     * 获取键名，键名：键名前缀 + 参数列表（SET：除了最后一个参数，最后一个参数是Value）
     *
     * @param prefix 键名前缀
     * @param args   参数列表
     * @param isSet  是否是SET
     * @return 键名，或""，用于从Db中获取数据
     */
    public static String getKey(String prefix, Object[] args, boolean isSet) {
        if (TextUtils.isEmpty(prefix)) {
            return "";
        }

        if (args == null) {
            return prefix;
        }

        int len = args.length;
        if (len <= 0) {
            return prefix;
        }

        StringBuilder data = new StringBuilder();

        data.append(prefix);
        for (int i = 0; i < len; i++) {
            if (isSet && i == (len - 1)) {
                break;
            }

            data.append(args[i]);
        }

        return data.toString();
    }

    /**
     * 从参数列表中获取数据
     *
     * @param args 参数列表，最后一个参数是Value
     * @return 值，或null，用于保存到Db中
     */
    public static Object getValue(Object[] args) {
        if (args == null) {
            return null;
        }

        int len = args.length;
        if (len <= 0) {
            return null;
        }

        return args[len - 1];
    }

    /**
     * Convert T to Observable<T>
     *
     * @param value a T Object
     * @param <T>   the type of the desired object
     * @return Observable<T>，an object of type Observable<T> from the value
     */
    public static <T> Observable<T> toObservable(@NonNull final T value) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                e.onNext(value);
            }
        });
    }

    /**
     * 判断返回值类型是否是Observable对象
     *
     * @param method 动态代理，反射的方法
     * @return Returns True, or False
     */
    public static boolean isObservable(@NonNull Method method) {
        Class<?> returnType = method.getReturnType();
        return returnType == Observable.class;
    }

    /**
     * 获取反射方法的返回值类型
     *
     * @param method       动态代理，反射的方法
     * @param isObservable 返回值类型是否是Observable对象
     * @return 返回值类型，如果是Observable对象，返回Observable中的泛型类型
     */
    public static Type getReturnType(@NonNull Method method, boolean isObservable) {
        Type type = method.getGenericReturnType();
        if (isObservable) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] arguments = parameterizedType.getActualTypeArguments();
            return arguments[0];
        }

        return type;
    }

}
