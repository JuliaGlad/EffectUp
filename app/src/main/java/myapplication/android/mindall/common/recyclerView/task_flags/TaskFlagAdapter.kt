package myapplication.android.mindall.common.recyclerView.task_flags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.databinding.RecyclerViewTaskFlagLayoutBinding

class TaskFlagAdapter : ListAdapter<TaskFlagModel, RecyclerView.ViewHolder>(TaskFlagCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(RecyclerViewTaskFlagLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = RecyclerViewTaskFlagLayoutBinding.bind(itemView)

        fun bind(model: TaskFlagModel){
            binding.flag.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, model.flag, itemView.context.theme))
            binding.taskCount.text = model.tasksCount
        }
    }
}