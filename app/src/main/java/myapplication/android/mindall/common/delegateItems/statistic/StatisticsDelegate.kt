package myapplication.android.mindall.common.delegateItems.statistic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerStatisticsItemBinding
import myapplication.android.mindall.databinding.RecyclerViewImportantDateItemBinding

class StatisticsDelegate : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(RecyclerStatisticsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as StatisticsModel)
    }

    override fun isOfViewType(item: DelegateItem) = item is StatisticsDelegateItem

    class ViewHolder(val binding: RecyclerStatisticsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: StatisticsModel) {
            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }
    }
}