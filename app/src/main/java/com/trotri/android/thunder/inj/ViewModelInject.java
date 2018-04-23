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

import com.trotri.android.thunder.base.AbstractViewModel;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ViewModelInject @interface file
 * ViewModel注解
 * 注解方式，从属性注释中获取ViewModel的Class，并从Class获取ViewModel对象，最终将ViewModel对象设置为属性值
 * <p>
 * 避免混淆，proguard-rules.pro:
 * -keep @interface 包名.ViewModelInject { *; }
 * </p>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ViewModelInject.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewModelInject {
    /**
     * The component class that is to be used for the view model.
     *
     * @return ViewModel.Class
     */
    Class<? extends AbstractViewModel> value();
}
