LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := libthunder
LOCAL_CFLAGS    := -Werror
LOCAL_SRC_FILES := Thunder.cpp
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
