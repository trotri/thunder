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

package com.trotri.android.thunder.inj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ViewInject @interface file
 * 视图注解
 * 注解方式，从属性注释中获取视图Id，并从视图Id获取视图对象，最终将视图对象设置为属性值
 * 不能注解的属性类型：静态属性、Final类型、基本类型、数组类型
 * <p>
 * 避免混淆，proguard-rules.pro:
 * -keep @interface 包名.ViewInject { *; }
 * </p>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ViewInject.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    /**
     * 视图Id
     *
     * @return 视图Id
     */
    int value();
}
