package myapplication.android.mindall.common.delegateItems.habit

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class HabitModel(
    val id: Int,
    val habit: String,
    val listener: ButtonClickListener
)