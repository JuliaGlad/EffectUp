package myapplication.android.mindall.common.recyclerView.timeItem

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class TimeChooseModel(
    val id: Int,
    val time: String,
    val listener: ButtonClickListener
) {
    fun compareTo(other: TimeChooseModel) = this.hashCode() == other.hashCode()

}