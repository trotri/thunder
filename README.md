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

### 加解密包
> + AES加解密类 - 转换方式：AES/CBC/PKCS5Padding
> + MD5加密类
> + URL安全的Base64加解密类 - 还原和去掉Base64密文中的+、/、=

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

### HTTP包
> + HTTP类
> + HTTP异步类 - 在后台线程中执行HTTP请求，执行完后，在主线程中回调Listener接口
> + 下载类
> + 下载异步类 - 在后台线程中执行下载，执行完后，在主线程中回调Listener接口
> + 网络连接类 - 移动运营商类型，选择合适的代理
> + 移动运营商类型
> + 联网类型

### 依赖注入
> + ViewModel注解
> + 视图注解

### 手机状态
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

### 视图包
> + 浏览器类
> + 沉浸辅助类
> + 上拉加载更多提示布局
> + 浏览器辅助类

Rice
------
<br>

