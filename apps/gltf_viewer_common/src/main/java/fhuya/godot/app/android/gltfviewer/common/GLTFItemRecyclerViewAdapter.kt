package fhuya.godot.app.android.gltfviewer.common

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import fhuya.godot.app.android.gltfviewer.common.GLTFContent.GLTFItem
import fhuya.godot.app.android.gltfviewer.common.databinding.FragmentItemsSelectionBinding

/**
 * [RecyclerView.Adapter] that can display a [GLTFItem].
 */
class GLTFItemRecyclerViewAdapter(
    private val context: Context,
    private val values: List<GLTFItem>,
    private val listener: Listener
) : RecyclerView.Adapter<GLTFItemRecyclerViewAdapter.ViewHolder>() {

    interface Listener {
        /**
         * Used to notify when a [GLTFItem] has been selected.
         */
        fun onItemSelected(item: GLTFItem)
    }

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
        val item = values[position]
        holder.screenshotView.setImageResource(item.getScreenshotDrawableId(context))
        holder.contentView.text = item.name
        holder.itemView.setOnClickListener {
            listener.onItemSelected(item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentItemsSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val screenshotView: ImageView = binding.contentScreenshot
        val contentView: TextView = binding.contentName

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}
