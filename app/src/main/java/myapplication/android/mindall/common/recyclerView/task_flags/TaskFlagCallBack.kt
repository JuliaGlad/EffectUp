package myapplication.android.mindall.common.recyclerView.task_flags

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class TaskFlagCallBack : DiffUtil.ItemCallback<TaskFlagModel>() {
    override fun areItemsTheSame(
        oldItem: TaskFlagModel, newItem: TaskFlagModel
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: TaskFlagModel,
        newItem: TaskFlagModel
    ) = oldItem.compareTo(newItem)
}