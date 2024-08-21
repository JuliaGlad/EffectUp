package myapplication.android.mindall.common.recyclerView.buttonWithText

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.databinding.RecyclerViewButtonWithTextLayoutBinding

class ButtonWithTextAdapter : ListAdapter<ButtonWithTextModel, RecyclerView.ViewHolder>(ButtonWithTextCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(RecyclerViewButtonWithTextLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val binding = RecyclerViewButtonWithTextLayoutBinding.bind(itemView)

        fun bind(model: ButtonWithTextModel){
            binding.title.text = model.title
            binding.subtext.text = model.subtitle
            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }
    }
}