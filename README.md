Android Framework
======
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


> + 版本信息类 - 获取AndroidManifest.xml中android:versionCode、android:versionName
> + 日志处理类 - 添加日志回调接口，输出日志时自定义其他操作，如：提交日志到服务器
> + 类型转换类 - 避免类型转换失败，闪退
> + 手机信息类 - 获取设备识别码、SIM卡序列号、手机号码等
> + 屏幕信息类 - 宽、高、密度等

### 抽象类包
> + ViewModel基类
> + Activity基类 - Fragment完全取代Activity处理View业务，Activity只处理Fragment业务
> + Fragment基类 - MVVM模式，处理ViewModel

### DB包
> + 数据库表的概要描述 - 包含表名、主键、表的自增字段、字段名、字段默认值等
> + 数据库列的概要描述 - 包含列名、类型、长度、默认值、是否是主键、是否自增、是否允许为空等
> + 数据库操作类 - 封装增删改查
> + 全局数据寄存类 - 使用DB寄存全局数据，Key => Value

### HTTP包
> + HTTP辅助类 - 检查是否联网、检查后台网络是否可用、检查是否开启Wifi等
> + HTTP请求封装
> + 上传|下载类
> + 网络改变监听类

### 文件包
> + SdCard文件读写类
> + 沙盒文件读写类
> + 文件查找类

### 依赖注入
> + ViewModel注解
> + 视图注解

### 常用工具包
> + Gson辅助类 - 处理属性名前缀：m的解析
> + 单位转换
> + 通讯录管理类
> + 图片辅助类 - 通过Drawable获取Bitmap
> + 资源管理类 - 通过资源名称获取资源Id
> + Fragment帮助类

### 视图包
> + 上拉加载更多提示布局
> + RecyclerView Adapter 基类，View包含一个Header和Footer
> + RecyclerView Adapter 基类，用于 DataBinding，View包含一个Header和Footer
> + RecyclerView ItemDecoration 基类 - LinearLayout样式基类 和 GridLayout StaggeredGrid样式基类
> + RecyclerView Expandable ItemDecoration 类 - 用于分组

### Js和Native通信
> 1. 规范Js调用Native的参数和返回值
> 2. 每个Js函数对应一个Java处理类，不把这些Java处理函数放一个类
> 3. 新增一个Js函数，只需要配置下，不需要写JSBridge代码
