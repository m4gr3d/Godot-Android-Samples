package fhuya.godot.app.android.gltfviewer.xr

import android.content.Intent
import fhuya.godot.app.android.gltfviewer.common.GLTFContent
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import org.godotengine.godot.plugin.UsedByGodot

/**
 * Runtime [GodotPlugin] used to enable interaction with the Godot gdscript logic.
 */
class AppPlugin(godot: Godot, private val xrEnabled: Boolean) : GodotPlugin(godot) {

    companion object {
        val SHOW_GLTF_SIGNAL = SignalInfo("show_gltf", String::class.java)
    }

    internal var currentGLTFSelection = "turkey"
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
    internal fun showGLTF(selectedGLTF: String) {
        if (mainLoopStarted && resumed && selectedGLTF != currentGLTFSelection) {
            emitSignal(SHOW_GLTF_SIGNAL.name, GLTFContent.getGLTFFilepath(selectedGLTF))
            currentGLTFSelection = selectedGLTF
        } else {
            pendingGLTF = selectedGLTF
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

    @UsedByGodot
    private fun showItemsSelection() {
        runOnUiThread {
            val selectItemsIntent = Intent(activity, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(GLTFContent.EXTRA_SELECTED_GLTF, currentGLTFSelection)
            }
            activity?.startActivity(selectItemsIntent)
        }
    }

    @UsedByGodot
    private fun isXREnabled() = xrEnabled

    @UsedByGodot
    private fun getCurrentGLTFSelection() = currentGLTFSelection
}