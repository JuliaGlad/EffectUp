package myapplication.android.mindall.di.daily

import myapplication.android.mindall.domain.useCase.dailyPlanTasks.AddDailyTaskUseCase
import myapplication.android.mindall.domain.useCase.dailyPlan.GetDailyPlanByDataUseCase
import myapplication.android.mindall.domain.useCase.dailyPlanTasks.UpdateTaskStatusUseCase
import myapplication.android.mindall.domain.useCase.dailyPlanTasks.GetDailyTasksUseCase

class DailyPlansDI {
    companion object{
        val getDailyPlanByDataUseCase = GetDailyPlanByDataUseCase()
        val getDailyTasksUseCase = GetDailyTasksUseCase()
        val addDailyTaskUseCase = AddDailyTaskUseCase()
        val updateTaskStatusUseCase = UpdateTaskStatusUseCase()
    }

}