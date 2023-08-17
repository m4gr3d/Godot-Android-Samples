#include <jni.h>

#include "plugin_manager.h"
#include "utils.h"

#undef JNI_PACKAGE_NAME
#define JNI_PACKAGE_NAME fhuyakou_godot_plugin_android_hellogdextension

#undef JNI_CLASS_NAME
#define JNI_CLASS_NAME HelloGDExtensionPlugin

extern "C" {
    JNIEXPORT void JNICALL JNI_METHOD(nativeAddGDExampleNode)(JNIEnv *env, jobject, jstring p_parent_node_path) {
        godot::PluginManager::get_singleton()->add_gdexample_node(jstring_to_string(env, p_parent_node_path));
    }

    JNIEXPORT void JNICALL JNI_METHOD(nativeToggleVisibility)(JNIEnv *env, jobject, jboolean p_visible) {
        godot::PluginManager::get_singleton()->toggle_visibility(p_visible);
    }
};
