package myapplication.android.mindall.common.delegateItems.goalItem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewGoalItemBinding

class GoalDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
       = ViewHolder(RecyclerViewGoalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind((item.content() as GoalModel))
    }

    override fun isOfViewType(item: DelegateItem) = item is GoalDelegateItem

    class ViewHolder(val binding: RecyclerViewGoalItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: GoalModel){
            binding.goal.text = model.goal
        }
    }
}