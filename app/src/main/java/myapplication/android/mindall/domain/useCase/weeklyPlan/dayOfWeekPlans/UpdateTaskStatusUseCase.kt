package myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class UpdateTaskStatusUseCase {
    fun invoke(
        weekPlansId: String,
        dayOfWeekId: String,
        tasks: List<String>,
        status: Boolean
    ) : Completable{
        return DI.daysOfWeekPlans.updateTaskStatus(weekPlansId, dayOfWeekId, tasks, status)
    }
}