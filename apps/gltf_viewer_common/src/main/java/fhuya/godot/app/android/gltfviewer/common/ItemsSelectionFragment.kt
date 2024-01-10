package fhuya.godot.app.android.gltfviewer.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A fragment representing a list of Items.
 */
class ItemsSelectionFragment : Fragment(), GLTFItemRecyclerViewAdapter.Listener {

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"

        const val EXTRA_SELECTED_GLTF = "extra_selected_gltf"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ItemsSelectionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }

    interface SelectionListener {
        fun onItemSelected(item: String)
    }

    private var selectionListener: SelectionListener? = null
    private var columnCount = 1

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val parentActivity = activity
        if (parentActivity is SelectionListener) {
            selectionListener = parentActivity
        }
    }

    override fun onDetach() {
        super.onDetach()
        selectionListener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_items_selection_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = GLTFItemRecyclerViewAdapter(context, GLTFContent.ITEMS,
                    this@ItemsSelectionFragment)

            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        val currentSelection = activity?.intent?.getStringExtra(EXTRA_SELECTED_GLTF)
        val view = view
        if (!currentSelection.isNullOrBlank() && view is RecyclerView) {
            val position = Math.max(GLTFContent.ITEMS.indexOf(currentSelection) -1, 0)
            view.layoutManager?.scrollToPosition(position)
        }
    }

    override fun onItemSelected(item: String) {
        selectionListener?.onItemSelected(item)
    }
}
