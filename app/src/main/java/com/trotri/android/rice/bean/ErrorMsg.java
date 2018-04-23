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

package com.trotri.android.rice.bean;

import android.text.TextUtils;
import android.util.SparseArray;

/**
 * ErrorMsg final class file
 * 常用错误信息类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ErrorMsg.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public final class ErrorMsg {
    /**
     * OK
     */
    public static final String SUCCESS_MSG = "OK";

    /**
     * 请求错误
     */
    public static final String ERROR_REQUEST = "Bad Request";

    /**
     * 用户没有访问权限
     */
    public static final String ERROR_FORBIDDEN = "Forbidden";

    /**
     * 页面不存在
     */
    public static final String ERROR_NOT_FOUND = "Not Found";

    /**
     * 系统运行异常
     */
    public static final String ERROR_SYSTEM_RUN_ERR = "Internal Server Error";

    /**
     * 脚本运行失败
     */
    public static final String ERROR_SCRIPT_RUN_ERR = "Not Implemented";

    /**
     * 参数错误
     */
    public static final String ERROR_ARGS_ERR = "Args Error";

    /**
     * 结果为空
     */
    public static final String ERROR_RESULT_EMPTY = "Result Empty";

    /**
     * 结果错误
     */
    public static final String ERROR_RESULT_ERR = "Result Error";

    /**
     * Json解析异常
     */
    public static final String ERROR_RESULT_JSON_SYNTAX_ERR = "Json Syntax Exception";

    /**
     * 未知错误
     */
    public static final String ERROR_UNKNOWN = "Unknown Error";

    /**
     * 寄存常用错误码和错误信息
     */
    private static SparseArray<String> mRegistry;

    /**
     * 通过错误码获取错误信息
     *
     * @param errNo 错误码
     * @return 错误信息
     */
    public static String getErrMsg(int errNo) {
        if (mRegistry == null) {
            mRegistry = new SparseArray<>();

            mRegistry.put(ErrorNo.SUCCESS_NUM, ErrorMsg.SUCCESS_MSG);
            mRegistry.put(ErrorNo.ERROR_REQUEST, ErrorMsg.ERROR_REQUEST);
            mRegistry.put(ErrorNo.ERROR_FORBIDDEN, ErrorMsg.ERROR_FORBIDDEN);
            mRegistry.put(ErrorNo.ERROR_NOT_FOUND, ErrorMsg.ERROR_NOT_FOUND);
            mRegistry.put(ErrorNo.ERROR_SYSTEM_RUN_ERR, ErrorMsg.ERROR_SYSTEM_RUN_ERR);
            mRegistry.put(ErrorNo.ERROR_SCRIPT_RUN_ERR, ErrorMsg.ERROR_SCRIPT_RUN_ERR);
            mRegistry.put(ErrorNo.ERROR_ARGS_ERR, ErrorMsg.ERROR_ARGS_ERR);
            mRegistry.put(ErrorNo.ERROR_RESULT_EMPTY, ErrorMsg.ERROR_RESULT_EMPTY);
            mRegistry.put(ErrorNo.ERROR_RESULT_ERR, ErrorMsg.ERROR_RESULT_ERR);
            mRegistry.put(ErrorNo.ERROR_RESULT_JSON_SYNTAX_ERR, ErrorMsg.ERROR_RESULT_JSON_SYNTAX_ERR);
            mRegistry.put(ErrorNo.ERROR_UNKNOWN, ErrorMsg.ERROR_UNKNOWN);
        }

        String errMsg = mRegistry.get(errNo);
        if (TextUtils.isEmpty(errMsg)) {
            return ERROR_UNKNOWN;
        }

        return errMsg;
    }

}
