package fhuyakou.godot.plugin.android.hellogdextension

import android.util.Log
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot

/**
 * Entry point for the 'HelloGDExtension' Android plugin.
 *
 * This plugin provides a couple of methods to add and manipulate a GDExample node.
 * The GDExample node is implemented and integrated within Godot using GDExtension.
 */
class HelloGDExtensionPlugin(godot: Godot) : GodotPlugin(godot) {

    companion object {
        val TAG = HelloGDExtensionPlugin::class.java.simpleName

        init {
            try {
                Log.v(TAG, "Loading hello_gdextension library")
                System.loadLibrary("hello_gdextension")
            } catch (e: UnsatisfiedLinkError) {
                Log.e(TAG, "Unable to load the hello_gdextension shared library")
            }
        }
    }

    @UsedByGodot
    private fun addGDExampleNode(parentNodePath: String) {
        Log.i(TAG, "Adding GDExample node to $parentNodePath")
        nativeAddGDExampleNode(parentNodePath)
    }

    @UsedByGodot
    private fun setGDExampleVisible(visible: Boolean) {
        Log.i(TAG, "Updating GDExample node visibility to $visible")
        nativeToggleVisibility(visible)
    }

    override fun getPluginName() = "HelloGDExtension"

    override fun getPluginGDExtensionModulesPaths() = setOf("res://addons/hello_gdextension_plugin/hello_gdextension.gdextension")

    private external fun nativeAddGDExampleNode(parentNodePath: String)

    private external fun nativeToggleVisibility(visible: Boolean)
}
