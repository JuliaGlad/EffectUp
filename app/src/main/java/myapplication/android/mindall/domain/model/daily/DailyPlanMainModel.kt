package myapplication.android.mindall.domain.model.daily

data class DailyPlanMainModel(
    val id: String,
    val data: String?,
    val greenFlagCount: String?,
    val yellowFlagCount: String?,
    val redFlagCount: String?
)