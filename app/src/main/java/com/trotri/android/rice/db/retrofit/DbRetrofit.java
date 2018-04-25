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

import android.content.Context;

import com.google.gson.Gson;
import com.trotri.android.rice.db.DbStorage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

/**
 * DbRetrofit class file
 * 类似Retrofit，处理Db相关操作
 * <pre>
 * public interface IBook {
 *     @GET("book_list")
 *     Observable<RequestAdapter.ResultList<BookBean>> findRows();
 *
 *     @SET("book_list")
 *     boolean putRows(RequestAdapter.ResultList<BookBean> value);
 *
 *     @GET("book_id_")
 *     Observable<RequestAdapter.Result<BookBean>> getRow(long id);
 *
 *     @SET("book_id_")
 *     boolean setRow(long id, RequestAdapter.Result<BookBean> value);
 * }
 *
 * IBook db = new DbRetrofitBuilder(App.getContext()).build().create(IBook.class);
 * </pre>
 * 需要包：
 * compile 'com.google.code.gson:gson:2.8.0'
 * compile 'io.reactivex.rxjava2:rxjava:2.1.7'
 * compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DbRetrofit.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class DbRetrofit {

    public static final String TAG = "DbRetrofit";

    /**
     * DbRetrofit辅助类
     */
    public Helper mHelper;

    /**
     * 动态代理，数据json和object或Observable<object>转换
     *
     * @param service 动态代理的接口
     * @param <T>     返回值类型，泛型
     * @return 动态代理的方法的返回值，获取数据：返回object或Observable<object>、设置数据：返回boolean
     */
    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                GET gInject = method.getAnnotation(GET.class);
                if (gInject != null) {
                    if (Util.isObservable(method)) {
                        Type type = Util.getReturnType(method, true);
                        return Util.toObservable(mHelper.get(gInject.value(), args, type));
                    }

                    Type type = Util.getReturnType(method, false);
                    return mHelper.get(gInject.value(), args, type);
                }

                SET sInject = method.getAnnotation(SET.class);
                if (sInject != null) {
                    return mHelper.set(sInject.value(), args);
                }

                throw new Exception("no inject or inject wrong, must be @GET(key prefix) or @SET(key prefix)");
            }
        });
    }

    /**
     * 获取DbRetrofit辅助类
     *
     * @return DbRetrofit辅助类，a Helper Object
     */
    public Helper getHelper() {
        return mHelper;
    }

    /**
     * Builder final class
     * Build a new {@link DbRetrofit}.
     *
     * @since 1.0
     */
    public static final class Builder {
        /**
         * Gson类
         */
        private Gson mGson;

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
        public Builder(Context c) {
            mAppContext = c.getApplicationContext();
        }

        /**
         * 设置Gson对象
         *
         * @param gson a Gson Object
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public DbRetrofit.Builder setGson(Gson gson) {
            mGson = gson;
            return this;
        }

        /**
         * 设置Db名的后缀
         *
         * @param dbPostfix Db名的后缀，Db名：包名 + 后缀名
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public DbRetrofit.Builder setDbPostfix(String dbPostfix) {
            mDbPostfix = dbPostfix;
            return this;
        }

        /**
         * 创建DbRetrofit对象
         *
         * @return DbRetrofit对象，a DbRetrofit Object
         */
        public DbRetrofit build() {
            DbRetrofit dbRetrofit = new DbRetrofit();

            if (mGson == null) {
                mGson = new Gson();
            }

            DbStorage storage = new DbStorage(mAppContext, mDbPostfix);
            dbRetrofit.mHelper = new Helper(mGson, storage);

            return dbRetrofit;
        }

    }

}
