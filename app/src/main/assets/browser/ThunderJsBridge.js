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

/**
 * Java Script Bridge Object
 */
var ThunderJsBridge = {};

/**
 * 系统类型
 */
ThunderJsBridge.OS = {
    /**
     * 安卓系统
     */
    TYPE_ANDROID: 1,

    /**
     * iOS系统
     */
    TYPE_IOS: 2,

    /**
     * 未知系统
     */
    TYPE_UNDEFINED: 0,

    /**
     * 系统类型
     */
    type: 0,

    /**
     * 获取系统类型，0：未知、1：Android、2：iOS
     */
    getType: function() {
        if (ThunderJsBridge.OS.type == ThunderJsBridge.OS.TYPE_UNDEFINED) {
            var userAgent = ThunderJsBridge.OS.getUserAgent();
            if (userAgent.indexOf("Android") > -1) {
                ThunderJsBridge.OS.type = ThunderJsBridge.OS.TYPE_ANDROID;
            } else {
                // 其他手机型号
            }
        }

        return ThunderJsBridge.OS.type;
    },

    /**
     * 获取是否是安卓系统
     */
    isAndroid: function() {
        return ThunderJsBridge.OS.getType() == ThunderJsBridge.OS.TYPE_ANDROID;
    },

    /**
     * 获取浏览器用户代理信息
     */
    getUserAgent: function() {
        return navigator.userAgent;
    }

};

/**
 * 回调函数管理
 */
ThunderJsBridge.Listeners = {
    /**
     * 寄存回调函数的Key长度
     */
    KEY_LEN: 8,

    /**
     * 寄存回调函数的仓库
     */
    data: [],

    /**
     * 获取回调函数
     */
    get: function(key) {
        if (ThunderJsBridge.Listeners.has(key)) {
            return ThunderJsBridge.Listeners.data[key];
        }

        return null;
    },

    /**
     * 添加回调函数
     */
    add: function(key, value) {
        ThunderJsBridge.Listeners.data[key] = value;
    },

    /**
     * 删除回调函数
     */
    remove: function(key) {
        delete ThunderJsBridge.Listeners.data[key];
    },

    /**
     * 获取回调函数是否存在
     */
    has: function(key) {
        return (ThunderJsBridge.Listeners.data[key] != undefined);
    },

    /**
     * 获取寄存回调函数的Key
     */
    getKey: function() {
        var result = "";

        do {
            result = ThunderJsBridge.Listeners.randStr(ThunderJsBridge.Listeners.KEY_LEN);
        }
        while (ThunderJsBridge.Listeners.has(result));

        return result;
    },

    /**
     * 获取指定长度的随机字符串
     */
    randStr: function(len) {
        var result = "";

        if ((len = parseInt(len)) <= 0) {
            return result;
        }

        var chars = "abcdefghijklmnopqrstuvwxyz";
        for (var i = 0; i < len; i++) {
            result += chars.charAt(Math.floor(Math.random() * chars.length));
        }

        return result;
    }

};

/**
 * 日志处理
 */
ThunderJsBridge.Log = {
    /**
     * 成功码
     */
    SUCCESS_NO: 0,

    /**
     * 错误码，window.Thunder undefined 只适用安卓系统
     */
    ERRNO_THUNDER_UNDEFINED: -1,

    /**
     * 错误码，operating system type undefined
     */
    ERRNO_OS_UNDEFINED: -2,

    /**
     * 错误码，Native exception Js method not found
     */
    ERRNO_METHOD_NOT_FOUND: -3,

    /**
     * 错误码，Native exception Js parameter json syntax wrong
     */
    ERRNO_JSON_SYNTAX_: -4,

    /**
     * 抛出错误
     * listener = { error: function(throwable) {} }
     * throwable = { errNo: 0, errMsg: "" }
     */
    throwError: function(listener, throwable) {
        if (listener == null || typeof(listener) != "object" || listener.error == undefined) {
            alert(JSON.stringify(throwable));
        } else {
            listener.error(throwable);
        }
    }

};

/**
 * 向Native发送请求
 * parameter = { beforeAsync: function(data) {}, success: function(data) {}, error: function(data) {}, complete: function(result) {} }
 * 请求Native成功时，回调success函数，否则回调error函数，无论成功失败，都会回调complete函数，result = true | false
 *
 * 安卓系统没有window.Thunder对象时，抛出错误 = { errNo: ThunderJsBridge.Log.ERRNO_THUNDER_UNDEFINED, errMsg: "window.Thunder undefined" }
 * 系统类型未知时，抛出错误 = { errNo: ThunderJsBridge.Log.ERRNO_OS_UNDEFINED, errMsg: "operating system type undefined" }
 */
ThunderJsBridge.invoke = function(method, parameter) {
    // 只适用安卓系统
    if (ThunderJsBridge.OS.isAndroid() && !window.Thunder) {
        ThunderJsBridge.Log.throwError(parameter, {errNo: ThunderJsBridge.Log.ERRNO_THUNDER_UNDEFINED, errMsg: "window.Thunder undefined"});
        return;
    }

    if (parameter == null) {
        parameter = {};
    }

    var key = ThunderJsBridge.Listeners.getKey();
    ThunderJsBridge.Listeners.add(key, parameter);

    parameter = JSON.stringify(parameter);
    var type = ThunderJsBridge.OS.getType();
    switch (type) {
        case ThunderJsBridge.OS.TYPE_ANDROID:
            Thunder.invoke(key, method, parameter);
            break;
        case ThunderJsBridge.OS.TYPE_IOS:
            document.location = "Thunder://invoke?" + encodeURIComponent(key) + "&" + encodeURIComponent(method) + "&" + encodeURIComponent(parameter);
            break;
        default:
            ThunderJsBridge.Log.throwError(listener, {errNo: ThunderJsBridge.Log.ERRNO_OS_UNDEFINED, errMsg: "operating system type undefined"});
            break;
    }

};

/**
 * 监听Native回调
 * throwable.errNo == ThunderJsBridge.Log.SUCCESS_NO时，回调success函数，否则回调error函数
 * 无论成功失败，都会回调complete函数，result = true | false
 */
ThunderJsBridge.onListen = function(key, data, throwable) {
    var listener = ThunderJsBridge.Listeners.get(key);
    ThunderJsBridge.Listeners.remove(key);

    if (typeof(listener) != "object") {
        ThunderJsBridge.Log.throwError(listener, throwable);
        return;
    }

    if (throwable.errNo != ThunderJsBridge.Log.SUCCESS_NO) {
        ThunderJsBridge.Log.throwError(listener, throwable);

        if (listener.complete != undefined && listener.complete != null) {
            listener.complete(false);
        }

        return;
    }

    if (listener.success != undefined && listener.success != null) {
        if (data == "" || data == undefined || data == null) {
            listener.success();
        } else {
            listener.success(data);
        }
    }

    if (listener.complete != undefined && listener.complete != null) {
        listener.complete(true);
    }

};

/**
 * 监听Native异步处理前的回调
 */
ThunderJsBridge.onBeforeAsyncListen = function(key, data) {
    var listener = ThunderJsBridge.Listeners.get(key);
    ThunderJsBridge.Listeners.remove(key);

    if (typeof(listener) != "object" || listener.beforeAsync == undefined || listener.beforeAsync == null) {
        return;
    }

    if (data == "" || data == undefined || data == null) {
        listener.beforeAsync();
    } else {
        listener.beforeAsync(data);
    }

};

/**
 * 标题栏
 */
TitleBar = {
    /**
     * 后退按钮
     */
    TYPE_BACKWARD: "backward",

    /**
     * 菜单按钮
     */
    TYPE_MENUS: "menus",

    /**
     * 按钮监听者，Java Script Object，必须包含一个onClick(type)方法
     */
    listeners: [],

    /**
     * 设置按钮监听者
     * Java Script Object，必须包含一个onClick(type)方法
     */
    addListener: function(listener) {
        if (listener == undefined || listener == null || typeof(listener) != "object") {
            return false;
        }

        if (listener.onClick == undefined || listener.onClick == null) {
            return false;
        }

        TitleBar.listeners.push(listener);
        return true;
    },

    /**
     * 按钮类型，backward：后退按钮、menus：菜单按钮
     */
    onClick: function(type) {
        for (var i in TitleBar.listeners) {
            TitleBar.listeners[i].onClick(type);
        }
    },

    /**
     * 获取是否是后退按钮
     */
    isBackward: function(type) {
        return type == TitleBar.TYPE_BACKWARD;
    },

    /**
     * 获取是否是菜单按钮
     */
    isMenus: function(type) {
        return type == TitleBar.TYPE_MENUS;
    }

};

/**
 * tt Object
 */
var tt = {
    /**
     * 提示
     */
    toast: function(p) {
        ThunderJsBridge.invoke("toast", {
            text: p.text, // 提示内容
            isLong: (p.isLong == undefined) ? false : (p.isLong ? true : false), // 展示时间
            success: p.success,
            error: p.error,
            complete: p.complete
        });
    },

    /**
     * 获取版本信息
     */
    getVersion: function(p) {
        ThunderJsBridge.invoke("getVersion", {
            success: p.success, // data = { code: 版本号, name: 版本名 }
            error: p.error,
            complete: p.complete
        });
    }

};
