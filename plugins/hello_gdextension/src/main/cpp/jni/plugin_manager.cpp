#include "plugin_manager.h"

#include <godot_cpp/classes/engine.hpp>
#include <godot_cpp/classes/main_loop.hpp>
#include <godot_cpp/classes/resource.hpp>
#include <godot_cpp/classes/resource_loader.hpp>
#include <godot_cpp/classes/scene_tree.hpp>
#include <godot_cpp/classes/window.hpp>

#include "utils.h"

namespace godot {

PluginManager *PluginManager::singleton = nullptr;

PluginManager::PluginManager() {
    gdexample_node = memnew(GDExample);

    Ref<Resource> resource_ref = ResourceLoader::get_singleton()->load("res://addons/hello_gdextension_plugin/interface/android_icon.svg");
    gdexample_node->set_texture(resource_ref);
}

PluginManager::~PluginManager() {
    memdelete(gdexample_node);
}

PluginManager *PluginManager::get_singleton() {
    if (singleton == nullptr) {
        singleton = new PluginManager();
    }
    return singleton;
}

void PluginManager::toggle_visibility(bool p_visible) {
    ALOG_ASSERT(gdexample_node != nullptr, "Uninitialized gdexample node");

    gdexample_node->set_visible(p_visible);
}

void PluginManager::add_gdexample_node(const String &p_parent_node_path) {
    ALOG_ASSERT(gdexample_node != nullptr, "Uninitialized gdexample node");

    if (p_parent_node_path.is_empty()) {
        ALOGW("Empty parent node path, aborting...");
        return;
    }

    // Retrieve the parent node.
    Node *parent_node = get_node(p_parent_node_path);
    if (!parent_node) {
        ALOGE("Unable to retrieve parent node with path %s", p_parent_node_path.utf8().get_data());
        return;
    }

    if (gdexample_node->get_parent() != nullptr) {
        gdexample_node->get_parent()->remove_child(gdexample_node);
    }

    parent_node->add_child(gdexample_node);
    gdexample_node->set_owner(parent_node);
    gdexample_node->set_centered(false);
}

Node *PluginManager::get_node(const String &p_node_path) {
    if (p_node_path.is_empty()) {
        ALOGW("Empty node path argument.");
        return nullptr;
    }

    MainLoop *main_loop = Engine::get_singleton()->get_main_loop();
    auto *scene_tree = Object::cast_to<SceneTree>(main_loop);
    if (!scene_tree) {
        ALOGW("Unable to retrieve the scene tree.");
        return nullptr;
    }

    const Window *window = scene_tree->get_root();
    NodePath node_path(p_node_path);
    Node *node = window->get_node_or_null(node_path);
    if (!node) {
        // Try again by treating the parameter as the node's name
        node = window->find_child(p_node_path, true, false);
    }

    return node;
}
}
