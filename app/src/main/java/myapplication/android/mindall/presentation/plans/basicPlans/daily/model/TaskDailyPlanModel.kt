package myapplication.android.mindall.presentation.plans.basicPlans.daily.model

data class TaskDailyPlanModel(
    val id: String,
    val title: String?,
    val isCompleted: Boolean?,
    val flag: String?
)