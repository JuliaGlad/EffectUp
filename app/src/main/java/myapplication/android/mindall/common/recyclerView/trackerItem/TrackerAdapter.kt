package myapplication.android.mindall.common.recyclerView.trackerItem

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.databinding.RecyclerViewQualityChooseBinding
import myapplication.android.mindall.databinding.RecyclerViewTimeItemBinding
import myapplication.android.mindall.databinding.RecyclerViewTrackerItemBinding

class TrackerAdapter : ListAdapter<TrackerModel, RecyclerView.ViewHolder>(TrackerItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            RecyclerViewQualityChooseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(val binding: RecyclerViewQualityChooseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TrackerModel) {
            binding.textValue.text = model.value
            binding.iconValue.setImageDrawable(
                ResourcesCompat.getDrawable(
                    itemView.resources,
                    model.valueIcon,
                    itemView.context.theme
                )
            )

            if (model.isChosen) {
                binding.item.strokeColor = ResourcesCompat.getColor(itemView.resources, R.color.md_theme_primary, itemView.context.theme)
            } else {
                binding.item.strokeColor = Color.TRANSPARENT
            }

            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }
    }
}