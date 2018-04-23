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

package com.trotri.android.thunder.file;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * FileHelper class file
 * 文件读写辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: FileHelper.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class FileHelper {

    public static final String TAG = "FileHelper";

    /**
     * 读取文件
     *
     * @param in 文件输入流，a FileInputStream Object
     * @return 文件内容，或null
     * @throws NullPointerException 如果参数为null，抛出异常
     * @throws IOException          如果读取文件失败，抛出异常
     */
    public static String read(FileInputStream in) throws NullPointerException, IOException {
        if (in == null) {
            throw new NullPointerException();
        }

        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder data = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }

            return data.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * 写入文件
     *
     * @param out  文件输出流，a FileOutputStream Object
     * @param data 需要写入或追加的数据
     * @throws NullPointerException 如果参数为null，抛出异常
     * @throws IOException          如果写入文件失败，抛出异常
     */
    public static void write(FileOutputStream out, String data) throws NullPointerException, IOException {
        if (out == null) {
            throw new NullPointerException();
        }

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 删除文件和目录（递归方式）
     *
     * @param fileName 文件或目录名，包括路径
     * @throws NullPointerException 如果参数为null，抛出异常
     * @throws SecurityException    如果没有删除权限，抛出异常
     */
    public static boolean remove(String fileName) throws NullPointerException, SecurityException {
        if (fileName == null) {
            throw new NullPointerException();
        }

        return remove(new File(fileName));
    }

    /**
     * 删除文件和目录（递归方式）
     *
     * @param f 文件或目录对象，a File Object
     * @return Returns True, or False
     * @throws NullPointerException 如果参数为null，抛出异常
     * @throws SecurityException    如果没有删除权限，抛出异常
     */
    public static boolean remove(File f) throws NullPointerException, SecurityException {
        if (!f.exists()) {
            return true;
        }

        delete(f);
        return !f.exists();
    }

    /**
     * 删除文件和目录（递归方式）
     *
     * @param f 文件或目录对象，a File Object
     * @throws NullPointerException 如果参数为null，抛出异常
     * @throws SecurityException    如果没有删除权限，抛出异常
     */
    public static void delete(File f) throws NullPointerException, SecurityException {
        if (f == null) {
            throw new NullPointerException();
        }

        if (f.isDirectory()) {
            File[] rows = f.listFiles();
            if (rows != null) {
                for (File value : rows) {
                    if (value != null) {
                        delete(value);
                    }
                }
            }
        }

        if (!f.delete()) {
            throw new SecurityException("File Delete Failure, fileName: " + f.getAbsolutePath());
        }
    }

    /**
     * 文件是否存在
     *
     * @param fileName 文件或目录名，包括路径
     * @return Returns the File Object if it exists, or null
     * @throws NullPointerException  如果参数为null，抛出异常
     * @throws FileNotFoundException 如果文件不存在，抛出异常
     */
    public static File exists(String fileName) throws NullPointerException, FileNotFoundException {
        if (fileName == null) {
            throw new NullPointerException();
        }

        File file = new File(fileName);
        if (file.exists()) {
            return file;
        }

        throw new FileNotFoundException("File Not Exists, fileName: " + fileName);
    }

    /**
     * 获取文件或目录名在应用沙盒中的绝对路径
     *
     * @param fileName 文件或目录名，不需要路径
     * @return 文件或目录名，包括绝对路径
     * @throws NullPointerException  如果参数为null，抛出异常
     * @throws FileNotFoundException 如果沙盒不存在，抛出异常
     */
    public static String sandboxFileName(Context c, String fileName) throws NullPointerException, FileNotFoundException {
        if (fileName == null) {
            throw new NullPointerException();
        }

        File file = sandboxDirectory(c);
        return file.getAbsolutePath() + File.separator + fileName;
    }

    /**
     * 获取应用沙盒目录绝对路径
     *
     * @param c 上下文环境
     * @return 沙盒目录文件对象，a File Object
     * @throws NullPointerException  如果参数为null，抛出异常
     * @throws FileNotFoundException 如果沙盒不存在，抛出异常
     */
    public static File sandboxDirectory(Context c) throws NullPointerException, FileNotFoundException {
        if (c == null) {
            throw new NullPointerException();
        }

        File file = c.getFilesDir();
        if (file == null) {
            throw new FileNotFoundException("Sandbox Not Exists or Cannot Readable");
        }

        return file;
    }

    /**
     * 获取文件或目录名在Sd卡中的绝对路径
     *
     * @param fileName 文件或目录名，不需要路径
     * @return 文件或目录名，包括Sd卡根路径
     * @throws NullPointerException  如果参数为null，抛出异常
     * @throws FileNotFoundException 如果Sd卡不存在，抛出异常
     */
    public static String sdFileName(String fileName) throws NullPointerException, FileNotFoundException {
        if (fileName == null) {
            throw new NullPointerException();
        }

        File file = sdDirectory(true);
        return file.getAbsolutePath() + File.separator + fileName;
    }

    /**
     * 获取Sd卡根目录绝对路径
     *
     * @param throwException 当Sd卡不存在时，True：抛出异常、False：返回null
     * @return 文件对象，a File Object
     * @throws FileNotFoundException 如果Sd卡不存在，返回null，或抛出异常
     */
    public static File sdDirectory(boolean throwException) throws FileNotFoundException {
        File file = sdDirectory();
        if (file == null && throwException) {
            throw new FileNotFoundException("Secure Digital Memory Card Not Exists or Cannot Readable");
        }

        return file;
    }

    /**
     * 获取Sd卡根目录绝对路径
     *
     * @return 文件对象，如果Sd卡不存在，返回null
     */
    public static File sdDirectory() {
        return sdExists() ? Environment.getExternalStorageDirectory() : null;
    }

    /**
     * 检查Sd卡是否存在
     *
     * @return Returns True, or False
     */
    public static boolean sdExists() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

}
