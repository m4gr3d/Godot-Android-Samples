package fhuya.godot.app.android.gltfviewer.xr

import android.annotation.SuppressLint
import android.content.Intent
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

/**
 * Runtime [GodotPlugin] used to enable interaction with the Godot gdscript logic.
 */
class XrAppPlugin(godot: Godot) : GodotPlugin(godot) {

    companion object {
        val SHOW_GLTF_SIGNAL = SignalInfo("show_gltf", String::class.java)
    }

    private var mainLoopStarted = false
    private var resumed = false
    private var pendingGLTF = ""

    override fun getPluginName() = "XrAppPlugin"

    override fun getPluginSignals() = setOf(SHOW_GLTF_SIGNAL)

    /**
     * Used to emit a signal to the gdscript logic to update the gltf being shown.
     *
     * @param glbFilepath Filepath of the gltf asset to be shown
     */
    internal fun showGLTF(glbFilepath: String) {
        if (mainLoopStarted && resumed) {
            emitSignal(SHOW_GLTF_SIGNAL.name, glbFilepath)
        } else {
            pendingGLTF = glbFilepath
        }
    }

    override fun onGodotMainLoopStarted() {
        mainLoopStarted = true
        dispatchPendingGLTFIfNeeded()
    }

    override fun onMainResume() {
        super.onMainResume()
        resumed = true
        dispatchPendingGLTFIfNeeded()
    }

    override fun onMainPause() {
        super.onMainPause()
        resumed = false
    }

    private fun dispatchPendingGLTFIfNeeded() {
        if (mainLoopStarted && resumed && pendingGLTF.isNotBlank()) {
            showGLTF(pendingGLTF)
            pendingGLTF = ""
        }
    }

    @SuppressLint("WrongConstant")
    @UsedByGodot
    private fun showItemsSelection() {
        runOnUiThread {
            val selectItemsIntent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(XRActivity.FLAG_ACTIVITY_CROSS_DISPLAY_RESULT)
            }
            activity?.startActivityForResult(selectItemsIntent, XRActivity.REQUEST_CODE)
        }
    }
}