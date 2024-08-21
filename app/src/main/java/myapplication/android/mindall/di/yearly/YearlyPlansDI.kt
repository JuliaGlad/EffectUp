package myapplication.android.mindall.di.yearly

import myapplication.android.mindall.domain.useCase.yearly.GetYearlyPlanIdUseCase

class YearlyPlansDI {
    companion object{
        val getYearlyPlanIdUseCase = GetYearlyPlanIdUseCase()
    }
}