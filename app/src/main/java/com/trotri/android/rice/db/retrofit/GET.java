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

package com.trotri.android.rice.db.retrofit;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * GET @interface file
 * GET注解
 * 注解方式，从属性注释中获取Db寄存的键名前缀，键名：键名前缀 + 参数列表
 * <p>
 * 避免混淆，proguard-rules.pro:
 * -keep @interface 包名.GET { *; }
 * </p>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: GET.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface GET {
    /**
     * 键名前缀，键名：键名前缀 + 参数列表
     *
     * @return 键名前缀
     */
    String value() default "";
}
