# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-verbose       #打印混淆信息
-dontshrink    #不删除无用的类
-dontoptimize  #优化
-dontpreverify #预校验
-optimizationpasses 5 #压缩比例
-dontusemixedcaseclassnames #大小写混合
-dontskipnonpubliclibraryclasses #第三方Jar
-dontskipnonpubliclibraryclassmembers #不跳过非公共的库的类成员
-keepattributes *Annotation* #保留注解，@Keep等注解有效
-keepattributes InnerClasses,EnclosingMethod,Exceptions,Deprecated #Dex、匿名内部类
-keepattributes Signature #泛型
-keepattributes SourceFile,LineNumberTable #保留行号

-dontwarn android.support.annotation.Keep

-keep @android.support.annotation.Keep class **
