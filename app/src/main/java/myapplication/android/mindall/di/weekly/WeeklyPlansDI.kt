package myapplication.android.mindall.di.weekly

import myapplication.android.mindall.domain.useCase.weeklyPlan.GetWeeklyPlansUseCase
import myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans.AddDayOfWeeksPlansUseCase
import myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans.GetDayOfWeekTaskUseCase
import myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans.GetDaysOfWeekUseCase
import myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans.UpdateTaskStatusUseCase

class WeeklyPlansDI {
    companion object{
        val getWeeklyPlansUseCase = GetWeeklyPlansUseCase()
        val getDaysOfWeekUseCase = GetDaysOfWeekUseCase()
        val getDayOfWeekTaskUseCase = GetDayOfWeekTaskUseCase()
        val updateTaskStatusUseCase = UpdateTaskStatusUseCase()
        val addDayOfWeekPlansUseCase = AddDayOfWeeksPlansUseCase()
    }
}