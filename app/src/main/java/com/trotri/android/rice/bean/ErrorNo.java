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

/**
 * ErrorNo final class file
 * 常用错误码类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ErrorNo.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public final class ErrorNo {
    /**
     * OK
     */
    public static final int SUCCESS_NUM = 0;

    /**
     * 请求错误
     */
    public static final int ERROR_REQUEST = 400;

    /**
     * 用户没有访问权限
     */
    public static final int ERROR_FORBIDDEN = 403;

    /**
     * 页面不存在
     */
    public static final int ERROR_NOT_FOUND = 404;

    /**
     * 系统运行异常
     */
    public static final int ERROR_SYSTEM_RUN_ERR = 500;

    /**
     * 脚本运行失败
     */
    public static final int ERROR_SCRIPT_RUN_ERR = 501;

    /**
     * 参数错误
     */
    public static final int ERROR_ARGS_ERR = 1001;

    /**
     * 结果为空
     */
    public static final int ERROR_RESULT_EMPTY = 1002;

    /**
     * 结果错误
     */
    public static final int ERROR_RESULT_ERR = 1003;

    /**
     * Json解析异常
     */
    public static final int ERROR_RESULT_JSON_SYNTAX_ERR = 1004;

    /**
     * 未知错误
     */
    public static final int ERROR_UNKNOWN = 2008;

}
