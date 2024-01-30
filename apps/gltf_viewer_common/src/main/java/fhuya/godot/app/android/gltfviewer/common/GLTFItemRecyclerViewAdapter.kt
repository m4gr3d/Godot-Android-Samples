package fhuya.godot.app.android.gltfviewer.common

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import fhuya.godot.app.android.gltfviewer.common.databinding.FragmentItemsSelectionBinding

/**
 * [RecyclerView.Adapter] that can display a [GLTFItem].
 */
class GLTFItemRecyclerViewAdapter(
    private val context: Context,
    private val values: List<String>,
    private val listener: Listener
) : RecyclerView.Adapter<GLTFItemRecyclerViewAdapter.ViewHolder>(), Filterable {

    interface Listener {
        /**
         * Used to notify when a [GLTFItem] has been selected.
         */
        fun onItemSelected(item: String)

        fun onShowItemInVr(item: String) {}
    }

    private var filteredValues = values

    // Check for hybrid capabilities availability
    private val isHybridApp = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && context.packageManager.hasSystemFeature("oculus.software.vr.app.hybrid", 1)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentItemsSelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredValues[position]
        holder.screenshotView.setImageResource(GLTFContent.getScreenshotDrawableId(context, item))
        holder.contentView.text = item
        holder.itemView.setOnClickListener {
            listener.onItemSelected(item)
        }

        if (isHybridApp) {
            holder.showInVrButton.visibility = View.VISIBLE
            holder.showInVrButton.setOnClickListener {
                listener.onShowItemInVr(item)
            }
        }
    }

    override fun getItemCount(): Int = filteredValues.size

    inner class ViewHolder(binding: FragmentItemsSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val screenshotView: ImageView = binding.contentScreenshot
        val contentView: TextView = binding.contentName
        val showInVrButton: ImageView = binding.showInVrButton

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                results.values = getFilteredResults(constraint.toString().lowercase())
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredValues = if (results == null) {
                    emptyList()
                } else {
                    results.values as List<String>
                }
                notifyDataSetChanged()
            }

            private fun getFilteredResults(constraint: String): List<String> {
                return if (constraint.isBlank()) {
                    values
                } else {
                    values.filter { it.lowercase().contains(constraint) }
                }
            }
        }
    }

}
