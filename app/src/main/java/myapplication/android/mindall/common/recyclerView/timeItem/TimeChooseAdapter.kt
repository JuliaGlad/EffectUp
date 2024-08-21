package myapplication.android.mindall.common.recyclerView.timeItem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import myapplication.android.mindall.databinding.RecyclerViewTimeItemBinding

class TimeChooseAdapter :
    ListAdapter<TimeChooseModel, RecyclerView.ViewHolder>(TimeChooseItemCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ViewHolder(
            RecyclerViewTimeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(getItem(position))
    }

    class ViewHolder(val binding: RecyclerViewTimeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TimeChooseModel) {
            binding.time.text = model.time
            binding.item.setOnClickListener {
                model.listener.onClick()
            }
        }
    }
}