package myapplication.android.mindall.domain.useCase.dailyPlanTasks

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class UpdateTaskStatusUseCase {
    fun invoke(planId: String, tasks: List<String>, status: Boolean) : Completable{
        return DI.dailyTasks.updateListTasksStatus(planId, tasks, status)
    }
}