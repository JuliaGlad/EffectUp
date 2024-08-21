package myapplication.android.mindall.domain.useCase.nightTrackers

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddNightTrackerUseCase {
    fun invoke(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        trackerId: String,
        date: String,
        value: String
    ): Completable {
        return DI.nightTrackers.addTracker(isNew, yearId, year, monthId, month, trackerId, date, value)
    }
}