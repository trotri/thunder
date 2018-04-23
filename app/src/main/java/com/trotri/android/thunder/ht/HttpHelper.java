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

package com.trotri.android.thunder.ht;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * HttpHelper class file
 * HTTP辅助类
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: HttpHelper.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class HttpHelper {

    private static final String TAG = "HttpHelper";

    /**
     * 将Map集合拼接成字符串
     *
     * @param params  Map集合，Key => String, Value => String
     * @param charset 字符编码
     * @return 查询串，a Params String
     */
    public static String joinString(Map<String, String> params, String charset) {
        StringBuilder data = new StringBuilder();

        String joinStr = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            data.append(joinStr).append(entry.getKey()).append("=").append(urlEncode(entry.getValue(), charset));
            joinStr = "&";
        }

        return data.toString();
    }

    /**
     * Encode URL
     *
     * @param url     a URL String
     * @param charset 字符编码
     * @return a Encoded URL String
     */
    @SuppressWarnings("deprecation")
    public static String urlEncode(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            Logger.e(Constants.TAG_LOG, TAG + " urlEncode()", e);
        }

        return URLEncoder.encode(url);
    }

    /**
     * 打开Wifi信息设置页面
     *
     * @param c 上下文环境
     */
    public static void toSettings(Context c) {
        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        c.startActivity(i);
    }

    /**
     * 计算是否上传/下载完成
     *
     * @param totalSize 总文件大小
     * @param size      已上传/下载文件大小
     * @return Returns True, or False
     */
    public static boolean isFinish(long totalSize, long size) {
        return (getProgress(totalSize, size) >= 100);
    }

    /**
     * 计算当前上传/下载进度
     *
     * @param totalSize 总文件大小
     * @param size      已上传/下载文件大小
     * @return 返回百分比，0 ~ 100之间
     */
    public static int getProgress(long totalSize, long size) {
        int progress = 0;

        if (totalSize > 0) {
            progress = (int) (((float) size / totalSize) * 100);
            progress = (progress < 0) ? 0 : ((progress > 100) ? 100 : progress);
        }

        return progress;
    }

}
