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

package com.trotri.android.library.data;

import com.trotri.android.rice.base.AbstractDataProcessor;
import com.trotri.android.thunder.ap.Logger;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * AbstractDetailProcessor abstract class file
 * 详情数据处理基类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: AbstractDetailProcessor.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class AbstractDetailProcessor<T> extends AbstractDataProcessor {

    public static final String TAG = "AbstractDetailProcessor";

    /**
     * 详情数据
     */
    private T mData;

    /**
     * 是否正在请求中
     */
    private boolean mLoading;

    /**
     * 请求详情数据
     *
     * @param param 参数
     * @param <P>   参数类型
     * @return 结果集，an Observable<RequestAdapter.Result<T>> Object
     */
    protected abstract <P> Observable<RequestAdapter.Result<T>> request(P param);

    /**
     * 请求数据
     *
     * @param param 参数
     * @param <P>   参数类型
     */
    public <P> void load(P param) {
        if (mLoading) {
            return;
        }

        mLoading = true;
        post(BIND_STATUS.LOADING.name());

        request(param).subscribe(new Consumer<RequestAdapter.Result<T>>() {
            @Override
            public void accept(RequestAdapter.Result<T> result) throws Exception {
                processResult(result);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable tr) throws Exception {
                Logger.e(Constants.TAG_LOG, TAG + " load()", tr);
                processResult(null);
            }
        });
    }

    /**
     * 处理请求结果
     *
     * @param result 请求结果，a RequestAdapter.Result<T> Object
     */
    protected void processResult(RequestAdapter.Result<T> result) {
        mLoading = false;

        // 请求失败，系统错误
        if (result == null) {
            post(BIND_STATUS.LOAD_FAILURE.name(), ErrorNo.ERROR_UNKNOWN, ErrorMsg.ERROR_UNKNOWN);
            return;
        }

        // 请求失败
        if (result.getErrNo() != ErrorNo.SUCCESS_NUM) {
            post(BIND_STATUS.LOAD_FAILURE.name(), result.getErrNo(), result.getErrMsg());
            return;
        }

        mData = result.getData();

        // 请求接口成功，但没有数据
        if (mData == null) {
            post(BIND_STATUS.NO_DATA.name(), ErrorNo.ERROR_RESULT_EMPTY, ErrorMsg.ERROR_RESULT_EMPTY);
            return;
        }

        post(BIND_STATUS.LOAD_SUCCESS.name());
    }

    /**
     * 获取详情数据
     *
     * @return 详情数据，a T Object, or null
     */
    public T getData() {
        return mData;
    }

    /**
     * 请求接口状态
     */
    public enum BIND_STATUS {
        /**
         * 请求中，正在请求数据
         */
        LOADING,

        /**
         * 请求成功
         */
        LOAD_SUCCESS,

        /**
         * 请求接口成功，但没有数据
         */
        NO_DATA,

        /**
         * 请求失败，通过错误码和错误消息获取失败原因
         */
        LOAD_FAILURE
    }

}
