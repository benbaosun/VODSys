#include <stdio.h>
#include <stdlib.h>
#include <jni.h>
#include <android/log.h>

#include "DecView.h"

#define LOG_TAG "System.out"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
JNIEXPORT void JNICALL Java_com_xujinliang_decview_DecView_Decorate
  (JNIEnv * env, jobject obj) {
	char* classname = "com/xujinliang/decview/DecView";
	jclass clazz;
	clazz = (*env)->FindClass(env,classname);
	if(clazz==0){
		LOGI("CAN'T FIND CLAZZ ");
	}else{
		LOGI("FIND CLASS");
	}
	jmethodID java_method = (*env)->GetMethodID(env,clazz,"CallFromC","()V");
	if(java_method==0){
		LOGI("CAN'T FIND java_method ");
	}else{
		LOGI("FIND java_method");
	}
	(*env)->CallVoidMethod(env,obj,java_method);
}
