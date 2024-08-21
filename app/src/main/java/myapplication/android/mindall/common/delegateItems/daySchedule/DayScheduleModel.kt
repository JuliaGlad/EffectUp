package myapplication.android.mindall.common.delegateItems.daySchedule

import myapplication.android.mindall.common.listeners.ButtonClickListener

data class DayScheduleModel(
    val id: Int?,
    val title: String?,
    val duration: String?,
    val isNotificationOn: Boolean?,
    val listener: ButtonClickListener
)