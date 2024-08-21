package myapplication.android.mindall.domain.useCase.nightTrackers

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.trackers.TrackerModel
import java.util.stream.Collectors

class GetNightTrackersUseCase {
    fun invoke(
        yearId: String,
        monthId: String
    ): Single<List<TrackerModel>> {
        return DI.nightTrackers.getMonthTrackers(yearId, monthId).map { dtos ->
            dtos.stream()
                .map { dto -> TrackerModel(dto.trackerId, dto.date, dto.value) }
                .collect(Collectors.toList())
        }
    }
}