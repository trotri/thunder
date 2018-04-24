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

import com.trotri.android.library.App;
import com.trotri.android.rice.db.retrofit.DbRetrofit;
import com.trotri.android.rice.util.DbRetrofitBuilder;

/**
 * BaseDb abstract class file
 * Db基类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: BaseDb.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class BaseDb {

    public static final String TAG = "BaseDb";

    /**
     * DbRetrofit对象
     */
    private static DbRetrofit sRetrofit;

    /**
     * 获取DbRetrofit对象
     *
     * @return a DbRetrofit Object
     */
    public static DbRetrofit getRetrofit() {
        if (sRetrofit == null) {
            sRetrofit = new DbRetrofitBuilder(App.getContext())
                    .build();
        }

        return sRetrofit;
    }

}
