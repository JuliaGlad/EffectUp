package myapplication.android.mindall.common.recyclerView.buttonWithIcon

import androidx.recyclerview.widget.DiffUtil

class ButtonWithIconCallBack : DiffUtil.ItemCallback<ButtonWithIconModel>() {
    override fun areItemsTheSame(
        oldItem: ButtonWithIconModel,
        newItem: ButtonWithIconModel
    ): Boolean {
       return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ButtonWithIconModel,
        newItem: ButtonWithIconModel
    ): Boolean {
        return oldItem.compareTo(newItem)
    }
}