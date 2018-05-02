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

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;
import com.trotri.android.thunder.ap.TypeCast;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Aes class file
 * AES加解密类
 * 转换方式：AES/CBC/PKCS5Padding
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Aes.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class Aes {

    public static final String TAG = "Aes";

    /**
     * 加密模式
     */
    public static final int MODE_ENCRYPT = Cipher.ENCRYPT_MODE;

    /**
     * 解密模式
     */
    public static final int MODE_DECRYPT = Cipher.DECRYPT_MODE;

    /**
     * 算法
     */
    public static final String ALGORITHM = "AES";

    /**
     * 转换方式
     */
    public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * 密钥长度
     */
    public static final int KEY_LEN = 32;

    /**
     * 向量长度
     */
    public static final int IV_LEN = 16;

    /**
     * 加解密类：{@link #TRANSFORMATION}转换方式
     */
    private static Cipher sCipher;

    /**
     * 解密运算
     *
     * @param ciphertext 密文
     * @param key        密钥
     * @param iv         向量
     * @return 明文，字节数组，或null
     */
    public static byte[] decode(byte[] ciphertext, String key, String iv) {
        return doFinal(MODE_DECRYPT, ciphertext, key, iv);
    }

    /**
     * 加密运算
     *
     * @param plaintext 明文
     * @param key       密钥
     * @param iv        向量
     * @return 密文，字节数组，或null
     */
    public static byte[] encode(byte[] plaintext, String key, String iv) {
        return doFinal(MODE_ENCRYPT, plaintext, key, iv);
    }

    /**
     * 加解密运算
     *
     * @param mode  操作模式，加密模式{@link #MODE_ENCRYPT}或解密模式{@link #MODE_DECRYPT}
     * @param value 待加解密的串，密文或明文
     * @param key   密钥
     * @param iv    向量
     * @return 已加解密的串，密文或明文，字节数组，或null
     */
    public static byte[] doFinal(int mode, byte[] value, String key, String iv) {
        if (mode != MODE_ENCRYPT && mode != MODE_DECRYPT) {
            Logger.e(Constants.TAG_LOG, TAG + " doFinal() mode '" + mode + "' is wrong, mode must be " + MODE_ENCRYPT + " or " + MODE_DECRYPT);
            return null;
        }

        if (value == null) {
            Logger.e(Constants.TAG_LOG, TAG + " doFinal() mode '" + mode + "', value is null");
            return null;
        }

        if (key == null) {
            Logger.e(Constants.TAG_LOG, TAG + " doFinal() mode '" + mode + "', key is null");
            return null;
        }

        if (iv == null) {
            Logger.e(Constants.TAG_LOG, TAG + " doFinal() mode '" + mode + "', iv is null");
            return null;
        }

        byte[] keyBytes = keyToBytes(key);
        if (keyBytes == null) {
            Logger.e(Constants.TAG_LOG, TAG + " doFinal() mode '" + mode + "', keyBytes is null");
            return null;
        }

        byte[] ivBytes = ivToBytes(iv);
        if (ivBytes == null) {
            Logger.e(Constants.TAG_LOG, TAG + " doFinal() mode '" + mode + "', ivBytes is null");
            return null;
        }

        Cipher cipher = getCipher();
        if (cipher == null) {
            Logger.e(Constants.TAG_LOG, TAG + " doFinal() mode '" + mode + "', Cipher is null");
            return null;
        }

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        try {
            cipher.init(mode, keySpec, ivSpec);
            return cipher.doFinal(value);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | BadPaddingException e) {
            Logger.e(Constants.TAG_LOG, TAG + " doFinal()", e);
        }

        return null;
    }

    /**
     * 密钥转字节数组，确保字节数等于“密钥字节数{@link #KEY_LEN}”
     *
     * @param key 密钥
     * @return Returns the converted value if it exists, or null
     */
    public static byte[] keyToBytes(String key) {
        if (key == null) {
            Logger.e(Constants.TAG_LOG, TAG + " keyToBytes() key is null");
            return null;
        }

        byte[] oldKeyBytes = TypeCast.toBytes(key);
        if (oldKeyBytes == null) {
            Logger.e(Constants.TAG_LOG, TAG + " keyToBytes() TypeCast.toBytes() oldKeyBytes is null, key: " + key);
            return null;
        }

        byte[] newKeyBytes = new byte[KEY_LEN];
        Arrays.fill(newKeyBytes, (byte) 0x0);

        int minLen = (oldKeyBytes.length < newKeyBytes.length) ? oldKeyBytes.length : newKeyBytes.length;
        System.arraycopy(oldKeyBytes, 0, newKeyBytes, 0, minLen);

        return newKeyBytes;
    }

    /**
     * 向量转字节数组，确保字节数等于“密钥字节数{@link #IV_LEN}”
     *
     * @param iv 密钥
     * @return Returns the converted value if it exists, or null
     */
    public static byte[] ivToBytes(String iv) {
        if (iv == null) {
            Logger.e(Constants.TAG_LOG, TAG + " ivToBytes() iv is null");
            return null;
        }

        byte[] oldIvBytes = TypeCast.toBytes(iv);
        if (oldIvBytes == null) {
            Logger.e(Constants.TAG_LOG, TAG + " ivToBytes() TypeCast.toBytes() oldIvBytes is null, iv: " + iv);
            return null;
        }

        byte[] newIvBytes = new byte[IV_LEN];
        Arrays.fill(newIvBytes, (byte) 0x0);

        int minLen = (oldIvBytes.length < newIvBytes.length) ? oldIvBytes.length : newIvBytes.length;
        System.arraycopy(oldIvBytes, 0, newIvBytes, 0, minLen);

        return newIvBytes;
    }

    /**
     * 获取加解密类：{@link #TRANSFORMATION}转换方式
     *
     * @return a Cipher Object, or null
     */
    public static Cipher getCipher() {
        if (sCipher == null) {
            try {
                sCipher = Cipher.getInstance(TRANSFORMATION);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getCipher()", e);
            }
        }

        return sCipher;
    }

}
