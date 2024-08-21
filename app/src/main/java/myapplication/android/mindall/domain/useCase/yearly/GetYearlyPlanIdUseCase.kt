package myapplication.android.mindall.domain.useCase.yearly

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetYearlyPlanIdUseCase {
    fun invoke(year: String): Single<String> {
        return DI.yearlyPlansRepository.getYearId(year)
    }
}