package myapplication.android.mindall.di.monthly

import myapplication.android.mindall.domain.useCase.monthly.GetMonthIdUseCase
import myapplication.android.mindall.domain.useCase.monthly.GetYearIdUseCase

class MonthlyDI {
    companion object{
        val getMonthIdUseCase = GetMonthIdUseCase()
        val getYearIdUseCase = GetYearIdUseCase()
    }
}