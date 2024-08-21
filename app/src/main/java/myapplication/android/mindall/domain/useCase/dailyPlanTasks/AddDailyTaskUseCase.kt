package myapplication.android.mindall.domain.useCase.dailyPlanTasks

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddDailyTaskUseCase {
    fun invoke(isNew: Boolean?, data: String, planId: String, taskId: String, title: String, flag: String): Completable{
        return DI.dailyTasks.addTask(isNew, data, planId, taskId, title, flag)
    }
}