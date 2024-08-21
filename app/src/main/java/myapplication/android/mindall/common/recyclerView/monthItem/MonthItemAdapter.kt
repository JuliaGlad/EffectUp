package myapplication.android.mindall.common.recyclerView.monthItem

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.RecyclerViewMonthItemBinding

class MonthItemAdapter : ListAdapter<MonthItemModel, RecyclerView.ViewHolder>(MonthItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = ViewHolder(RecyclerViewMonthItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(itemView: RecyclerViewMonthItemBinding) : RecyclerView.ViewHolder(itemView.root){
        private val binding = RecyclerViewMonthItemBinding.bind(itemView.root)

        fun bind(model: MonthItemModel){
            binding.monthTitle.text = model.month
            binding.durationTitle.text = model.duration

            if (model.isChosen){
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