package myapplication.android.mindall.data.dto.weeklyDtos

data class DayOfWeekPlansDto(
    val id: String,
    val dayOfWeek: String?,
    val date: String?,
    val greenTaskCount: String?,
    val yellowTaskCount: String?,
    val redTaskCount: String?
)