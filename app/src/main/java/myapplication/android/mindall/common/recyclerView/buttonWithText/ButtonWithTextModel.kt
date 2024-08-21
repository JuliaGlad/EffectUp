package myapplication.android.mindall.common.recyclerView.buttonWithText

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class ButtonWithTextModel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val listener: ButtonClickListener
) {
    fun compareTo(other: ButtonWithTextModel) : Boolean{
        return this.hashCode() == other.hashCode()
    }
}