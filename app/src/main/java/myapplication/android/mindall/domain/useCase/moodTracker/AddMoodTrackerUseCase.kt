package myapplication.android.mindall.domain.useCase.moodTracker

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddMoodTrackerUseCase {
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
        return DI.moodTracker.addTracker(isNew, yearId, year, monthId, month, trackerId, date, value)
    }
}