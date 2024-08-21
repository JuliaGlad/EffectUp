package myapplication.android.mindall.domain.useCase.monthly.tasks

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddMonthlyTaskUseCase {
    fun invoke(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        taskId: String,
        title: String,
        flag: String
    ): Completable{
        return DI.monthlyTasks.addMonthlyTask(isNew, yearId, year, monthId, month, taskId, title, flag)
    }
}