package fhuya.godot.app.android.gltfviewer.xr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import fhuya.godot.app.android.gltfviewer.common.ItemsSelectionFragment
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotActivity
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.xr.XRMode

class XRActivity : GodotActivity() {

    internal companion object {
        private val TAG = XRActivity::class.java.simpleName
    }

    private var xrAppPlugin: XrAppPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppPluginIfNeeded(godot!!)
        handleDataIntent(intent)
    }

    override fun onNewIntent(newIntent: Intent) {
        super.onNewIntent(newIntent)
        handleDataIntent(newIntent)
    }

    override fun getCommandLine() : List<String> {
        val commandLines = mutableListOf<String>()
        commandLines.addAll(super.getCommandLine())

        commandLines.add(XRMode.OPENXR.cmdLineArg)
        commandLines.add("--xr-mode")
        commandLines.add("on")
        return commandLines
    }

    override fun getHostPlugins(godot: Godot): Set<GodotPlugin> {
        initAppPluginIfNeeded(godot)
        return setOf(xrAppPlugin!!)
    }

    private fun initAppPluginIfNeeded(godot: Godot) {
        if (xrAppPlugin == null) {
            xrAppPlugin = XrAppPlugin(godot)
        }
    }

    private fun handleDataIntent(intent: Intent) {
        val selectedGLTF = intent.getStringExtra(ItemsSelectionFragment.EXTRA_SELECTED_GLTF) ?: return
        Log.d(TAG, "Showing gltf item $selectedGLTF")
        xrAppPlugin?.showGLTF(selectedGLTF)
    }

}
