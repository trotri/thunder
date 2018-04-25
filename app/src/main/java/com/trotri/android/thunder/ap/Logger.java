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

package com.trotri.android.thunder.ap;

import android.util.Log;

/**
 * Logger final class file
 * 日志处理类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Logger.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public final class Logger {
    /**
     * 默认是否关闭日志输出
     */
    private static boolean DEFAULT_CLOSE_PRINT = false;

    /**
     * 默认是否关闭日志报告
     */
    private static boolean DEFAULT_CLOSE_REPORT = false;

    /**
     * 日志级别
     */
    public static final int VERBOSE = Log.VERBOSE;
    public static final int DEBUG = Log.DEBUG;
    public static final int INFO = Log.INFO;
    public static final int WARN = Log.WARN;
    public static final int ERROR = Log.ERROR;

    /**
     * 报告日志接口
     */
    private static OnReportListener sReportListener = null;

    /**
     * 是否关闭日志输出，True：关闭日志输出、False：打开日志输出
     */
    private static boolean sClosePrint = DEFAULT_CLOSE_PRINT;

    /**
     * 是否关闭日志报告，True：关闭日志报告、False：打开日志报告
     */
    private static boolean sCloseReport = DEFAULT_CLOSE_REPORT;

    /**
     * 构造方法：禁止New实例
     */
    private Logger() {
    }

    /**
     * 打印Verbose日志
     *
     * @param tag 日志标识
     * @param msg 日志内容
     * @return The number of bytes written.
     */
    public static int v(String tag, String msg) {
        report(VERBOSE, tag, msg);

        if (isClosePrint()) {
            return 0;
        }

        return Log.v(tag, msg);
    }

    /**
     * 打印Debug日志
     *
     * @param tag 日志标识
     * @param msg 日志内容
     * @return The number of bytes written.
     */
    public static int d(String tag, String msg) {
        report(DEBUG, tag, msg);

        if (isClosePrint()) {
            return 0;
        }

        return Log.d(tag, msg);
    }

    /**
     * 打印Info日志
     *
     * @param tag 日志标识
     * @param msg 日志内容
     * @return The number of bytes written.
     */
    public static int i(String tag, String msg) {
        report(INFO, tag, msg);

        if (isClosePrint()) {
            return 0;
        }

        return Log.i(tag, msg);
    }

    /**
     * 打印Warn日志
     *
     * @param tag 日志标识
     * @param msg 日志内容
     * @return The number of bytes written.
     */
    public static int w(String tag, String msg) {
        report(WARN, tag, msg);

        if (isClosePrint()) {
            return 0;
        }

        return Log.w(tag, msg);
    }

    /**
     * 打印Warn日志
     *
     * @param tag 日志标识
     * @param tr  a Throwable to log
     * @return The number of bytes written.
     */
    public static int w(String tag, Throwable tr) {
        return w(tag, Log.getStackTraceString(tr));
    }

    /**
     * 打印Warn日志
     *
     * @param tag 日志标识
     * @param msg 日志内容
     * @param tr  a Throwable to log
     * @return The number of bytes written.
     */
    public static int w(String tag, String msg, Throwable tr) {
        return w(tag, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * 打印Error日志
     *
     * @param tag 日志标识
     * @param msg 日志内容
     * @return The number of bytes written.
     */
    public static int e(String tag, String msg) {
        report(ERROR, tag, msg);

        if (isClosePrint()) {
            return 0;
        }

        return Log.e(tag, msg);
    }

    /**
     * 打印Error日志
     *
     * @param tag 日志标识
     * @param tr  a Throwable to log
     * @return The number of bytes written.
     */
    public static int e(String tag, Throwable tr) {
        return e(tag, getStackTraceString(tr));
    }

    /**
     * 打印Error日志
     *
     * @param tag 日志标识
     * @param msg 日志内容
     * @param tr  a Throwable to log
     * @return The number of bytes written.
     */
    public static int e(String tag, String msg, Throwable tr) {
        return e(tag, msg + '\n' + getStackTraceString(tr));
    }

    /**
     * 通过Throwable获取日志内容
     *
     * @param tr a Throwable to log
     * @return 日志内容
     */
    public static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }

    /**
     * 报告日志
     *
     * @param priority 日志级别
     * @param tag      日志标识
     * @param msg      日志内容
     */
    public static void report(int priority, String tag, String msg) {
        if (isCloseReport()) {
            return;
        }

        if (sReportListener == null) {
            return;
        }

        sReportListener.onReport(priority, tag, msg);
    }

    /**
     * 设置报告日志接口
     *
     * @param listener a OnReportListener to report log
     */
    public static void setOnReportListener(OnReportListener listener) {
        sReportListener = listener;
    }

    /**
     * 是否关闭日志输出
     *
     * @return Returns True, or False
     */
    public static boolean isClosePrint() {
        return sClosePrint;
    }

    /**
     * 设置是否关闭日志输出
     *
     * @param value 是否关闭日志输出，True：关闭日志输出、False：打开日志输出，默认：{@link #DEFAULT_CLOSE_PRINT}
     */
    public static void setClosePrint(boolean value) {
        sClosePrint = value;
    }

    /**
     * 是否关闭日志报告
     *
     * @return Returns True, or False
     */
    public static boolean isCloseReport() {
        return sCloseReport;
    }

    /**
     * 设置是否关闭日志报告
     *
     * @param value 是否关闭日志报告，True：关闭日志报告、False：打开日志报告，默认：{@link #DEFAULT_CLOSE_REPORT}
     */
    public static void setCloseReport(boolean value) {
        sCloseReport = value;
    }

    /**
     * OnReportListener interface
     * 报告日志接口
     *
     * @since 1.0
     */
    public interface OnReportListener {
        /**
         * 报告日志的回调方法
         *
         * @param priority 日志级别
         * @param tag      日志标识
         * @param msg      日志内容
         */
        void onReport(int priority, String tag, String msg);
    }

}
