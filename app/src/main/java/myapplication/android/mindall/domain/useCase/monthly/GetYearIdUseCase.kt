package myapplication.android.mindall.domain.useCase.monthly

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.data.repository.MonthlyPlanRepository
import myapplication.android.mindall.di.DI

class GetYearIdUseCase {
    fun invoke(year: String): Single<String>{
        return DI.monthlyPlanRepository.getYearId(year)
    }
}