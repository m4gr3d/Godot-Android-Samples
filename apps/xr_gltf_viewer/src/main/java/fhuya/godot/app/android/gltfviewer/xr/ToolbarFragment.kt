package fhuya.godot.app.android.gltfviewer.xr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment

/**
 * Implementation for the app's bottom toolbar
 */
class ToolbarFragment: Fragment() {

    companion object {
        private val TAG = ToolbarFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_xr_toolbar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchView = view.findViewById<SearchView>(R.id.filter_items)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val parentActivity = activity
                val adapterFiler = if (parentActivity is MainActivity) parentActivity.getItemsFilter() else null
                adapterFiler?.filter?.filter(newText)
                return true
            }
        })
    }
}