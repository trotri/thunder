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

/**
 * CrashHandler class file
 * 崩溃处理类
 * <p>
 * 避免混淆，proguard-rules.pro:
 * -keepclassmembers class 包名.CrashHandler { *; }
 * </p>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: CrashHandler.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";

    /**
     * 默认的线程Id
     */
    public static final long DEFAULT_THREAD_ID = -1;

    /**
     * 默认的线程名
     */
    public static final String DEFAULT_THREAD_NAME = "unknown";

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        long threadId = (t != null) ? t.getId() : DEFAULT_THREAD_ID;
        String threadName = (t != null) ? t.getName() : DEFAULT_THREAD_NAME;
        exec(threadId, threadName, e);
    }

    /**
     * 处理崩溃
     *
     * @param threadId   当前线程Id
     * @param threadName 当前线程名
     * @param e          a Throwable Object
     */
    public void exec(long threadId, String threadName, Throwable e) {
        Logger.e(Constants.TAG_LOG, TAG + " exec() threadId: " + threadId + ", threadName: '" + threadName + "'", e);
    }

}
