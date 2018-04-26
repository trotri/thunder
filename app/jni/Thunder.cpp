/**
 * Thunder file
 * Ndk测试
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Thunder.cpp 1 2018-04-12 10:00:06Z huan.song $
 * @since 1.0
 */

#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define LOG_TAG    "libthunder"
#define LOG_I(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOG_E(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

void exec() {
    LOG_I("exec() ====== ");
}

extern "C" {
    JNIEXPORT void JNICALL Java_com_trotri_android_library_ndk_Thunder_exec(JNIEnv * env, jobject obj);
};

JNIEXPORT void JNICALL Java_com_trotri_android_library_ndk_Thunder_exec(JNIEnv * env, jobject obj) {
    exec();
}
