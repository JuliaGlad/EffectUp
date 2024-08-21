package myapplication.android.mindall.domain.useCase.dailyPlanSchedule

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.daily.ScheduleMainModel
import java.util.stream.Collectors

class GetDailyScheduleUseCase {
    fun invoke(dailyPlanId: String): Single<List<ScheduleMainModel>> {
        return DI.daySchedule.getSchedules(dailyPlanId).map { dtos ->
            dtos.stream()
                .map { dto ->
                    ScheduleMainModel(
                        dto.id,
                        dto.duration,
                        dto.task,
                        dto.isNotificationOn,
                        dto.notificationStart
                    )
                }.collect(Collectors.toList())
        }
    }
}