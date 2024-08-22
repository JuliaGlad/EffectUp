package myapplication.android.mindall.domain.useCase.habitTracker

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.trackers.HabitTrackersModel
import java.util.stream.Collectors

class GetMonthHabitTrackersUseCase {
    fun invoke(
        yearId: String,
        monthId: String,
        habitId: String
    ): Single<List<HabitTrackersModel>> {
        return DI.habitTrackers.getMonthTrackers(yearId, monthId, habitId).map { dtos ->
            dtos.stream()
                .map { dto -> HabitTrackersModel(dto.id, dto.date, dto.isCompleted) }
                .collect(Collectors.toList())
        }
    }
}