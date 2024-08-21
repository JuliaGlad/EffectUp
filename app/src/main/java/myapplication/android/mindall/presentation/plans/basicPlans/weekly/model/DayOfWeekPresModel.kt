package myapplication.android.mindall.presentation.plans.basicPlans.weekly.model

data class DayOfWeekPresModel(
    val id: String,
    val dayOfWeek: String?,
    val date: String?,
    val greenTaskCount: String?,
    val yellowTaskCount: String?,
    val redTaskCount: String?
)
