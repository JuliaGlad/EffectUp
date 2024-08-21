package myapplication.android.mindall.domain.model.weekly

data class DayOfWeekModel(
    val id: String,
    val dayOfWeek: String?,
    val date: String?,
    val greenTaskCount: String?,
    val yellowTaskCount: String?,
    val redTaskCount: String?
)
