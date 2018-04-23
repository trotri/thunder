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

import java.util.Random;

/**
 * RandHelper class file
 * 随机数辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: RandHelper.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class RandHelper {

    public static final String TAG = "RandHelper";

    /**
     * 随机数类
     */
    private static final Random INSTANCE = new Random();

    /**
     * 获取两非负数之间的随机数
     *
     * @param min 最小非负数，随机数包含此数
     * @param max 最大非负数，随机数不包含此数
     * @return 随机数，[最小-最大)
     */
    public static int betweenInt(int min, int max) {
        if (min < 0) {
            return 0;
        }

        if (max <= 0) {
            return min;
        }

        int bound = max - min;
        return nextInt(bound) + min;
    }

    /**
     * 获取[0-正整数)之间的随机数
     *
     * @param bound 正整数，随机数不包含此数
     * @return 随机数，[0-正整数)
     */
    public static int nextInt(int bound) {
        if (bound <= 0) {
            return 0;
        }

        return INSTANCE.nextInt(bound);
    }

    /**
     * 获取一个唯一的随机串（不完全唯一）
     *
     * @param num 随机数位数
     * @return 随机串，当前时间 + num位随机数
     */
    public static String unique(int num) {
        if (num <= 0) {
            return "" + currentTimeMillis();
        }

        return currentTimeMillis() + "" + betweenInt(10 * num, 100 * num);
    }

    /**
     * 获取当前时间，精确到毫秒
     *
     * @return 当前时间，单位：毫秒
     */
    public static long currentTimeMillis() {
        return System.currentTimeMillis();
    }

}
