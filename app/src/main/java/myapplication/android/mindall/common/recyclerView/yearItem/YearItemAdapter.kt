package myapplication.android.mindall.common.recyclerView.yearItem

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.RecyclerViewYearBinding

class YearItemAdapter : ListAdapter<YearItemModel, RecyclerView.ViewHolder>(YearItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            RecyclerViewYearBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(val binding: RecyclerViewYearBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: YearItemModel) {
            binding.titleYear.text = model.year

            if (model.isChosen) {
                binding.item.strokeColor = getResourcesCompatColor(R.color.md_theme_primary)
            } else {
                binding.item.strokeColor = Color.TRANSPARENT
            }

            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }

        private fun getResourcesCompatColor(color: Int): Int {
            return ResourcesCompat.getColor(itemView.resources, color, itemView.context.theme)
        }
    }
}