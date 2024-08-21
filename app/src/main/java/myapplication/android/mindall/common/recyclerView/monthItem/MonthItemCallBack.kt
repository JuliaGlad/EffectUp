package myapplication.android.mindall.common.recyclerView.monthItem

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Recycler

class MonthItemCallBack : DiffUtil.ItemCallback<MonthItemModel>() {
    override fun areItemsTheSame(oldItem: MonthItemModel, newItem: MonthItemModel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: MonthItemModel, newItem: MonthItemModel): Boolean = oldItem.compareTo(newItem)
}