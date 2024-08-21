package myapplication.android.mindall.presentation.plans.basicPlans.yearly.models

import myapplication.android.mindall.presentation.plans.basicPlans.monthly.model.GoalPresModel

data class GoalAndDatesModels(
    val goals: MutableList<GoalPresModel>,
    val dates: MutableList<DatesModel>
)