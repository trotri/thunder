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

package com.trotri.android.thunder.util;

import android.content.Context;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * DexHelper class file
 * DexClassLoader辅助类
 * Optimized Directory Name，thunder_dex_optimized_毫秒时间戳+4位随机数、模式：PRIVATE
 * <pre>
 * String optimizedName = "test";
 * String fileName = "/data/data/<package name>/files/thunder_extends.jar";
 * String packageName = "com.trotri.android.thunder_extends.Bootstrap";
 *
 * Class clazz = DexHelper.getInstance(context).loadClass(optimizedName, fileName, packageName);
 * try {
 *     Object obj = clazz.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
 *     clazz.getMethod("initContext", new Class[]{Context.class}).invoke(obj, new Object[]{context});
 *     clazz.getMethod("run", new Class[0]).invoke(obj, new Object[0]);
 * } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
 *     e.printStackTrace();
 * }
 * </pre>
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: DexHelper.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class DexHelper {

    public static final String TAG = "DexHelper";

    /**
     * Optimized Directory Name的前缀，内部存储空间，不需要包含包名
     */
    public static final String OPTIMIZED_DIRECTORY_PREFIX_NAME = Constants.TAG_LOWER + "_dex_optimized";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object INSTANCE_LOCK = new Object();

    private static DexHelper sInstance;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 构造方法：初始化上下文环境
     */
    private DexHelper(Context c) {
        mAppContext = c.getApplicationContext();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static DexHelper getInstance(Context c) {
        synchronized (INSTANCE_LOCK) {
            if (sInstance == null) {
                sInstance = new DexHelper(c);
            }

            return sInstance;
        }
    }

    /**
     * 通过反射获取对象
     *
     * @param optimizedName Optimized Directory Postfix Name
     * @param fileName      Jar|Dex文件名，包括目录路径
     * @param packageName   类全路径，包名 + 类名
     * @return a Class Object, or null
     */
    public Class loadClass(String optimizedName, String fileName, String packageName) {
        DexClassLoader loader = getClassLoader(optimizedName, fileName);
        if (loader == null) {
            Logger.e(Constants.TAG_LOG, TAG + " loadClass() Dex Class Loader is null");
            return null;
        }

        try {
            return loader.loadClass(packageName);
        } catch (ClassNotFoundException e) {
            Logger.e(Constants.TAG_LOG, TAG + " loadClass()", e);
        }

        return null;
    }

    /**
     * 获取类加载器
     *
     * @param optimizedName Optimized Directory Postfix Name
     * @param fileName      Jar|Dex文件名，包括目录路径
     * @return a DexClassLoader Object, or null
     */
    public DexClassLoader getClassLoader(String optimizedName, String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            Logger.e(Constants.TAG_LOG, TAG + " getClassLoader() Jar|Dex File Not Exists, fileName: '" + fileName + "'");
            return null;
        }

        File optimizedDir = mkOptimizedDir(optimizedName);
        if (optimizedDir == null) {
            return null;
        }

        return new DexClassLoader(file.getAbsolutePath(), optimizedDir.getAbsolutePath(), null, getAppContext().getClassLoader());
    }

    /**
     * 创建缓存目录
     *
     * @param optimizedName Optimized Directory Postfix Name
     * @return a File Object For Optimized Directory, or null
     */
    public File mkOptimizedDir(String optimizedName) {
        File file = new File(getAppContext().getCacheDir(), getOptimizedDirectoryName(optimizedName));
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Logger.e(Constants.TAG_LOG, TAG + " mkOptimizedDir() Create Dex Optimized Directory Failure, fileName: '" + file.getAbsolutePath() + "'");
                return null;
            }
        }

        return file;
    }

    /**
     * 获取Optimized Directory Name
     *
     * @param optimizedName Optimized Directory Postfix Name
     * @return Optimized Directory Name, {@link #OPTIMIZED_DIRECTORY_PREFIX_NAME} + "_" + optimizedName
     */
    public String getOptimizedDirectoryName(String optimizedName) {
        if (optimizedName == null) {
            optimizedName = "";
        }

        return OPTIMIZED_DIRECTORY_PREFIX_NAME + "_" + optimizedName;
    }

    /**
     * 获取上下文环境
     *
     * @return an Application Context Object
     */
    public Context getAppContext() {
        return mAppContext;
    }

}
