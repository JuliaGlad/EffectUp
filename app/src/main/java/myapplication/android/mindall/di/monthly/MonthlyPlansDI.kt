package myapplication.android.mindall.di.monthly

import myapplication.android.mindall.domain.useCase.monthly.tasks.AddMonthlyTaskUseCase
import myapplication.android.mindall.domain.useCase.monthly.tasks.GetMonthlyTaskUseCase
import myapplication.android.mindall.domain.useCase.monthly.tasks.UpdateMonthlyTaskStatusUseCase
import myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans.UpdateTaskStatusUseCase

class MonthlyPlansDI {
    companion object {
        val getMonthlyTaskUseCase = GetMonthlyTaskUseCase()
        val addMonthlyPlansUseCase = AddMonthlyTaskUseCase()
        val updateMonthlyTaskStatusUseCase = UpdateMonthlyTaskStatusUseCase()
    }
}