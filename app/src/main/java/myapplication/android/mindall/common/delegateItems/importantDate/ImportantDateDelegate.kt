package myapplication.android.mindall.common.delegateItems.importantDate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewImportantDateItemBinding

class ImportantDateDelegate : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(RecyclerViewImportantDateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as ImportantDateModel)
    }

    override fun isOfViewType(item: DelegateItem) = item is ImportantDateDelegateItem

    class ViewHolder(val binding: RecyclerViewImportantDateItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: ImportantDateModel) {
            binding.titleTask.text = model.task
            binding.titleDuration.text = model.date
        }
    }
}