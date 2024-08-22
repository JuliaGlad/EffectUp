package myapplication.android.mindall.domain.useCase.habitTracker

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.data.dto.trackers.HabitDto
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.trackers.HabitModel
import java.util.stream.Collectors

class GetHabitsUseCase {
    fun invoke(): Single<List<HabitModel>> {
        return DI.habitTrackersRepository.getHabits().map { dtos ->
            dtos.stream()
                .map { dto -> HabitModel(dto.id, dto.habit) }
                .collect(Collectors.toList())
        }
    }
}