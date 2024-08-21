package myapplication.android.mindall.domain.useCase.dailyPlanTasks

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class EditTaskUseCase {
    fun invoke(planId: String, taskId: String, title: String, flag: String) : Completable{
        return DI.dailyTasks.editTask(planId, taskId, title, flag)
    }
}