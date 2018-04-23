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

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.base.AbstractViewModel;

import java.lang.reflect.Field;

/**
 * ViewModelInjectProcessor class file
 * ViewModelInject注解处理类
 * ****** TODO: Debug包时可用，Release包时不可用 ******
 * ****** TODO: 试验类，效率低，不可使用 ******
 * 注解方式，从属性注释中获取ViewModel的Class，并从Class获取ViewModel对象，最终将ViewModel对象设置为属性值
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ViewModelInjectProcessor.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class ViewModelInjectProcessor {

    public static final String TAG = "ViewModelInjectProcessor";

    /**
     * Activity或Fragment中ViewModel的属性名
     */
    public static final String FIELD_NAME = "mViewModel";

    /**
     * 处理注解
     *
     * @param obj a Fragment Object, or an Activity Object
     * @param <T> an AbstractViewModel type
     * @throws IllegalArgumentException the obj is null
     * @throws IllegalArgumentException the ViewModelInject is null
     * @throws NoSuchFieldException     field name is wrong
     */
    public static <T extends AbstractViewModel> void process(Object obj) throws IllegalArgumentException, NoSuchFieldException {
        if (obj == null) {
            throw new IllegalArgumentException("obj is null");
        }

        Class<?> clazz = obj.getClass();
        String className = clazz.getName();

        Field field;
        try {
            field = clazz.getField(FIELD_NAME);
        } catch (NoSuchFieldException e) {
            Logger.e(Constants.TAG_LOG, TAG + " process() class: '" + className + "', field: '" + FIELD_NAME + "'", e);
            throw e;
        }

        ViewModelInject annotation = clazz.getAnnotation(ViewModelInject.class);
        if (annotation == null) {
            throw new IllegalArgumentException("ViewModelInject is null");
        }

        Class<T> cls = (Class<T>) annotation.value();
        try {
            T viewModel = cls.newInstance();
            field.setAccessible(true);
            try {
                field.set(obj, viewModel);
                Logger.d(Constants.TAG_LOG, TAG + " process() class: '" + className + "', field: '" + FIELD_NAME + "'");
            } catch (IllegalAccessException e) {
                Logger.e(Constants.TAG_LOG, TAG + " process() class: '" + className + "', field: '" + FIELD_NAME + "'", e);
            }
        } catch (InstantiationException e) {
            Logger.e(Constants.TAG_LOG, TAG + " process() class: '" + className + "'", e);
        } catch (IllegalAccessException e) {
            Logger.e(Constants.TAG_LOG, TAG + " process() class: '" + className + "'", e);
        }
    }

}
