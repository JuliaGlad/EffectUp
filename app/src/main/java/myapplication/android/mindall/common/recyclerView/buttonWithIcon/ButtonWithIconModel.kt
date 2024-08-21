package myapplication.android.mindall.common.recyclerView.buttonWithIcon

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class ButtonWithIconModel(
    val id: Int,
    val title: String,
    val icon: Int,
    val isAttention: Boolean,
    val listener: ButtonClickListener
) {
 fun compareTo(other : ButtonWithIconModel) : Boolean{
     return other.hashCode() == this.hashCode()
 }
}