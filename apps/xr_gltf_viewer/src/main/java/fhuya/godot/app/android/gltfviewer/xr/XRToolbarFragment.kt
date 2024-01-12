package fhuya.godot.app.android.gltfviewer.xr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fhuya.godot.app.android.gltfviewer.common.GLTFContent

/**
 * Provides the UI used to transition to XR
 */
class XRToolbarFragment: Fragment() {

    companion object {
        private val TAG = XRToolbarFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_xr_toolbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val openInXRButton = view.findViewById<View>(R.id.open_in_xr_button)
        openInXRButton.setOnClickListener {
            val parentActivity = activity
            if (parentActivity is MainActivity) {
                openInXR(parentActivity.appPlugin?.currentGLTFSelection)
            }
        }
    }

    private fun openInXR(selectedGLTF: String?) {
        if (selectedGLTF.isNullOrBlank()) {
            return
        }

        // Fire an intent to start the XRActivity with the selected gltf
        Log.d(TAG, "Starting XRActivity with selected gltf: $selectedGLTF")
        val xrIntent = Intent(activity, XRActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(GLTFContent.EXTRA_SELECTED_GLTF, selectedGLTF)
        }
        startActivity(xrIntent)
        activity?.finishAndRemoveTask()
    }
}