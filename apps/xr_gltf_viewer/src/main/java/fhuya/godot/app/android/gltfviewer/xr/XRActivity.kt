package fhuya.godot.app.android.gltfviewer.xr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotActivity
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.xr.XRMode

class XRActivity : GodotActivity() {

    internal companion object {
        private val TAG = XRActivity::class.java.simpleName

        const val REQUEST_CODE = 777
        const val EXTRA_GLTF_FILE_PATH = "extra_gltf_file_path"

        const val FLAG_ACTIVITY_CROSS_DISPLAY_RESULT = 0x00000100
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
        val glbFilePath = intent.getStringExtra(EXTRA_GLTF_FILE_PATH) ?: return
        Log.d(TAG, "Showing gltf item with path $glbFilePath")
        xrAppPlugin?.showGLTF(glbFilePath)
    }

}
