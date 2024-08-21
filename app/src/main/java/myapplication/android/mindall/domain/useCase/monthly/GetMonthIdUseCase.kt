package myapplication.android.mindall.domain.useCase.monthly

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetMonthIdUseCase {
    fun invoke(
        month: String,
        yearId: String
    ): Single<String> {
        return DI.monthlyPlanRepository.getMonthId(month, yearId)
    }
}