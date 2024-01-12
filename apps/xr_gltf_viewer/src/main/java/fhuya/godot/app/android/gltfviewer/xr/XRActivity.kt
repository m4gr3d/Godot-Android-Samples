package fhuya.godot.app.android.gltfviewer.xr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import fhuya.godot.app.android.gltfviewer.common.GLTFContent
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotActivity
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.xr.XRMode

class XRActivity : GodotActivity() {

    internal companion object {
        private val TAG = XRActivity::class.java.simpleName
    }

    private val intentFilter = IntentFilter(GLTFContent.ACTION_GLTF_SELECTED)

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent == null) {
                return
            }

            if (intent.action == GLTFContent.ACTION_GLTF_SELECTED) {
                val selectedGLTF = intent.getStringExtra(GLTFContent.EXTRA_SELECTED_GLTF) ?: return
                Log.d(TAG, "Received 'GLTF SELECTED' broadcast for $selectedGLTF")
                this@XRActivity.appPlugin?.showGLTF(selectedGLTF)
            }
        }

    }

    private var appPlugin: AppPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initAppPluginIfNeeded(godot!!)
        handleDataIntent(intent)

        ContextCompat.registerReceiver(this, broadcastReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED)
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
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
        return setOf(appPlugin!!)
    }

    private fun initAppPluginIfNeeded(godot: Godot) {
        if (appPlugin == null) {
            appPlugin = AppPlugin(godot, true)
        }
    }

    private fun handleDataIntent(intent: Intent) {
        val selectedGLTF = intent.getStringExtra(GLTFContent.EXTRA_SELECTED_GLTF) ?: return
        Log.d(TAG, "Showing gltf item $selectedGLTF")
        appPlugin?.showGLTF(selectedGLTF)
    }

}
