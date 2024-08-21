package myapplication.android.mindall.domain.useCase.dailyPlanSchedule

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class EditDailyScheduleUseCase {
    fun invoke(
        dailyPlanId: String,
        scheduleId: String,
        task: String,
        duration: String,
        isNotificationOn: Boolean,
        notificationStart: String
    ) : Completable{
        return DI.daySchedule.editSchedule(dailyPlanId, scheduleId, task, duration, isNotificationOn, notificationStart)
    }
}