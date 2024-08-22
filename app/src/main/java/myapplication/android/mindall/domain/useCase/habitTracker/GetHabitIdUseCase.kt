package myapplication.android.mindall.domain.useCase.habitTracker

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class GetHabitIdUseCase {
    fun invoke(habit: String): Single<String>{
        return DI.habitTrackersRepository.getHabitId(habit)
    }
}