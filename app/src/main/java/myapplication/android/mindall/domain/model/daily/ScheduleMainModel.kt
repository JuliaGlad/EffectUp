package myapplication.android.mindall.domain.model.daily

data class ScheduleMainModel(
    val id: String?,
    val duration: String?,
    val task: String?,
    val isNotificationOn: Boolean?,
    val notificationStart: String?
)