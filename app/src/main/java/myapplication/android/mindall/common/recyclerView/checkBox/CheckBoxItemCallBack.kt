package myapplication.android.mindall.common.recyclerView.checkBox

import androidx.recyclerview.widget.DiffUtil
import myapplication.android.mindall.common.recyclerView.weekDay.WeekDayItemModel

class CheckBoxItemCallBack : DiffUtil.ItemCallback<CheckBoxModel>(){
    override fun areItemsTheSame(oldItem: CheckBoxModel, newItem: CheckBoxModel): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CheckBoxModel, newItem: CheckBoxModel): Boolean = oldItem.compareTo(newItem)
}