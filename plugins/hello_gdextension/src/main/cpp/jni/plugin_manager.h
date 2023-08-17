#ifndef PLUGIN_MANAGER_H
#define PLUGIN_MANAGER_H

#include <godot_cpp/variant/string.hpp>

#include "../gdexample.h"

namespace godot {

class PluginManager {
public:
    static PluginManager *get_singleton();

    void add_gdexample_node(const String &p_parent_node_path);

    void toggle_visibility(bool p_visible);

private:
    PluginManager();

    ~PluginManager();

    static Node *get_node(const String &p_node_path);

    static PluginManager *singleton;

    GDExample *gdexample_node = nullptr;
};

}

#endif // PLUGIN_MANAGER_H
