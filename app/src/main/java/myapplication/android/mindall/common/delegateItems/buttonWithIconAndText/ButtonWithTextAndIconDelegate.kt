package myapplication.android.mindall.common.delegateItems.buttonWithIconAndText

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.R
import myapplication.android.mindall.common.delegate.AdapterDelegate
import myapplication.android.mindall.common.delegate.DelegateItem
import myapplication.android.mindall.databinding.RecyclerViewButtonWithIconAndTextBinding
import myapplication.android.mindall.databinding.RecyclerViewScheduleItemBinding

class ButtonWithTextAndIconDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(RecyclerViewButtonWithIconAndTextBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as ButtonWithTextAndIconDelegateModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is ButtonWithTextAndIconDelegateItem

    class ViewHolder(val binding: RecyclerViewButtonWithIconAndTextBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(model: ButtonWithTextAndIconDelegateModel){
            binding.title.text = model.title
            binding.subtext.text = model.subtitle
            binding.icon.setImageDrawable(ResourcesCompat.getDrawable(itemView.resources, model.icon, itemView.context.theme))
            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }
    }
}
