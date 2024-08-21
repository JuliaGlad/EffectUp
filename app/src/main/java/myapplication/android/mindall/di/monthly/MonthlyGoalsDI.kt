package myapplication.android.mindall.di.monthly

import myapplication.android.mindall.domain.useCase.monthly.goals.AddMonthlyGoalUseCase
import myapplication.android.mindall.domain.useCase.monthly.goals.GetMonthlyGoalsUseCase

class MonthlyGoalsDI {
    companion object{
        val getMonthlyGoalsUseCase = GetMonthlyGoalsUseCase()
        val addMonthlyGoalUseCase = AddMonthlyGoalUseCase()
    }
}