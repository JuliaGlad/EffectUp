package myapplication.android.mindall.presentation.plans.basicPlans.daily.model

data class DailyPlanModel(
    val id: String,
    val data: String?,
    val greenFlagCount: String?,
    val yellowFlagCount: String?,
    val redFlagCount: String?
)