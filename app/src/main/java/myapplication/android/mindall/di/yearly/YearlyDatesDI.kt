package myapplication.android.mindall.di.yearly

import myapplication.android.mindall.domain.useCase.yearly.dates.AddYearlyDateUseCase
import myapplication.android.mindall.domain.useCase.yearly.dates.GetYearlyDatesUseCase

class YearlyDatesDI {
    companion object{
        val getYearlyDatesUseCase = GetYearlyDatesUseCase()
        val addYearlyDateUseCase = AddYearlyDateUseCase()
    }
}