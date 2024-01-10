package fhuya.godot.app.android.gltfviewer.xr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import fhuya.godot.app.android.gltfviewer.common.GLTFContent
import fhuya.godot.app.android.gltfviewer.common.ItemsSelectionFragment

class MainActivity : AppCompatActivity(), ItemsSelectionFragment.SelectionListener {

    internal companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var itemsSelectionFragment = supportFragmentManager.findFragmentById(R.id.item_selection_fragment_container)
        if (itemsSelectionFragment !is ItemsSelectionFragment) {
            itemsSelectionFragment = ItemsSelectionFragment.newInstance(1)
            supportFragmentManager.beginTransaction()
                .replace(R.id.item_selection_fragment_container, itemsSelectionFragment)
                .commitAllowingStateLoss()
        }
    }

    override fun onItemSelected(item: GLTFContent.GLTFItem) {
        // Fire an intent to start the XRActivity with the gltf file path payload
        Log.d(TAG, "Starting XRActivity with selected gltf: ${item.glbFilepath}")
        val xrIntent = Intent(this, XRActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(XRActivity.EXTRA_GLTF_FILE_PATH, item.glbFilepath)
        }
        startActivity(xrIntent)
        finishAndRemoveTask()
    }
}