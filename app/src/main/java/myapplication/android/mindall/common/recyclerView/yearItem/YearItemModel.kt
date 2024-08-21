package myapplication.android.mindall.common.recyclerView.yearItem

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class YearItemModel(
    val id: Int,
    val year: String,
    var isChosen: Boolean,
    val listener: ButtonClickListener
){
    fun compareTo(other: YearItemModel) = this.hashCode() == other.hashCode()
}
