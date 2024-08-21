package myapplication.android.mindall.common.recyclerView.buttonWithIconAndText

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class ButtonWithIconAndTextModel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: Int,
    val listener: ButtonClickListener
) {
    fun compareTo(other: ButtonWithIconAndTextModel) : Boolean{
        return this.hashCode() == other.hashCode()
    }
}