package myapplication.android.mindall.common.recyclerView.small_flag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.databinding.RecyclerViewSmallFlagBinding
import myapplication.android.mindall.databinding.RecyclerViewTaskFlagLayoutBinding

class SmallTaskFlagAdapter : ListAdapter<SmallTaskFlagModel, RecyclerView.ViewHolder>(SmallTaskFlagCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ViewHolder(RecyclerViewSmallFlagBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = RecyclerViewSmallFlagBinding.bind(itemView)

        fun bind(model: SmallTaskFlagModel){
            binding.flag.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, model.flag, itemView.context.theme))
            binding.taskCount.text = model.tasksCount
        }
    }
}