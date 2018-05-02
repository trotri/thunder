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

import android.os.Handler;
import android.os.Looper;

/**
 * UiThread class file
 * 主线程类
 * 在主线程中执行代码，用于从子线程切换到主线程
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: UiThread.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class UiThread {

    public static final String TAG = "UiThread";

    /**
     * 主线程Handler类
     */
    public static final Handler MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 将AbstractCommand子类添加到主线程队列
     *
     * @param command The Command that will be executed.
     * @return Returns true if the Runnable was successfully placed in to the
     * message queue.  Returns false on failure, usually because the
     * looper processing the message queue is exiting.
     */
    public static boolean exec(AbstractCommand command) {
        return MAIN_THREAD_HANDLER.post(command);
    }

    /**
     * 执行命令
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.UiThread$AbstractCommand { *; }
     * </p>
     */
    public abstract static class AbstractCommand implements Runnable {
        @Override
        public void run() {
            exec();
        }

        /**
         * 在主线程执行操作
         */
        public abstract void exec();

    }

}
