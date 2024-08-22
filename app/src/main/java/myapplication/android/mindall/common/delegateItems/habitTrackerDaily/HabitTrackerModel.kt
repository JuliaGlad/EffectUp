package myapplication.android.mindall.common.delegateItems.habitTrackerDaily

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class HabitTrackerModel(
    val id: Int,
    val date: String,
    val isComplete: Boolean,
    val listener: ButtonClickListener
)