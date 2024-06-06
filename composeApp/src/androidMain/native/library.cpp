#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL Java_de_cacheoverflow_cashflow_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject activity) {
    return env->NewStringUTF(std::string("This is C++").c_str());
}