package myapplication.android.mindall.common.recyclerView.buttonWithText

import androidx.recyclerview.widget.DiffUtil

class ButtonWithTextCallBack : DiffUtil.ItemCallback<ButtonWithTextModel>() {
    override fun areItemsTheSame(
        oldItem: ButtonWithTextModel,
        newItem: ButtonWithTextModel
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: ButtonWithTextModel,
        newItem: ButtonWithTextModel
    ): Boolean = oldItem.compareTo(newItem)
}