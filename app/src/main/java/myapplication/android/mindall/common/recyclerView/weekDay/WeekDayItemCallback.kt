package myapplication.android.mindall.common.recyclerView.weekDay

import androidx.recyclerview.widget.DiffUtil

class WeekDayItemCallback : DiffUtil.ItemCallback<WeekDayItemModel>() {
    override fun areItemsTheSame(oldItem: WeekDayItemModel, newItem: WeekDayItemModel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: WeekDayItemModel, newItem: WeekDayItemModel): Boolean = oldItem.compareTo(newItem)
}