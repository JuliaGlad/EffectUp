package myapplication.android.mindall.presentation.plans.basicPlans.monthly.model

import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.TaskDailyPlanModel

data class GoalAndTaskModel(
    val tasks: List<TaskDailyPlanModel>,
    val goals: List<GoalPresModel>
)