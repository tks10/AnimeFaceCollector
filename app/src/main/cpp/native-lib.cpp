#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_net_subroh0508_animefacecollector_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Fooooooooooooo!";
    return env->NewStringUTF(hello.c_str());
}
