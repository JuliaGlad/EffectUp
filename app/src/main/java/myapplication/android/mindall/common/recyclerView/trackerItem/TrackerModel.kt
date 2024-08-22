package myapplication.android.mindall.common.recyclerView.trackerItem

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class TrackerModel(
    val id: Int,
    val value: String,
    val valueIcon: Int,
    var isChosen: Boolean,
    val listener: ButtonClickListener
) {
    fun compareTo(other: TrackerModel) = this.hashCode() == other.hashCode()

}