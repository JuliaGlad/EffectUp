package myapplication.android.mindall.domain.useCase.habitTracker

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddHabitUseCase {
    fun invoke(habitId: String, habit: String): Completable{
        return DI.habitTrackersRepository.addHabit(habitId, habit)
    }
}