package myapplication.android.mindall.domain.useCase.moodTracker

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetMonthIdUseCase {
    fun invoke(yearId: String, month: String): Single<String>{
        return DI.moodTracker.getMonthId(yearId, month)
    }
}