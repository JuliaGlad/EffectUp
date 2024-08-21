package myapplication.android.mindall.di.yearly

import myapplication.android.mindall.domain.useCase.yearly.goals.AddYearlyGoalsUseCase
import myapplication.android.mindall.domain.useCase.yearly.goals.GetYearlyGoalsUseCase

class YearlyGoalsDI {
    companion object{
        val getYearlyGoalsUseCase = GetYearlyGoalsUseCase()
        val addYearlyGoalsUseCase = AddYearlyGoalsUseCase()
    }
}