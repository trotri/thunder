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

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitBuilder class file
 * Build a new {@link Retrofit}.
 * 需要包：
 * compile 'com.google.code.gson:gson:2.8.0'
 * compile 'io.reactivex.rxjava2:rxjava:2.1.7'
 * compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
 * compile 'com.squareup.okhttp3:okhttp:3.7.0'
 * compile 'com.squareup.retrofit2:retrofit:2.2.0'
 * compile 'com.squareup.retrofit2:converter-gson:2.2.0'
 * compile 'com.squareup.retrofit2:adapter-rxjava2:2.2.0'
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: RetrofitBuilder.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class RetrofitBuilder {
    /**
     * 访问方式
     */
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    /**
     * Url前缀
     */
    private String mBaseUrl;

    /**
     * 全局表单
     */
    private FormBody.Builder mQueryStr = new FormBody.Builder();

    /**
     * Create the {@link Retrofit} instance using the configured values.
     *
     * @return a Retrofit Object
     */
    public Retrofit build() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(getInterceptor()).build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonHelper.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client);

        return builder.build();
    }

    /**
     * 获取OkHttp拦截器
     *
     * @return OkHttp拦截器
     */
    public Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().post(mQueryStr.build()).build();
                return chain.proceed(request);
            }
        };
    }

    /**
     * 设置Url前缀
     *
     * @param baseUrl Url前缀
     * @return Returns a reference to the same RetrofitBuilder object, so you can chain put calls together.
     */
    public RetrofitBuilder baseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }

    /**
     * 全局表单添加字段
     *
     * @param key   Form Key
     * @param value Form Value
     * @return Returns a reference to the same RetrofitBuilder object, so you can chain put calls together.
     */
    public RetrofitBuilder addQuery(String key, String value) {
        mQueryStr.add(key, value);
        return this;
    }

}
