package myapplication.android.mindall.common.recyclerView.buttonWithIconAndText

import androidx.recyclerview.widget.DiffUtil

class ButtonWithIconAndTextCallBack : DiffUtil.ItemCallback<ButtonWithIconAndTextModel>() {
    override fun areItemsTheSame(
        oldItem: ButtonWithIconAndTextModel,
        newItem: ButtonWithIconAndTextModel
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: ButtonWithIconAndTextModel,
        newItem: ButtonWithIconAndTextModel
    ): Boolean = oldItem.compareTo(newItem)
}