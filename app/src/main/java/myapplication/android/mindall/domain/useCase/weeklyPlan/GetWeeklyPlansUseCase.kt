package myapplication.android.mindall.domain.useCase.weeklyPlan

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetWeeklyPlansUseCase {
    fun invoke(date: String): Single<String>{
        return DI.weeklyPlansRepository.getWeeklyPlans(date)
    }
}