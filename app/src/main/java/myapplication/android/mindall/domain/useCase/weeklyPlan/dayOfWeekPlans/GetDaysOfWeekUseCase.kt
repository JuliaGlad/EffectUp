package myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.weekly.AllDaysModel
import myapplication.android.mindall.domain.model.weekly.DayOfWeekModel
import java.util.stream.Collectors

class GetDaysOfWeekUseCase {
    fun invoke(weekPlansId: String?): Single<List<DayOfWeekModel>> {
        return DI.daysOfWeekPlans.getDaysOfWeek(weekPlansId).map { dtos ->
             dtos.stream()
                .map { dto ->
                    DayOfWeekModel(
                        dto.id,
                        dto.dayOfWeek,
                        dto.date,
                        dto.greenTaskCount,
                        dto.yellowTaskCount,
                        dto.redTaskCount
                    )
                }.collect(Collectors.toList())
        }

    }
}