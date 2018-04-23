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

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.trotri.android.rice.bean.Constants;
import com.trotri.android.thunder.ap.Logger;

import java.util.Map;

/**
 * FragmentHelper class file
 * Fragment辅助类
 * 需要包：
 * Android Support Library V4
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: FragmentHelper.java 1 2016-01-08 10:00:06Z huan.song $
 * @since 1.0
 */
public class FragmentHelper {

    public static final String TAG = "FragmentHelper";

    /**
     * Fragment管理类
     */
    private FragmentManager mFragmentManager;

    /**
     * Activity名
     */
    private String mActivityName;

    /**
     * 构造方法：初始化Fragment管理类、Activity名
     *
     * @param fragmentManager Fragment管理类
     * @param activityName    Activity名
     */
    public FragmentHelper(FragmentManager fragmentManager, String activityName) {
        mFragmentManager = fragmentManager;
        mActivityName = activityName;
    }

    /**
     * 添加并提交多个Fragment
     *
     * @param fragments a Map Key => Fragment Container View Id, Value => a Fragment
     * @return Returns True, or False 退出Activity后或旋转屏幕后再提交Transaction，会执行失败
     * @throws NullPointerException  the parameter fragments is null
     * @throws IllegalStateException 将已使用Fragment添加到其他的Container View Id
     */
    public boolean batchAdd(Map<Integer, Fragment> fragments) throws NullPointerException, IllegalStateException {
        if (fragments == null) {
            Logger.e(Constants.TAG_LOG, TAG + " batchAdd() activity name: " + getActivityName() + ", fragments is null");
            throw new NullPointerException("Fragments is null");
        }

        if (fragments.isEmpty()) {
            Logger.e(Constants.TAG_LOG, TAG + " batchAdd() activity name: " + getActivityName() + ", fragments is empty");
            return false;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            int containerViewId = entry.getKey();
            Fragment fragment = entry.getValue();
            Logger.d(Constants.TAG_LOG, TAG + " batchAdd() activity name: " + getActivityName() + ", container view id: " + containerViewId);

            transaction.add(containerViewId, fragment);
        }

        try {
            int identifier = transaction.addToBackStack(null).commit();
            Logger.d(Constants.TAG_LOG, TAG + " batchAdd() activity name: " + getActivityName() + ", transaction id: " + identifier);
        } catch (IllegalStateException e) {
            Logger.e(Constants.TAG_LOG, TAG + " batchAdd() activity name: " + getActivityName(), e);
            return false;
        }

        return true;
    }

    /**
     * 添加并提交一个Fragment
     *
     * @param containerViewId Fragment Container View Id
     * @param fragment        a Fragment Object
     * @return Returns True, or False 退出Activity后或旋转屏幕后再提交Transaction，会执行失败
     * @throws IllegalStateException 将已使用Fragment添加到其他的Container View Id
     */
    public boolean add(@IdRes int containerViewId, Fragment fragment) throws IllegalStateException {
        FragmentTransaction transaction = getFragmentManager().beginTransaction().add(containerViewId, fragment).addToBackStack(null);

        try {
            int identifier = transaction.commit();
            Logger.d(Constants.TAG_LOG, TAG + " add() activity name: " + getActivityName() + ", container view id: " + containerViewId + ", transaction id: " + identifier);
        } catch (IllegalStateException e) {
            Logger.e(Constants.TAG_LOG, TAG + " add() activity name: " + getActivityName() + ", container view id: " + containerViewId, e);
            return false;
        }

        return true;
    }

    /**
     * 替换并提交多个Fragment
     *
     * @param fragments a Map Key => Fragment Container View Id, Value => a Fragment
     * @return Returns True, or False 退出Activity后或旋转屏幕后再提交Transaction，会执行失败
     * @throws NullPointerException  the parameter fragments is null
     * @throws IllegalStateException 替换已使用Fragment的Container View Id
     */
    public boolean batchReplace(Map<Integer, Fragment> fragments) throws NullPointerException, IllegalStateException {
        if (fragments == null) {
            Logger.e(Constants.TAG_LOG, TAG + " batchReplace() activity name: " + getActivityName() + ", fragments is null");
            throw new NullPointerException("Fragments is null");
        }

        if (fragments.isEmpty()) {
            Logger.e(Constants.TAG_LOG, TAG + " batchReplace() activity name: " + getActivityName() + ", fragments is empty");
            return false;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            int containerViewId = entry.getKey();
            Fragment fragment = entry.getValue();
            Logger.d(Constants.TAG_LOG, TAG + " batchReplace() activity name: " + getActivityName() + ", container view id: " + containerViewId);

            transaction.replace(containerViewId, fragment);
        }

        try {
            int identifier = transaction.addToBackStack(null).commit();
            Logger.d(Constants.TAG_LOG, TAG + " batchReplace() activity name: " + getActivityName() + ", transaction id: " + identifier);
        } catch (IllegalStateException e) {
            Logger.e(Constants.TAG_LOG, TAG + " batchReplace() activity name: " + getActivityName(), e);
            return false;
        }

        return true;
    }

    /**
     * 替换并提交一个Fragment
     *
     * @param containerViewId Fragment Container View Id
     * @param fragment        a Fragment Object
     * @return Returns True, or False 退出Activity后或旋转屏幕后再提交Transaction，会执行失败
     * @throws IllegalStateException 替换已使用Fragment的Container View Id
     */
    public boolean replace(@IdRes int containerViewId, Fragment fragment) throws IllegalStateException {
        FragmentTransaction transaction = getFragmentManager().beginTransaction().replace(containerViewId, fragment).addToBackStack(null);

        try {
            int identifier = transaction.commit();
            Logger.d(Constants.TAG_LOG, TAG + " replace() activity name: '" + getActivityName() + "', container view id: " + containerViewId + ", transaction id: " + identifier);
        } catch (IllegalStateException e) {
            Logger.e(Constants.TAG_LOG, TAG + " replace() activity name: '" + getActivityName() + "', container view id: " + containerViewId, e);
            return false;
        }

        return true;
    }

    /**
     * 删除并提交一个Fragment
     *
     * @param fragment a Fragment Object
     * @return Returns True, or False 退出Activity后或旋转屏幕后再提交Transaction，会执行失败
     */
    public boolean remove(Fragment fragment) {
        try {
            int identifier = getFragmentManager().beginTransaction().remove(fragment).addToBackStack(null).commit();
            Logger.d(Constants.TAG_LOG, TAG + " remove() activity name: '" + getActivityName() + "', transaction id: " + identifier);
        } catch (IllegalStateException e) {
            Logger.e(Constants.TAG_LOG, TAG + " remove() activity name: '" + getActivityName() + "'", e);
            return false;
        }

        return true;
    }

    /**
     * 显示并提交一个Fragment
     *
     * @param fragment a Fragment Object
     * @return Returns True, or False 退出Activity后或旋转屏幕后再提交Transaction，会执行失败
     */
    public boolean show(Fragment fragment) {
        try {
            int identifier = getFragmentManager().beginTransaction().show(fragment).addToBackStack(null).commit();
            Logger.d(Constants.TAG_LOG, TAG + " show() activity name: '" + getActivityName() + "', transaction id: " + identifier);
        } catch (IllegalStateException e) {
            Logger.e(Constants.TAG_LOG, TAG + " show() activity name: '" + getActivityName() + "'", e);
            return false;
        }

        return true;
    }

    /**
     * 隐藏并提交一个Fragment
     *
     * @param fragment a Fragment Object
     * @return Returns True, or False 退出Activity后或旋转屏幕后再提交Transaction，会执行失败
     */
    public boolean hide(Fragment fragment) {
        try {
            int identifier = getFragmentManager().beginTransaction().hide(fragment).addToBackStack(null).commit();
            Logger.d(Constants.TAG_LOG, TAG + " hide() activity name: '" + getActivityName() + "', transaction id: " + identifier);
        } catch (IllegalStateException e) {
            Logger.e(Constants.TAG_LOG, TAG + " hide() activity name: '" + getActivityName() + "'", e);
            return false;
        }

        return true;
    }

    /**
     * 获取Fragment管理类
     *
     * @return a FragmentManager Object
     */
    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    /**
     * 获取Activity名
     *
     * @return Activity名
     */
    public String getActivityName() {
        return mActivityName;
    }

}
