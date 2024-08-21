package myapplication.android.mindall.common.delegateItems.daySchedule

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewScheduleItemBinding

class DayScheduleDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(RecyclerViewScheduleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as DayScheduleModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is DayScheduleDelegateItem

    class ViewHolder(val binding: RecyclerViewScheduleItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(model: DayScheduleModel){
            binding.titleDuration.text = model.duration
            binding.titleTask.text = model.title
            if (model.isNotificationOn == true){
                binding.icon.setImageDrawable(getResourceCompatDrawable(R.drawable.ic_notifications_active))
            } else {
                binding.icon.setImageDrawable(getResourceCompatDrawable(R.drawable.ic_notifications_off))
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
