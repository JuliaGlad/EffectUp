package myapplication.android.mindall.domain.useCase.dailyPlanSchedule

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class DeleteScheduleUseCase {
    fun invoke(dailyPlanId: String, scheduleId: String): Completable{
        return DI.daySchedule.deleteSchedule(dailyPlanId, scheduleId)
    }
}