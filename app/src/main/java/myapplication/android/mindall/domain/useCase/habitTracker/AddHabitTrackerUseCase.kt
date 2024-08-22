package myapplication.android.mindall.domain.useCase.habitTracker

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddHabitTrackerUseCase {
    fun invoke(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        habitId: String,
        trackerId: String,
        date: String,
        isComplete: Boolean
    ): Completable {
        return DI.habitTrackers.addTracker(isNew, yearId, year, monthId, month, habitId, trackerId, date, isComplete)
    }
}