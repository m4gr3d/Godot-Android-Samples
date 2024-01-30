package fhuya.godot.app.android.gltfviewer.pancake

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fhuya.godot.app.android.gltfviewer.common.GLTFContent
import fhuya.godot.app.android.gltfviewer.common.GLTFItemRecyclerViewAdapter
import fhuya.godot.app.android.gltfviewer.common.ItemsSelectionFragment
import org.godotengine.godot.Godot
import org.godotengine.godot.GodotFragment
import org.godotengine.godot.GodotHost
import org.godotengine.godot.plugin.GodotPlugin

/**
 * Implements the [GodotHost] interface so it can access functionality from the [Godot] instance.
 */
class MainActivity: AppCompatActivity(), GodotHost, GLTFItemRecyclerViewAdapter.Listener {

    private var godotFragment: GodotFragment? = null

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

        var itemsSelectionFragment = supportFragmentManager.findFragmentById(R.id.item_selection_fragment_container)
        if (itemsSelectionFragment !is ItemsSelectionFragment) {
            itemsSelectionFragment = ItemsSelectionFragment.newInstance(1)
            supportFragmentManager.beginTransaction()
                .replace(R.id.item_selection_fragment_container, itemsSelectionFragment)
                .commitAllowingStateLoss()
        }
    }

    private fun initAppPluginIfNeeded(godot: Godot) {
        if (appPlugin == null) {
            appPlugin = AppPlugin(godot)
        }
    }

    override fun getActivity() = this

    override fun getGodot() = godotFragment?.godot

    override fun getHostPlugins(godot: Godot): Set<GodotPlugin> {
        initAppPluginIfNeeded(godot)

        return setOf(appPlugin!!)
    }

    override fun onItemSelected(item: String) {
        appPlugin?.showGLTF(GLTFContent.getGLTFFilepath(item))
    }
}
