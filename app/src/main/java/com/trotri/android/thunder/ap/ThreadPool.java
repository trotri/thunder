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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPool class file
 * 标准线程池类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ThreadPool.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class ThreadPool {

    public static final String TAG = "ThreadPool";

    /**
     * 处理器数量，>= 1
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池核心线程数
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    /**
     * 线程池最大线程数
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    /**
     * 多余的空闲线程生存时间
     */
    private static final int KEEP_ALIVE = 1;

    /**
     * 阻塞队列容量
     */
    private static final int POOL_WORK_QUEUE_CAPACITY = 128;

    /**
     * 阻塞队列
     */
    private static final BlockingQueue<Runnable> POOL_WORK_QUEUE = new LinkedBlockingQueue<>(POOL_WORK_QUEUE_CAPACITY);

    /**
     * 线程工厂
     */
    private static final ThreadFactory THREAD_FACTORY = new DefaultThreadFactory();

    /**
     * 崩溃处理类
     */
    private static CrashHandler sCrashHandler = new CrashHandler();

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static final Executor THREAD_POOL_EXECUTOR
            = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE,
            TimeUnit.SECONDS, POOL_WORK_QUEUE, THREAD_FACTORY);

    /**
     * Executes the given command at some time in the future.  The command
     * may execute in a new thread, in a pooled thread, or in the calling
     * thread, at the discretion of the {@code Executor} implementation.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     *                                    accepted for execution
     * @throws NullPointerException       if command is null
     */
    public static void execute(AbstractCommand command) {
        THREAD_POOL_EXECUTOR.execute(command);
    }

    /**
     * 获取崩溃处理类
     *
     * @return a CrashHandler Object, or null
     */
    public static CrashHandler getCrashHandler() {
        return sCrashHandler;
    }

    /**
     * 设置崩溃处理类
     *
     * @param crashHandler a CrashHandler Object
     */
    public static void setCrashHandler(CrashHandler crashHandler) {
        sCrashHandler = crashHandler;
    }

    /**
     * 执行命令基类
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.ThreadPool$AbstractCommand { *; }
     * </p>
     */
    public abstract static class AbstractCommand implements Runnable {
        @Override
        public void run() {
            onInitialize();

            exec();
        }

        /**
         * 初始化崩溃处理类
         */
        public void onInitialize() {
            CrashHandler h = getCrashHandler();
            if (h != null) {
                Thread.currentThread().setUncaughtExceptionHandler(h);
            }
        }

        /**
         * 在后台线程执行操作
         */
        public abstract void exec();

    }

    /**
     * 线程工厂
     * <p>
     * 避免混淆，proguard-rules.pro:
     * -keepclassmembers class 包名.ThreadPool$DefaultThreadFactory { *; }
     * </p>
     */
    static class DefaultThreadFactory implements ThreadFactory {

        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, TAG + " #" + mCount.getAndIncrement());
        }

    }

}
