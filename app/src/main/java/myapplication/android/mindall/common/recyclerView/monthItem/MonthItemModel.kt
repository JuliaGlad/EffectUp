package myapplication.android.mindall.common.recyclerView.monthItem

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class MonthItemModel(
    val id: Int,
    val month: String,
    val duration: String,
    var isChosen: Boolean,
    val listener: ButtonClickListener
){
    fun compareTo(other: MonthItemModel) = this.hashCode() == other.hashCode()
}