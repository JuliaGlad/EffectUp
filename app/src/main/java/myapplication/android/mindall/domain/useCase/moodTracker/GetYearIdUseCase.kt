package myapplication.android.mindall.domain.useCase.moodTracker

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetYearIdUseCase {
    fun invoke(year: String): Single<String>{
        return DI.moodTrackerRepository.getYearId(year)
    }
}