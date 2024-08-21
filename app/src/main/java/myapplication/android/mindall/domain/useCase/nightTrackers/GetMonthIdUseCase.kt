package myapplication.android.mindall.domain.useCase.nightTrackers

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetMonthIdUseCase {
    fun invoke(yearId: String, month: String): Single<String>{
        return DI.nightTrackers.getMonthId(yearId, month)
    }
}