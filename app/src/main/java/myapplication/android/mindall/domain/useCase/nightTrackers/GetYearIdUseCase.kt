package myapplication.android.mindall.domain.useCase.nightTrackers

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetYearIdUseCase {
    fun invoke(year: String): Single<String>{
        return DI.nightTrackersRepository.getYearId(year)
    }
}