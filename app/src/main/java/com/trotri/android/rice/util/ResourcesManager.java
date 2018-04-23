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

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;

import com.trotri.android.rice.bean.Constants;
import com.trotri.android.thunder.ap.Logger;

/**
 * ResourcesManager class file
 * 资源管理类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: ResourcesManager.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class ResourcesManager {
    /**
     * 资源类型
     */
    public static final String TYPE_ID = "id";
    public static final String TYPE_LAYOUT = "layout";
    public static final String TYPE_STRING = "string";
    public static final String TYPE_DRAWABLE = "drawable";
    public static final String TYPE_COLOR = "color";
    public static final String TYPE_DIMEN = "dimen";
    public static final String TYPE_ANIM = "anim";
    public static final String TYPE_STYLE = "style";
    public static final String TYPE_ARRAY = "array";
    public static final String TYPE_RAW = "raw";

    private static final String TAG = "ResourcesManager";

    /**
     * 用于对获取单例的线程加锁
     */
    private static final Object sInstanceLock = new Object();

    private static ResourcesManager sInstance;

    /**
     * 上下文环境
     */
    private final Context mAppContext;

    /**
     * 资源类
     */
    private final Resources mResources;

    /**
     * 项目包名
     */
    private final String mAppPackage;

    /**
     * 构造方法：初始化上下文环境、资源类、项目包名
     */
    private ResourcesManager(Context c) {
        mAppContext = c.getApplicationContext();
        mResources = mAppContext.getResources();
        mAppPackage = mAppContext.getPackageName();
    }

    /**
     * 获取已存在的实例，该实例是共享的，如果实例不存在，则创建新实例
     */
    public static ResourcesManager getInstance(Context c) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new ResourcesManager(c);
            }

            return sInstance;
        }
    }

    /**
     * 通过名称获取String资源值
     *
     * @param name 资源名称
     * @return Returns the resource value
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public String getString(String name) throws NullPointerException, Resources.NotFoundException {
        return mResources.getString(getStringId(name));
    }

    /**
     * 通过名称获取String资源值列表
     *
     * @param name 资源名称
     * @return Returns the resource values
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public String[] getStringArray(String name) throws NullPointerException, Resources.NotFoundException {
        return mResources.getStringArray(getStringId(name));
    }

    /**
     * 通过名称获取Drawable资源值
     *
     * @param name 资源名称
     * @return Returns the resource value
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public Drawable getDrawable(String name, @Nullable Resources.Theme theme) throws NullPointerException, Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mResources.getDrawable(getDrawableId(name), theme);
        }

        return mResources.getDrawable(getDrawableId(name));
    }

    /**
     * 通过名称获取Color资源值
     *
     * @param name 资源名称
     * @return Returns the resource value
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    @ColorInt
    public int getColor(String name, @Nullable Resources.Theme theme) throws NullPointerException, Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mResources.getColor(getColorId(name), theme);
        }

        return mResources.getColor(getColorId(name));
    }

    /**
     * 通过名称获取Color资源值列表
     *
     * @param name 资源名称
     * @return Returns the resource values
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public ColorStateList getColorStateList(String name, @Nullable Resources.Theme theme) throws NullPointerException, Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mResources.getColorStateList(getColorId(name), theme);
        }

        return mResources.getColorStateList(getColorId(name));
    }

    /**
     * 通过名称获取Dimension资源值，单位：像素
     *
     * @param name 资源名称
     * @return Returns the resource value
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public float getDimension(String name) throws NullPointerException, Resources.NotFoundException {
        return mResources.getDimension(getDimenId(name));
    }

    /**
     * 通过名称获取View资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_ID);
    }

    /**
     * 通过名称获取Layout资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getLayoutId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_LAYOUT);
    }

    /**
     * 通过名称获取String资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getStringId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_STRING);
    }

    /**
     * 通过名称获取Drawable资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getDrawableId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_DRAWABLE);
    }

    /**
     * 通过名称获取Color资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getColorId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_COLOR);
    }

    /**
     * 通过名称获取Dimen资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getDimenId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_DIMEN);
    }

    /**
     * 通过名称获取Anim资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getAnimId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_ANIM);
    }

    /**
     * 通过名称获取Style资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getStyleId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_STYLE);
    }

    /**
     * 通过名称获取Array资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getArrayId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_ARRAY);
    }

    /**
     * 通过名称获取Raw资源Id
     *
     * @param name 资源名称
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getRawId(String name) throws NullPointerException, Resources.NotFoundException {
        return getIdentifier(name, TYPE_RAW);
    }

    /**
     * 通过名称和类型获取资源Id
     *
     * @param name 资源名称
     * @param type 资源类型，包括：id、layout、string、drawable、color、dimen、anim、style、array、raw
     * @return Returns the resource identifier
     * @throws NullPointerException        the resource name is null
     * @throws Resources.NotFoundException the resource was not found
     */
    public int getIdentifier(String name, String type) throws NullPointerException, Resources.NotFoundException {
        if (name == null) {
            Logger.e(Constants.TAG_LOG, TAG + " getIdentifier() failure, type: '" + type + "', name is null");
            throw new NullPointerException("type: '" + type + "', name is null");
        }

        name = name.trim();
        if (name.isEmpty()) {
            Logger.e(Constants.TAG_LOG, TAG + " getIdentifier() failure, type: '" + type + "', name is empty");
            throw new Resources.NotFoundException("type: '" + type + "', name is empty");
        }

        int identifier = 0;
        try {
            identifier = mResources.getIdentifier(name, type, mAppPackage);
        } catch (Exception e) {
            // Ignore
        }

        if (identifier <= 0) {
            Logger.e(Constants.TAG_LOG, TAG + " getIdentifier() Failure, type: '" + type + "', name: '" + name + "', resource not found");
            throw new Resources.NotFoundException("type: '" + type + "', name: '" + name + "', resource not found");
        }

        return identifier;
    }

    /**
     * 获取上下文环境
     *
     * @return an Application Context Object
     */
    public Context getAppContext() {
        return mAppContext;
    }

    /**
     * 获取资源类
     *
     * @return a Resources Object
     */
    public Resources getResources() {
        return mResources;
    }

    /**
     * 获取项目包名
     *
     * @return 项目包名
     */
    public String getAppPackage() {
        return mAppPackage;
    }

}
