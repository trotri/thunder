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

package com.trotri.android.rice.util;

import com.trotri.android.thunder.ap.CrashHandler;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * TimeTicker class file
 * 计划任务类，周期执行和延迟执行
 * 需要包：
 * compile 'org.apache.commons:commons-lang3:3.6'
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: TimeTicker.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class TimeTicker {

    public static final String TAG = "TimeTicker";

    /**
     * 线程名
     */
    private static final String THREAD_NAME = TAG + " #%d";

    /**
     * 处理器数量，>= 1
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池核心线程数
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    /**
     * 线程工厂
     */
    private static final ThreadFactory THREAD_FACTORY = new BasicThreadFactory.Builder()
            .namingPattern(THREAD_NAME).daemon(true).build();

    /**
     * 崩溃处理类
     */
    private static CrashHandler sCrashHandler = new CrashHandler();

    /**
     * 周期执行
     *
     * @param command      the runnable task
     * @param initialDelay 首次执行延迟时间
     * @param period       在上个任务开始执行之后延迟多久执行下个任务，延迟时间从上一个任务开始时计算
     * @param unit         时间单位
     * @param l            执行完成后回调方法
     * @param <V>          Runnable::exec()返回结果类型
     * @return 句柄，a ScheduledFuture<?> Object，用于取消任务
     * @throws RejectedExecutionException if the task cannot be scheduled for execution
     * @throws NullPointerException       if command is null
     * @throws IllegalArgumentException   if period less than or equal to zero
     */
    public static <V> ScheduledFuture<?> atFixedRate(AbstractRunnable<V> command, long initialDelay, long period, TimeUnit unit, Listener<V> l) {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        command.mExecutorService = newScheduledThreadPool();
        command.mListener = l;

        return command.mExecutorService.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    /**
     * 周期执行
     *
     * @param command      the runnable task
     * @param initialDelay 首次执行延迟时间
     * @param period       在上个任务执行完成之后延迟多久执行下个任务，延迟时间从上一个任务结束时计算
     * @param unit         时间单位
     * @param l            执行完成后回调方法
     * @param <V>          Runnable::exec()返回结果类型
     * @return 句柄，a ScheduledFuture<?> Object，用于取消任务
     * @throws RejectedExecutionException if the task cannot be scheduled for execution
     * @throws NullPointerException       if command is null
     * @throws IllegalArgumentException   if period less than or equal to zero
     */
    public static <V> ScheduledFuture<?> withFixedDelay(AbstractRunnable<V> command, long initialDelay, long period, TimeUnit unit, Listener<V> l) {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        command.mExecutorService = newScheduledThreadPool();
        command.mListener = l;

        return command.mExecutorService.scheduleWithFixedDelay(command, initialDelay, period, unit);
    }

    /**
     * 延迟执行
     *
     * @param command the runnable task
     * @param delay   延迟时间
     * @param unit    时间单位
     * @param l       执行完成后回调方法
     * @param <V>     Runnable::exec()返回结果类型
     * @return 句柄，a ScheduledFuture<?> Object，用于取消任务
     * @throws RejectedExecutionException if the task cannot be scheduled for execution
     * @throws NullPointerException       if command is null
     */
    public static <V> ScheduledFuture<?> delay(AbstractRunnable<V> command, long delay, TimeUnit unit, Listener<V> l) {
        if (command == null) {
            throw new NullPointerException("command is null");
        }

        command.mExecutorService = newScheduledThreadPool();
        command.mListener = l;

        return command.mExecutorService.schedule(command, delay, unit);
    }

    /**
     * 获取计划任务执行类
     *
     * @return 计划任务执行类，a ScheduledExecutorService Object
     */
    public static ScheduledExecutorService newScheduledThreadPool() {
        return new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, THREAD_FACTORY);
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
     * -keepclassmembers class 包名.TimeTicker$AbstractRunnable { *; }
     * </p>
     */
    public abstract static class AbstractRunnable<V> implements Runnable {
        /**
         * 计划任务执行类
         */
        private ScheduledExecutorService mExecutorService;

        /**
         * 执行完成后回调接口
         */
        private Listener<V> mListener;

        @Override
        public void run() {
            onInitialize();

            V result = exec();

            if (mListener != null) {
                mListener.onComplete(mExecutorService, result);
            }
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
         *
         * @return 执行结果
         */
        public abstract V exec();

    }

    /**
     * Listener interface
     * 回调接口
     *
     * @since 1.0
     */
    public interface Listener<V> {
        /**
         * 执行完成后回调方法
         *
         * @param service 执行计划任务类，用于shutdown()
         * @param result  执行结果
         */
        void onComplete(ScheduledExecutorService service, V result);
    }

}
