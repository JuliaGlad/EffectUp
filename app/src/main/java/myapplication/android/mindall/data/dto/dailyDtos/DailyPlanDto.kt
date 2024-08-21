package myapplication.android.mindall.data.dto.dailyDtos

data class DailyPlanDto(
    val id: String,
    val data: String?,
    val greenTaskCount: String?,
    val yellowTaskCount: String?,
    val redTaskCount: String?
)