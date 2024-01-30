package fhuya.godot.app.android.gltfviewer.xr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import fhuya.godot.app.android.gltfviewer.common.GLTFContent
import fhuya.godot.app.android.gltfviewer.common.GLTFItemRecyclerViewAdapter
import fhuya.godot.app.android.gltfviewer.common.ItemsSelectionFragment
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotFragment
import org.godotengine.godot.GodotHost
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.utils.ProcessPhoenix
import org.godotengine.godot.xr.XRMode

class MainActivity : AppCompatActivity(), GodotHost, GLTFItemRecyclerViewAdapter.Listener {

    internal companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var godotFragment: GodotFragment? = null
    private var itemsSelectionFragment: ItemsSelectionFragment? = null

    private var appPlugin: AppPlugin? = null

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

        var itemsFragment = supportFragmentManager.findFragmentById(R.id.item_selection_fragment_container)
        if (itemsFragment !is ItemsSelectionFragment) {
            itemsFragment = ItemsSelectionFragment.newInstance(1)
            supportFragmentManager.beginTransaction()
                .replace(R.id.item_selection_fragment_container, itemsFragment)
                .commitAllowingStateLoss()
        }
        itemsSelectionFragment = itemsFragment

        val toolbarContainerView = findViewById<View>(R.id.xr_toolbar_container)
        toolbarContainerView.visibility = View.VISIBLE

        var xrToolbarFragment = supportFragmentManager.findFragmentById(R.id.xr_toolbar_container)
        if (xrToolbarFragment !is ToolbarFragment) {
            xrToolbarFragment = ToolbarFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.xr_toolbar_container, xrToolbarFragment)
                .commitAllowingStateLoss()
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

    override fun onShowItemInVr(item: String) {
        if (item.isBlank()) {
            return
        }

        // Fire an intent to start the XRActivity with the selected gltf
        Log.d(TAG, "Starting XRActivity with selected gltf: $item")
        val xrIntent = Intent(this, XRActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(GLTFContent.EXTRA_SELECTED_GLTF, item)
        }
        startActivity(xrIntent)
        finishAndRemoveTask()
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

    internal fun getItemsFilter() = itemsSelectionFragment?.getAdapterFilter()
}