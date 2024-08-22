package myapplication.android.mindall.common.delegateItems.habitTrackerDaily

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewHabitDailyItemBinding
import myapplication.android.mindall.databinding.RecyclerViewHabitItemBinding

class HabitTrackerDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
       = ViewHolder(RecyclerViewHabitDailyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind((item.content() as HabitTrackerModel))
    }

    override fun isOfViewType(item: DelegateItem) = item is HabitTrackerDelegateItem

    class ViewHolder(val binding: RecyclerViewHabitDailyItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: HabitTrackerModel){
            binding.dateTitle.text = model.date
            checkComplete(model.isComplete)
            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }

        private fun checkComplete(isComplete: Boolean) {
            if(isComplete){
                binding.valueTitle.text = itemView.resources.getString(R.string.complete)
                binding.iconValue.setImageDrawable(getResourcesCompatDrawable(R.drawable.ic_complete))
            } else {
                binding.valueTitle.text = itemView.resources.getString(R.string.incomplete)
                binding.iconValue.setImageDrawable(getResourcesCompatDrawable(R.drawable.ic_incomplete))
            }
        }

        private fun getResourcesCompatDrawable(id: Int): Drawable? {
            return ResourcesCompat.getDrawable(itemView.resources, id, itemView.context.theme)
        }
    }
}