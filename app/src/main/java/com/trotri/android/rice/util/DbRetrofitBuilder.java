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

import android.content.Context;

import com.trotri.android.rice.db.retrofit.DbRetrofit;

/**
 * DbRetrofitBuilder class file
 * Build a new {@link DbRetrofit}.
 * 需要包：
 * compile 'com.google.code.gson:gson:2.8.0'
 * compile 'io.reactivex.rxjava2:rxjava:2.1.7'
 * compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DbRetrofitBuilder.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class DbRetrofitBuilder {
    /**
     * Db名的后缀，Db名：包名 + 后缀名
     */
    private String mDbPostfix;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境
     *
     * @param c 上下文环境
     */
    public DbRetrofitBuilder(Context c) {
        mAppContext = c.getApplicationContext();
    }

    /**
     * Create the {@link DbRetrofit} instance using the configured values.
     *
     * @return a DbRetrofit Object
     */
    public DbRetrofit build() {
        DbRetrofit.Builder builder = new DbRetrofit.Builder(mAppContext)
                .setDbPostfix(mDbPostfix)
                .setGson(GsonHelper.create());

        return builder.build();
    }

    /**
     * 设置Db名的后缀
     *
     * @param dbPostfix Db名的后缀，Db名：包名 + 后缀名
     * @return Returns a reference to the same RetrofitBuilder object, so you can chain put calls together.
     */
    public DbRetrofitBuilder dbPostfix(String dbPostfix) {
        mDbPostfix = dbPostfix;
        return this;
    }

}
