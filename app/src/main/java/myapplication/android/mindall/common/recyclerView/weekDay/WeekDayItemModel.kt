package myapplication.android.mindall.common.recyclerView.weekDay

import myapplication.android.mindall.common.listeners.ButtonClickListener
import myapplication.android.mindall.common.recyclerView.small_flag.SmallTaskFlagModel
import myapplication.android.mindall.common.recyclerView.task_flags.TaskFlagModel

data class WeekDayItemModel(
    val id: Int,
    val dayOfWeek: String,
    val date: String,
    val tasks: List<SmallTaskFlagModel>,
    val listener: ButtonClickListener
){
    fun compareTo(item: WeekDayItemModel) : Boolean = this.hashCode() == item.hashCode()
}