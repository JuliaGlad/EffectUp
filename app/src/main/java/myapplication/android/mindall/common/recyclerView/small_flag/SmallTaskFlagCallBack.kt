package myapplication.android.mindall.common.recyclerView.small_flag

import androidx.recyclerview.widget.DiffUtil

class SmallTaskFlagCallBack : DiffUtil.ItemCallback<SmallTaskFlagModel>() {
    override fun areItemsTheSame(
        oldItem: SmallTaskFlagModel, newItem: SmallTaskFlagModel
    ) = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: SmallTaskFlagModel,
        newItem: SmallTaskFlagModel
    ) = oldItem.compareTo(newItem)
}