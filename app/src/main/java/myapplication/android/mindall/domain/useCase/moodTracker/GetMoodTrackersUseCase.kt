package myapplication.android.mindall.domain.useCase.moodTracker

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.trackers.TrackerModel
import java.util.stream.Collectors

class GetMoodTrackersUseCase {
    fun invoke(
        yearId: String,
        monthId: String
    ): Single<List<TrackerModel>> {
        return DI.moodTracker.getMonthTrackers(yearId, monthId).map { dtos ->
            dtos.stream()
                .map { dto -> TrackerModel(dto.trackerId, dto.date, dto.value) }
                .collect(Collectors.toList())
        }
    }
}