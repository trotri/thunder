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

package com.trotri.android.thunder.crypto;

import android.util.Base64;

/**
 * UrlSafeBase64 class file
 * URL安全的Base64加解密类
 * 还原和去掉Base64密文中的+、/、=
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: UrlSafeBase64.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class UrlSafeBase64 {

    public static final String TAG = "UrlSafeBase64";

    /**
     * 用于填充Base64长度的等于号
     */
    public static final String EQUAL_SIGN_4 = "====";

    /**
     * Base64解密，URL安全转义
     *
     * @param value 密文
     * @return 明文，字节数组，或null
     */
    public static byte[] decode(String value) {
        value = urlDecode(value);
        if (value == null) {
            return null;
        }

        return base64Decode(value);
    }

    /**
     * Base64加密，URL安全转义
     *
     * @param value 明文
     * @return 密文，字符串，或null
     */
    public static String encode(byte[] value) {
        String result = base64Encode(value);
        if (result == null) {
            return null;
        }

        return urlEncode(result);
    }

    /**
     * Base64加密，URL安全转义
     *
     * @param value 明文
     * @return 密文，字符串，或null
     */
    public static String encode(String value) {
        if (value == null) {
            return null;
        }

        return encode(value.getBytes());
    }

    /**
     * Base64解密，普通的解密，非URL安全转义
     *
     * @param value 密文
     * @return 明文，字节数组，或null
     */
    public static byte[] base64Decode(String value) {
        if (value == null) {
            return null;
        }

        return Base64.decode(value, Base64.DEFAULT);
    }

    /**
     * Base64加密，普通的加密，非URL安全转义
     *
     * @param value 明文
     * @return 密文，字符串，或null
     */
    public static String base64Encode(byte[] value) {
        if (value == null) {
            return null;
        }

        return Base64.encodeToString(value, Base64.DEFAULT).replace("\r", "").replace("\n", "");
    }

    /**
     * 转义URL
     * 还原Base64密文中的+、/、=
     *
     * @param value 待转义字符串
     * @return 转义后字符串，或null
     */
    public static String urlDecode(String value) {
        if (value == null) {
            return null;
        }

        int size = value.length() % 4;
        if (size > 0) {
            // 增加 (4 - size) 个 "="
            value += EQUAL_SIGN_4.substring(size);
        }

        return value.replace('-', '+').replace('_', '/');
    }

    /**
     * 转义URL
     * 去掉Base64密文中的+、/、=
     *
     * @param value 待转义字符串
     * @return 转义后字符串，或null
     */
    public static String urlEncode(String value) {
        if (value == null) {
            return null;
        }

        return value.replace('+', '-').replace('/', '_').replaceAll("=", "");
    }

}
