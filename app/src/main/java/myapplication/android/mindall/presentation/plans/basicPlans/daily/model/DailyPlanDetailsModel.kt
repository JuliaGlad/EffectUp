package myapplication.android.mindall.presentation.plans.basicPlans.daily.model

data class DailyPlanDetailsModel(
    val tasks : List<TaskDailyPlanModel>,
    val schedules : List<ScheduleDayModel>
)