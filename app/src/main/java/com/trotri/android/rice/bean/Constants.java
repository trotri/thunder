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
 * Constants final class file
 * 常量类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Constants.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public final class Constants {
    /**
     * 是否是测试版本，线上：false
     */
    public static final boolean DEBUG = true;

    /**
     * Rice.Jar版本，Jar后缀
     */
    public static final String VERSION = "1.0.1";

    /**
     * Tag
     */
    public static final String TAG = "Rice";

    /**
     * Tag：小写
     */
    public static final String TAG_LOWER = TAG.toLowerCase();

    /**
     * Tag：日志
     */
    public static final String TAG_LOG = TAG + "-" + VERSION;

}
