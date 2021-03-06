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

package com.trotri.android.library.base;

import com.trotri.android.java.sample.R;
import com.trotri.android.library.App;
import com.trotri.android.library.data.Constants;
import com.trotri.android.rice.util.RetrofitBuilder;

import retrofit2.Retrofit;

/**
 * BaseHttp abstract class file
 * Http基类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BaseHttp.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class BaseHttp {

    public static final String TAG = "BaseHttp";

    /**
     * Retrofit对象
     */
    private static Retrofit sRetrofit;

    /**
     * 获取Retrofit对象
     *
     * @return a Retrofit Object
     */
    public static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = new RetrofitBuilder()
                    .baseUrl(baseUrl())
                    .addQuery("version", Constants.VERSION)
                    .addQuery("os", Constants.OS)
                    .build();
        }

        return sRetrofit;
    }

    /**
     * Obtain Base Url
     *
     * @return Base Url
     */
    public static String baseUrl() {
        return App.getContext().getString(R.string.base_url);
    }

}
