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

import com.trotri.android.rice.bean.ErrorMsg;
import com.trotri.android.rice.bean.ErrorNo;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * ActionManager class file
 * RxJava方法管理类，用于Mvvm模式，方法绑定和通知
 * 需要包：
 * compile 'io.reactivex.rxjava2:rxjava:2.1.7'
 * compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ActionManager.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class ActionManager {

    public static final String TAG = "ActionManager";

    /**
     * RxJava方法集合
     */
    private final Map<String, Consumer<Result>> mActions = new HashMap<>();

    /**
     * 执行RxJava方法
     *
     * @param name   方法名
     * @param errNo  错误码
     * @param errMsg 错误消息
     * @throws ClassNotFoundException RxJava方法名不存在，忘记绑定或绑定错误
     * @throws RuntimeException       RxJava执行异常
     */
    public void exec(final String name, final int errNo, final String errMsg) throws ClassNotFoundException, RuntimeException {
        Consumer<Result> func = get(name);

        ObservableOnSubscribe<Result> subscriber = new ObservableOnSubscribe<Result>() {
            @Override
            public void subscribe(ObservableEmitter<Result> e) throws Exception {
                e.onNext(new Result(errNo, errMsg));
            }
        };

        Observable.create(subscriber).observeOn(AndroidSchedulers.mainThread()).subscribe(func, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable tr) throws Exception {
                throw new RuntimeException(TAG + " exec() Func: '" + name + "' Error", tr);
            }
        });
    }

    /**
     * 获取RxJava方法
     *
     * @param name 方法名
     * @return RxJava方法，a Consumer<Result> Object
     * @throws ClassNotFoundException RxJava方法不存在，忘记绑定或绑定错误
     */
    public Consumer<Result> get(String name) throws ClassNotFoundException {
        Consumer<Result> result = mActions.get(name);
        if (result == null) {
            throw new ClassNotFoundException(TAG + " get() Func: '" + name + "' Not Found");
        }

        return result;
    }

    /**
     * 添加RxJava方法
     *
     * @param name 方法名
     * @param func RxJava方法，a Consumer<Result> Object
     */
    public void add(String name, Consumer<Result> func) {
        mActions.put(name, func);
    }

    /**
     * Result class
     */
    public static class Result {
        /**
         * 错误码
         */
        private int mErrNo;

        /**
         * 错误消息
         */
        private String mErrMsg;

        /**
         * 构造方法：错误码 = 成功码、错误消息 = 成功消息
         */
        public Result() {
            this(ErrorNo.SUCCESS_NUM, ErrorMsg.SUCCESS_MSG);
        }

        /**
         * 构造方法：初始化错误码、错误消息和内容
         *
         * @param errNo  错误码
         * @param errMsg 错误消息
         */
        public Result(int errNo, String errMsg) {
            setErrNo(errNo);
            setErrMsg(errMsg);
        }

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

    }

}
