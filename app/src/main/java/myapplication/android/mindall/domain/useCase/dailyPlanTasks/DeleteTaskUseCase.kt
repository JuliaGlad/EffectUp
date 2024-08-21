package myapplication.android.mindall.domain.useCase.dailyPlanTasks

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class DeleteTaskUseCase {
    fun invoke(planId: String, taskId: String) : Completable{
        return DI.dailyTasks.deleteTask(planId, taskId)
    }
}