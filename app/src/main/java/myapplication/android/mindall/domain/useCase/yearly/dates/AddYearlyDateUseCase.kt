package myapplication.android.mindall.domain.useCase.yearly.dates

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddYearlyDateUseCase {
    fun invoke(
        isNew: Boolean,
        yearId: String,
        year: String,
        dateId: String,
        date: String,
        event: String
    ): Completable{
        return DI.yearlyDats.addDate(isNew, yearId, year, dateId, date, event)
    }
}