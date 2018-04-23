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

package com.trotri.android.thunder.ap;

/**
 * HttpStatus final class file
 * HTTP状态码
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: HttpStatus.java 1 2015-02-22 10:00:06Z huan.song $
 * @since 1.0
 */
public final class HttpStatus {
    /**
     * Continue (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_CONTINUE = 100;

    /**
     * Switching Protocols (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_SWITCHING_PROTOCOLS = 101;

    /**
     * Processing (WebDAV - RFC 2518)
     */
    public static final int SC_PROCESSING = 102;

    /**
     * OK (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_OK = 200;

    /**
     * Created (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_CREATED = 201;

    /**
     * Accepted (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_ACCEPTED = 202;

    /**
     * Non Authoritative Information (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;

    /**
     * No Content (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_NO_CONTENT = 204;

    /**
     * Reset Content (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_RESET_CONTENT = 205;

    /**
     * Partial Content (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_PARTIAL_CONTENT = 206;

    /**
     * Multi-Status (WebDAV - RFC 2518) or 207 Partial Update OK (HTTP/1.1 - draft-ietf-http-v11-spec-rev-01?)
     */
    public static final int SC_MULTI_STATUS = 207;

    /**
     * Mutliple Choices (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_MULTIPLE_CHOICES = 300;

    /**
     * Moved Permanently (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_MOVED_PERMANENTLY = 301;

    /**
     * Moved Temporarily (Sometimes Found) (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_MOVED_TEMPORARILY = 302;

    /**
     * See Other (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_SEE_OTHER = 303;

    /**
     * Not Modified (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_NOT_MODIFIED = 304;

    /**
     * Use Proxy (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_USE_PROXY = 305;

    /**
     * Temporary Redirect (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_TEMPORARY_REDIRECT = 307;

    /**
     * Bad Request (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_BAD_REQUEST = 400;

    /**
     * Unauthorized (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_UNAUTHORIZED = 401;

    /**
     * Payment Required (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_PAYMENT_REQUIRED = 402;

    /**
     * Forbidden (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_FORBIDDEN = 403;

    /**
     * Not Found (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_NOT_FOUND = 404;

    /**
     * Method Not Allowed (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_METHOD_NOT_ALLOWED = 405;

    /**
     * Not Acceptable (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_NOT_ACCEPTABLE = 406;

    /**
     * Proxy Authentication Required (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;

    /**
     * Request Timeout (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_REQUEST_TIMEOUT = 408;

    /**
     * Conflict (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_CONFLICT = 409;

    /**
     * Gone (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_GONE = 410;

    /**
     * Length Required (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_LENGTH_REQUIRED = 411;

    /**
     * Precondition Failed (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_PRECONDITION_FAILED = 412;

    /**
     * Request Entity Too Large (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_REQUEST_TOO_LONG = 413;

    /**
     * Request-URI Too Long (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_REQUEST_URI_TOO_LONG = 414;

    /**
     * Unsupported Media Type (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;

    /**
     * Requested Range Not Satisfiable (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

    /**
     * Expectation Failed (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_EXPECTATION_FAILED = 417;

    /**
     * Static constant for a 419 error.
     */
    public static final int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 419;

    /**
     * Static constant for a 420 error.
     */
    public static final int SC_METHOD_FAILURE = 420;

    /**
     * Unprocessable Entity (WebDAV - RFC 2518)
     */
    public static final int SC_UNPROCESSABLE_ENTITY = 422;

    /**
     * Locked (WebDAV - RFC 2518)
     */
    public static final int SC_LOCKED = 423;

    /**
     * Failed Dependency (WebDAV - RFC 2518)
     */
    public static final int SC_FAILED_DEPENDENCY = 424;

    /**
     * Server Error (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_INTERNAL_SERVER_ERROR = 500;

    /**
     * Not Implemented (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_NOT_IMPLEMENTED = 501;

    /**
     * Bad Gateway (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_BAD_GATEWAY = 502;

    /**
     * Service Unavailable (HTTP/1.0 - RFC 1945)
     */
    public static final int SC_SERVICE_UNAVAILABLE = 503;

    /**
     * Gateway Timeout (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_GATEWAY_TIMEOUT = 504;

    /**
     * HTTP Version Not Supported (HTTP/1.1 - RFC 2616)
     */
    public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;

    /**
     * Insufficient Storage (WebDAV - RFC 2518)
     */
    public static final int SC_INSUFFICIENT_STORAGE = 507;

}
