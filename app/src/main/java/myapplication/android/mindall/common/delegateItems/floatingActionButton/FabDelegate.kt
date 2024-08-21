package myapplication.android.mindall.common.delegateItems.floatingActionButton

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewFloatingActionButtonBinding

class FabDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(RecyclerViewFloatingActionButtonBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as FabModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is FabDelegateItem

    class ViewHolder(val binding: RecyclerViewFloatingActionButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: FabModel) {
            binding.floatingActionButton.setOnClickListener {
                model.listener.onClick()
            }
        }
    }
}