package myapplication.android.mindall.domain.useCase.habitTracker

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetMonthIdUseCase {
    fun invoke(
        yearId: String,
        habitId: String,
        month: String
    ): Single<String>{
        return DI.habitTrackers.getMonthId(yearId, habitId, month)
    }
}