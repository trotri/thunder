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

import android.app.Activity;
import android.view.View;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * ViewInjectProcessor class file
 * ViewInject注解处理类
 * ****** TODO: Debug包时可用，Release包时不可用 ******
 * ****** TODO: 试验类，效率低，不可使用 ******
 * 注解方式，从属性注释中获取视图Id，并从视图Id获取视图对象，最终将视图对象设置为属性值
 * 不处理的属性类型：静态属性、Final类型、基本类型、数组类型
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ViewInjectProcessor.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class ViewInjectProcessor {

    private static final String TAG = "ViewInjectProcessor";

    /**
     * 处理Activity注解
     *
     * @param obj an Activity object
     */
    public static void process(Activity obj) {
        if (obj == null) {
            throw new IllegalArgumentException("obj is null");
        }

        process(obj, obj.getWindow().getDecorView());
    }

    /**
     * 处理注解
     *
     * @param obj       a Fragment object, or an Activity object, or an User Declared ViewHolder
     * @param container 父视图
     * @throws IllegalArgumentException the obj is null
     * @throws IllegalArgumentException the container is null
     */
    public static void process(Object obj, View container) throws IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("obj is null");
        }

        if (container == null) {
            throw new IllegalArgumentException("view is null");
        }

        Class clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length <= 0) {
            return;
        }

        String className = clazz.getName();
        for (Field f : fields) {
            String fieldName = f.getName();

            ViewInject annotation = f.getAnnotation(ViewInject.class);
            if (annotation == null) {
                continue;
            }

            int modifiers = f.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                continue;
            }

            Class type = f.getType();
            if (type.isPrimitive() || type.isArray()) {
                continue;
            }

            f.setAccessible(true);
            try {
                f.set(obj, container.findViewById(annotation.value()));
                Logger.d(Constants.TAG_LOG, TAG + " process() class: '" + className + "', field: '" + fieldName + "'");
            } catch (IllegalAccessException e) {
                Logger.e(Constants.TAG_LOG, TAG + " process() class: '" + className + "', field: '" + fieldName + "'", e);
            }
        }
    }

}
