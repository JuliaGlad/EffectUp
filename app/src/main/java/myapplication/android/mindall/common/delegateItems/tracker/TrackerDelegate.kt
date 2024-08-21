package myapplication.android.mindall.common.delegateItems.tracker

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.common.Constants.Companion.GOOD
import myapplication.android.mindall.common.Constants.Companion.GREAT
import myapplication.android.mindall.common.Constants.Companion.NOT_BAD
import myapplication.android.mindall.common.Constants.Companion.VERY_BAD
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewTrackerItemBinding

class TrackerDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            RecyclerViewTrackerItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as TrackerModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is TrackerDelegateItem

    class ViewHolder(val binding: RecyclerViewTrackerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: TrackerModel) {
            binding.dateTitle.text = model.date
            binding.valueTitle.text = model.value

            when (model.value) {
                GREAT -> binding.iconValue.setImageDrawable(getResourceCompatDrawable(R.drawable.ic_very_good))
                GOOD -> binding.iconValue.setImageDrawable(getResourceCompatDrawable(R.drawable.ic_good))
                NOT_BAD -> binding.iconValue.setImageDrawable(getResourceCompatDrawable(R.drawable.ic_neutral))
                VERY_BAD -> binding.iconValue.setImageDrawable(getResourceCompatDrawable(R.drawable.ic_bad))
            }

            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }

        private fun getResourceCompatDrawable(icon: Int): Drawable? {
            return ResourcesCompat.getDrawable(itemView.resources, icon, itemView.context.theme)
        }
    }
}
