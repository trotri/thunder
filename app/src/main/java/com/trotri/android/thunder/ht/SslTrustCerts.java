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

import com.trotri.android.thunder.ap.Constants;
import com.trotri.android.thunder.ap.Logger;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SslTrustCerts class file
 * HTTPS请求忽略证书
 * 注：一个IP配置多个证书，Https请求会报javax.net.ssl.SSLException异常
 * javax.net.ssl.SSLException: hostname in certificate didn't match: <url> != <url> OR <url> OR <url>
 * Https请求前执行：SslTrustCerts.enable(); 可以在程序启动的时候，Application类中执行。
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: SslTrustCerts.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public class SslTrustCerts {

    public static final String TAG = "SslTrustCerts";

    /**
     * 协议
     */
    public static final String PROTOCOL = "SSL";

    /**
     * X509证书类
     */
    public static final X509Certificate[] X509_CERTIFICATES = new X509Certificate[0];

    /**
     * X509证书管理类
     */
    public static final X509TrustManager X509_TRUST_MANAGER = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return X509_CERTIFICATES;
        }
    };

    /**
     * 证书管理类
     */
    public static final TrustManager[] TRUST_MANAGERS = new TrustManager[]{X509_TRUST_MANAGER};

    /**
     * 域名验证类
     */
    public static final HostnameVerifier HOSTNAME_VERIFIER = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 随机数类
     */
    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * SSL上下文环境
     */
    private static SSLContext sSslContext;

    /**
     * 授权所有的HTTPS请求
     */
    public static void enable() {
        SSLContext sslContext = getSslContext();
        if (sslContext == null) {
            Logger.e(Constants.TAG_LOG, TAG + " enable() SSLContext is null");
            return;
        }

        try {
            sslContext.init(null, TRUST_MANAGERS, SECURE_RANDOM);
        } catch (KeyManagementException e) {
            Logger.e(Constants.TAG_LOG, TAG + " enable()", e);
            return;
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER);
    }

    /**
     * 获取SSL上下文环境
     *
     * @return a SSLContext Object, or null
     */
    public static SSLContext getSslContext() {
        if (sSslContext == null) {
            try {
                sSslContext = SSLContext.getInstance(PROTOCOL);
            } catch (NoSuchAlgorithmException e) {
                Logger.e(Constants.TAG_LOG, TAG + " getSslContext()", e);
            }
        }

        return sSslContext;
    }

}
