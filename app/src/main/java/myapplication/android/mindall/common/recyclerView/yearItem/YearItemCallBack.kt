package myapplication.android.mindall.common.recyclerView.yearItem

import androidx.recyclerview.widget.DiffUtil

class YearItemCallBack : DiffUtil.ItemCallback<YearItemModel>() {
    override fun areItemsTheSame(oldItem: YearItemModel, newItem: YearItemModel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: YearItemModel, newItem: YearItemModel): Boolean = oldItem.compareTo(newItem)
}