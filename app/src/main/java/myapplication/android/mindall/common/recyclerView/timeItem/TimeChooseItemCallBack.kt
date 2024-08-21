package myapplication.android.mindall.common.recyclerView.timeItem

import androidx.recyclerview.widget.DiffUtil

class TimeChooseItemCallBack : DiffUtil.ItemCallback<TimeChooseModel>() {
    override fun areItemsTheSame(oldItem: TimeChooseModel, newItem: TimeChooseModel) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TimeChooseModel, newItem: TimeChooseModel) = oldItem.compareTo(newItem)
}