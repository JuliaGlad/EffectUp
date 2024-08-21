package myapplication.android.mindall.common.delegateItems.tracker

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class TrackerModel(
    val id: Int?,
    val date: String?,
    val value: String?,
    val listener: ButtonClickListener
)