package myapplication.android.mindall.common.delegateItems.habit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewGoalItemBinding
import myapplication.android.mindall.databinding.RecyclerViewHabitItemBinding

class HabitDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
       = ViewHolder(RecyclerViewHabitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind((item.content() as HabitModel))
    }

    override fun isOfViewType(item: DelegateItem) = item is HabitDelegateItem

    class ViewHolder(val binding: RecyclerViewHabitItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(model: HabitModel){
            binding.habit.text = model.habit
            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }
    }
}