Android Framework
======
<br>

Thunder
------
<br>

### 核心包
> + 权限检查类 - 已检查的权限：访问手机状态、网络、访问网络状态、访问Wifi状态、定位、读取通讯录、Sd卡、接受手机启动广播
> + 崩溃处理类 - Thread.UncaughtExceptionHandler子类
> + 日志处理类 - 输出日志时自定义日志回调接口，如：提交日志到服务器
> + 网络辅助类 - 是否联网、联网类型、子网络类型、网络信息类和扩展信息
> + 系统服务管理类 - 通过名字获取系统服务：手机信息服务类、网络状态服务类、Wifi状态服务类、定位系统服务类和剪贴板服务类
> + 标准线程池类 - 通过线程池创建子线程，并指定子线程崩溃处理类
> + 类型转换类 - 避免NumberFormatException，Obj2Int、Obj2Long、Obj2Float、Obj2Double、ByteArr2Hex、Str2ByteArr、ByteArr2Str、Int2Ip、ByteArr2Mac
> + 主线程类 - 在主线程中执行代码，用于从子线程切换到主线程

### HTTP包
> + HTTP类
> + HTTP异步类 - 在后台线程中执行HTTP请求，执行完后，在主线程中回调Listener接口
> + 下载类
> + 下载异步类 - 在后台线程中执行下载，执行完后，在主线程中回调Listener接口
> + 网络连接类 - 移动运营商类型，选择合适的代理
> + 移动运营商类型
> + 联网类型

### DB包
> + 数据库表的概要描述 - 包含表名、主键、表的自增字段、字段名、字段默认值等
> + 数据库列的概要描述 - 包含列名、类型、长度、默认值、是否是主键、是否自增、是否允许为空等
> + 数据库操作类 - 封装增删改查
> + 全局数据寄存类 - 使用DB寄存全局数据，Key => Value
> + 异步全局数据寄存类 - 在后台线程中执行存储，执行完后，在主线程中回调Listener接口

### 文件包
> + SdCard文件读写类 - Secure Digital Memory Card 读写类
> + 沙盒文件读写类 - 应用沙盒目录中文件读写类，/data/data/<package name>/files
> + 文件查找类 - 通过文件名和后缀名查找

### 手机状态包
> + 通讯录管理类
> + 屏幕信息类 - 宽、高、密度等
> + 位置信息类
> + Manifest信息类 - 获取AndroidManifest.xml中信息
> + MetaData数据类 - 获取AndroidManifest.xml中<meta-data android:name="" android:value="" />
> + 网络信息类 - Wifi名称、Mac地址、Ip地址
> + 手机信息类 - 设备识别码、设备Id、网络运营商类型、网络运营商类型名称、Android Id、系统语言
> + 版本信息类 - 获取AndroidManifest.xml中manifest:package、android:versionCode、android:versionName

### 常用工具包
> + 剪贴板辅助类 - 
> + DexClassLoader辅助类 - Dex方式热更新Jar代码
> + 单位转换类 - Dp to Px、Px to Dp、Sp to Px、Px to Sp
> + 图片辅助类 - Bitmap to Drawable、图片信息、缩放比例
> + 随机数辅助类
> + 全局数据寄存类 - 使用SharedPreferences寄存

### 加解密包
> + AES加解密类 - 转换方式：AES/CBC/PKCS5Padding
> + MD5加密类
> + URL安全的Base64加解密类 - 还原和去掉Base64密文中的+、/、=

### 视图包
> + 浏览器类
> + 沉浸辅助类
> + 上拉加载更多提示布局
> + 浏览器辅助类

### 依赖注入包
> + ViewModel注解
> + 视图注解

Rice
------
<br>

### 基类包
> + Activity基类
> + Fragment基类
> + 数据处理器基类

### 数据包
> + 常用错误码类
> + 常用错误信息类

### DB包
> + DbRetrofit - 类似Retrofit，处理Db相关操作

### 常用工具包
> + RxJava方法管理类 - 用于Mvvm模式，方法绑定和通知
> + RetrofitBuilder - Build a new Retrofit
> + DbRetrofitBuilder - Build a new DbRetrofit
> + Fragment辅助类
> + Gson辅助类 - 适配m前缀的属性名
> + 资源管理类 - 通过名字获取资源值
> + 计划任务类 - 周期执行和延迟执行

### 视图包
> + RecyclerView Adapter 基类 - View包含一个Header和Footer
> + 标准的 RecyclerView Adapter 基类 - 用于 DataBinding，View包含一个Header和Footer
> + 标准的 RecyclerView Adapter 基类 - View包含一个Header和Footer
> + RecyclerView ItemDecoration 类
> + RecyclerView LinearLayout ItemDecoration 类
> + RecyclerView Expandable ItemDecoration 类
> + RecyclerView GridLayout StaggeredGrid ItemDecoration 类

### Js包

###### 监听标题栏按钮
标题栏有2个按钮，“返回按钮”和“菜单按钮”，应该由Android代码处理2个按钮的点击事件（暂时没开发）。<br>
目前只用Js的方式通知按钮被点击了，在Html页面Js没加载完毕的时候，点击2个按钮会没有响应。<br>
<pre><code>
/**
 * 设置标题栏监听者
 */
TitleBar.addListener({
    /**
     * 监听标题栏点击事件
     */
    onClick: function(type) {
        // 点击“返回按钮”
        if (TitleBar.isBackward(type)) {
            alert("Backward Clicked");
        }

        // 点击“菜单按钮”
        if (TitleBar.isMenus(type)) {
            alert("Menus Clicked");
        }
    }
});
</code></pre>
<br>

###### 调用Js方法说明
调用Js方法格式<br>
<pre><code>
tt.方法名({
    参数1: 值1,
    参数2: 值2,
    success: function(data) {}, // 调用成功时执行，data（Json）
    error: function(throwable) {}, // 调用失败时执行，throwable（Json {errNo: 1, errMsg: ""}）中包括错误码和错误消息
    complete: function(result) {}, // 无论成功或失败，都回调该方法，result: true or false
});
</code></pre>
<br>

Demo中调用了2个Js方法：<br>
Demo：https://github.com/trotri/thunder/blob/master/app/src/main/assets/browser/demo.html<br>
1、提示框，对应Java的ToastHandler类<br>
<pre><code>
tt.toast({
    text: text, // 参数：提示内容
    isLong: isLong, // 参数：展示时长，true: 长时间展示（约3.5s）、false：短时间展示（约1.5s）
    error: function(throwable) { // 调用失败时执行，throwable（Json {errNo: 1, errMsg: ""}）中包括错误码和错误消息
        showJson(throwable);
    }
});
</code></pre>
<br>

2、获取版本信息，对应Java的ToastHandler类<br>
<pre><code>
tt.getVersion({
    success: function(data) { // 调用成功时执行，data(Json)中包括版本号和版本名
        showJson(data);
    },
    error: function(throwable) { // 调用失败时执行，throwable(Json{errNo: 1, errMsg: ""})中包括错误码和错误消息
        showJson(throwable);
    },
    complete: function(result) { // 无论成功或失败，都回调该方法，result: true or false
        alert(result);
    },
});
</code></pre>
<br>

###### error: function(throwable)，throwable(Json{errNo: 1, errMsg: ""})中系统的错误码和错误消息说明
1、安卓系统没有window.Thunder对象时，抛出错误 = { errNo: -1, errMsg: "window.Thunder undefined" }<br>
2、系统类型未知时，抛出错误 = { errNo: -2, errMsg: "operating system type undefined" }<br>
3、Android没提供该方法，抛出错误 = { errNo: -3, errMsg: "Js method not found exception" }<br>
4、参数不是有效的Json格式，抛出错误 = { errNo: -4, errMsg: "Js parameter json syntax exception" }<br>
<br>

###### Js方法库，由Android开发者维护
网址：https://github.com/trotri/thunder/blob/master/app/src/main/assets/browser/ThunderJsBridge.js<br>
将TrotriJSBridge.js拷贝到自己的服务器，自己的网页中需要引用该Js，<script src="http://yoursite.com/***/TrotriJSBridge.js"></script><br>
如果想知道Android提供了哪些Js方法，除了参考Demo，还可以参考TrotriJSBridge.js中的tt类
<br>

### Js方法，Android代码说明（H5开发者可以忽略该说明）
每个Js方法对应一个Java类，全部在该目录下。<br>
目录：https://github.com/trotri/thunder/tree/master/app/src/main/java/com/trotri/android/rice/js/handlers<br>
ToastHandler类：提示框，参数：提示内容、展示时长<br>
VersionHandler类：获取版本信息，参数：无<br>
<br>
