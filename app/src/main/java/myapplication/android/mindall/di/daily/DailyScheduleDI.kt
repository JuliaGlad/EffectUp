package myapplication.android.mindall.di.daily

import myapplication.android.mindall.domain.useCase.dailyPlanSchedule.AddDailyScheduleUseCase
import myapplication.android.mindall.domain.useCase.dailyPlanSchedule.GetDailyScheduleUseCase

class DailyScheduleDI {
    companion object{
        val addDailyScheduleUseCase = AddDailyScheduleUseCase()
        val getDailyScheduleUseCase = GetDailyScheduleUseCase()
    }
}