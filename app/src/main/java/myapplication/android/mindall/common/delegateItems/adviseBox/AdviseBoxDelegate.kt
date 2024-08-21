package myapplication.android.mindall.common.delegateItems.adviseBox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewButtonWithIconAndTextBinding
import myapplication.android.mindall.databinding.RecyclerViewInfoBoxBinding

class AdviseBoxDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(RecyclerViewInfoBoxBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as AdviseBoxModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is AdviseBoxDelegateItem

    class ViewHolder(val binding: RecyclerViewInfoBoxBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(model: AdviseBoxModel){
            binding.body.text = model.title
        }
    }
}
