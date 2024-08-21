package myapplication.android.mindall.domain.useCase.monthly.tasks

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class UpdateMonthlyTaskStatusUseCase {
    fun invoke(
        yearId: String,
        monthId: String,
        tasks: List<String>,
        status: Boolean
    ): Completable{
        return DI.monthlyTasks.updateMonthlyTaskStatus(yearId, monthId, tasks, status)
    }
}