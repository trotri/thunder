package com.trotri.android.library.ndk;

/**
 * Thunder class file
 * Ndk类，调用Thunder.cpp中的exec()
 *
 * @author 宋欢 <trotri@yeah.net>
 * @version $Id: Thunder.java 1 2016-10-12 10:00:06Z huan.song $
 * @since 1.0
 */
public class Thunder {

    public static final String TAG = "Thunder";

    static {
        System.loadLibrary("thunder");
    }

    /**
     * Thunder.cpp
     * JNIEXPORT void JNICALL Java_com_trotri_android_library_ndk_Thunder_exec(JNIEnv * env, jobject obj)
     */
    public static native void exec();

}
