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

import java.util.List;

/**
 * RequestAdapter final class file
 * 请求结果适配器
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: RequestAdapter.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public final class RequestAdapter {
    /**
     * ResultList class
     */
    public static class ResultList<T> extends Result<DataList<T>> {
    }

    /**
     * Result class
     */
    public static class Result<T> {
        /**
         * 错误码
         */
        private int mErrNo = ErrorNo.SUCCESS_NUM;

        /**
         * 错误消息
         */
        private String mErrMsg = ErrorMsg.SUCCESS_MSG;

        /**
         * 数据
         */
        private T mData;

        /**
         * 获取错误码
         *
         * @return 错误码
         */
        public int getErrNo() {
            return mErrNo;
        }

        /**
         * 设置错误码
         *
         * @param errNo 错误码
         */
        public void setErrNo(int errNo) {
            mErrNo = errNo;
        }

        /**
         * 获取错误消息
         *
         * @return 错误消息
         */
        public String getErrMsg() {
            return mErrMsg;
        }

        /**
         * 设置错误消息
         *
         * @param errMsg 错误消息
         */
        public void setErrMsg(String errMsg) {
            mErrMsg = errMsg;
        }

        /**
         * 获取数据
         */
        public T getData() {
            return mData;
        }

        /**
         * 设置数据
         *
         * @param data 返回数据
         */
        public void setData(T data) {
            mData = data;
        }

        @Override
        public String toString() {
            return (new StringBuilder()).append("[ ")
                    .append("errNo: ").append(getErrNo())
                    .append(", errMsg: ").append(getErrMsg())
                    .append(", data: ").append(getData())
                    .append(" ]").toString();
        }

    }

    /**
     * DataList class
     */
    public static class DataList<T> {
        /**
         * 总记录数
         */
        private long mTotal;

        /**
         * 查询记录数 SELECT * FROM table LIMIT offset, [limit];
         */
        private int mLimit;

        /**
         * 查询起始位置 SELECT * FROM table LIMIT [offset], limit;
         */
        private int mOffset;

        /**
         * 列表
         */
        private List<T> mRows;

        /**
         * 获取总记录数
         *
         * @return 总记录数
         */
        public long getTotal() {
            return mTotal;
        }

        /**
         * 设置总记录数
         *
         * @param total 总记录数
         */
        public void setTotal(long total) {
            mTotal = total;
        }

        /**
         * 获取查询记录数
         *
         * @return 查询记录数
         */
        public int getLimit() {
            return mLimit;
        }

        /**
         * 设置查询记录数
         *
         * @param limit 查询记录数
         */
        public void setLimit(int limit) {
            mLimit = limit;
        }

        /**
         * 获取查询起始位置
         *
         * @return 查询起始位置
         */
        public int getOffset() {
            return mOffset;
        }

        /**
         * 设置查询起始位置
         *
         * @param offset 查询起始位置
         */
        public void setOffset(int offset) {
            mOffset = offset;
        }

        /**
         * 获取列表
         *
         * @return 列表
         */
        public List<T> getRows() {
            return mRows;
        }

        /**
         * 设置列表
         *
         * @param rows 列表
         */
        public void setRows(List<T> rows) {
            mRows = rows;
        }

        @Override
        public String toString() {
            return (new StringBuilder()).append("[ ")
                    .append("total: ").append(getTotal())
                    .append(", limit: ").append(getLimit())
                    .append(", offset: ").append(getOffset())
                    .append(", size: ").append((getRows() == null) ? 0 : getRows().size())
                    .append(" ]").toString();
        }

    }

}
