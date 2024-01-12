package fhuya.godot.app.android.gltfviewer.xr

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import fhuya.godot.app.android.gltfviewer.common.GLTFContent
import fhuya.godot.app.android.gltfviewer.common.ItemsSelectionFragment
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotFragment
import org.godotengine.godot.GodotHost
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.utils.ProcessPhoenix
import org.godotengine.godot.xr.XRMode

class MainActivity : AppCompatActivity(), GodotHost, ItemsSelectionFragment.SelectionListener {

    internal companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var godotFragment: GodotFragment? = null

    internal var appPlugin: AppPlugin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentGodotFragment = supportFragmentManager.findFragmentById(R.id.godot_fragment_container)
        if (currentGodotFragment is GodotFragment) {
            godotFragment = currentGodotFragment
        } else {
            godotFragment = GodotFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.godot_fragment_container, godotFragment!!)
                .commitNowAllowingStateLoss()
        }

        initAppPluginIfNeeded(godot!!)

        var itemsSelectionFragment = supportFragmentManager.findFragmentById(R.id.item_selection_fragment_container)
        if (itemsSelectionFragment !is ItemsSelectionFragment) {
            itemsSelectionFragment = ItemsSelectionFragment.newInstance(1)
            supportFragmentManager.beginTransaction()
                .replace(R.id.item_selection_fragment_container, itemsSelectionFragment)
                .commitAllowingStateLoss()
        }

        // Check for hybrid capabilities availability
        val toolbarContainerView = findViewById<View>(R.id.xr_toolbar_container)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && packageManager.hasSystemFeature("oculus.software.vr.app.hybrid", 1)) {
            Log.d(TAG, "Enabling XR toolbar")
            toolbarContainerView.visibility = View.VISIBLE

            var xrToolbarFragment = supportFragmentManager.findFragmentById(R.id.xr_toolbar_container)
            if (xrToolbarFragment !is XRToolbarFragment) {
                xrToolbarFragment = XRToolbarFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.xr_toolbar_container, xrToolbarFragment)
                    .commitAllowingStateLoss()
            }
        } else {
            toolbarContainerView.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ProcessPhoenix.forceQuit(this)
    }

    override fun getCommandLine() : List<String> {
        val commandLines = mutableListOf<String>()
        commandLines.addAll(super.getCommandLine())

        commandLines.add(XRMode.REGULAR.cmdLineArg)
        commandLines.add("--xr-mode")
        commandLines.add("off")
        return commandLines
    }

    override fun onItemSelected(item: String) {
        appPlugin?.showGLTF(item)
        sendBroadcast(Intent(GLTFContent.ACTION_GLTF_SELECTED).apply {
            putExtra(GLTFContent.EXTRA_SELECTED_GLTF, item)
        })
    }

    private fun initAppPluginIfNeeded(godot: Godot) {
        if (appPlugin == null) {
            appPlugin = AppPlugin(godot, false)
        }
    }

    override fun getActivity() = this

    override fun getGodot() = godotFragment?.godot

    override fun getHostPlugins(godot: Godot): Set<GodotPlugin> {
        initAppPluginIfNeeded(godot)

        return setOf(appPlugin!!)
    }
}