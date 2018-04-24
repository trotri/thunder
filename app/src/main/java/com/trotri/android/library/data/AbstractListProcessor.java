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
 * AbstractListProcessor abstract class file
 * 列表数据处理基类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: AbstractListProcessor.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public abstract class AbstractListProcessor<T> extends AbstractDataProcessor {

    public static final String TAG = "AbstractListProcessor";

    /**
     * 列表数据提供者
     */
    private ListProvider<T> mProvider;

    /**
     * 是否正在请求中
     */
    private boolean mLoading;

    /**
     * 请求列表数据
     *
     * @return 结果集，an Observable<RequestAdapter.ResultList<T>> Object
     */
    protected abstract Observable<RequestAdapter.ResultList<T>> request();

    /**
     * 请求数据
     *
     * @param reset 是否清空当前数据，下拉刷新时：reset = true
     */
    public void load(final boolean reset) {
        if (mLoading) {
            return;
        }

        mLoading = true;
        post(BIND_STATUS.LOADING.name());

        if (reset) {
            clear();
        }

        request().subscribe(new Consumer<RequestAdapter.ResultList<T>>() {
            @Override
            public void accept(RequestAdapter.ResultList<T> result) throws Exception {
                processResult(result);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable tr) throws Exception {
                Logger.e(Constants.TAG_LOG, TAG + " load() reset: " + reset, tr);
                processResult(null);
            }
        });
    }

    /**
     * 处理请求结果
     *
     * @param result 请求结果，a RequestAdapter.ResultList<T> Object
     */
    protected void processResult(RequestAdapter.ResultList<T> result) {
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

        RequestAdapter.DataList<T> data = result.getData();

        // 请求接口成功，但没有数据
        if (data == null) {
            post(BIND_STATUS.NO_DATA.name(), ErrorNo.ERROR_RESULT_EMPTY, ErrorMsg.ERROR_RESULT_EMPTY);
            return;
        }

        if (mProvider == null) {
            mProvider = new ListProvider<>();
        }

        mProvider.setData(data);
        mProvider.setOffsetToNextPage();

        if (hasMoreData()) {
            // 请求成功，还可以再次请求数据
            post(BIND_STATUS.LOAD_MORE.name());
        } else {
            // 请求接口成功，但不可再次请求数据
            post(BIND_STATUS.NO_DATA.name());
        }
    }

    /**
     * 获取详情数据
     *
     * @param position 位置
     * @return 详情数据 a T Object, or null
     */
    public T getItem(int position) {
        ListProvider<T> provider = getProvider();
        return (provider == null) ? null : provider.getItem(position);
    }

    /**
     * 获取当前记录数
     *
     * @return 当前记录数
     */
    public int getSize() {
        ListProvider<T> provider = getProvider();
        return (provider == null) ? 0 : provider.getSize();
    }

    /**
     * 获取总记录数
     *
     * @return 总记录数
     */
    public long getTotal() {
        ListProvider<T> provider = getProvider();
        return (provider == null) ? 0 : provider.getTotal();
    }

    /**
     * 获取Null数
     *
     * @return Null数
     */
    public long getNull() {
        ListProvider<T> provider = getProvider();
        return (provider == null) ? 0 : provider.getNull();
    }

    /**
     * 是否有Null数据
     *
     * @return Returns True, or False
     */
    public boolean hasNull() {
        ListProvider<T> provider = getProvider();
        return (provider != null) && provider.hasNull();
    }

    /**
     * 获取每次查询记录数
     * SELECT * FROM table LIMIT offset, [limit];
     *
     * @return 每次查询记录数
     */
    public int getLimit() {
        ListProvider<T> provider = getProvider();
        return (provider == null) ? ListProvider.DEFAULT_LIMIT : provider.getLimit();
    }

    /**
     * 获取每次查询起始位置
     * SELECT * FROM table LIMIT [offset], limit;
     *
     * @return 每次查询起始位置
     */
    public int getOffset() {
        ListProvider<T> provider = getProvider();
        return (provider == null) ? 0 : provider.getOffset();
    }

    /**
     * 获取当前页码
     *
     * @return 当前页码
     */
    public int getPageNo() {
        ListProvider<T> provider = getProvider();
        return (provider == null) ? 1 : provider.getPageNo();
    }

    /**
     * 获取是否是第一页
     *
     * @return Returns True, or False
     */
    public boolean firstPage() {
        ListProvider<T> provider = getProvider();
        return (provider == null) || provider.firstPage();
    }

    /**
     * 是否有数据
     *
     * @return Returns True, or False
     */
    public boolean hasData() {
        ListProvider<T> provider = getProvider();
        return (provider != null) && provider.hasData();
    }

    /**
     * 是否还可以再次请求更多数据
     *
     * @return Returns True, or False
     */
    public boolean hasMoreData() {
        ListProvider<T> provider = getProvider();
        return (provider != null) && provider.hasMoreData();
    }

    /**
     * 重置数据，清空当前数据，重新从第一页开始加载
     */
    public void clear() {
        ListProvider<T> provider = getProvider();
        if (provider != null) {
            provider.clear();
        }
    }

    /**
     * 获取列表数据提供者
     *
     * @return 列表数据提供者，a T Object, or null
     */
    public ListProvider<T> getProvider() {
        return mProvider;
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
         * 请求成功，还可以再次请求更多数据
         */
        LOAD_MORE,

        /**
         * 请求接口成功，但没有数据或不可再次请求数据
         */
        NO_DATA,

        /**
         * 请求失败，通过错误码和错误消息获取失败原因
         */
        LOAD_FAILURE
    }

}
