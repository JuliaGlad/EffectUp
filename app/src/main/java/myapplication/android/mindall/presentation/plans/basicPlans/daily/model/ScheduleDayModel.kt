package myapplication.android.mindall.presentation.plans.basicPlans.daily.model

data class ScheduleDayModel(
    val id: String?,
    val duration: String?,
    val task: String?,
    val isNotificationOn: Boolean?,
    val notificationStart: String?
)