package myapplication.android.mindall.domain.useCase.monthly.goals

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddMonthlyGoalUseCase {
    fun invoke(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        goalId: String,
        goal: String
    ): Completable{
        return DI.monthlyGoals.addMonthlyGoal(isNew, yearId, year, monthId, month, goalId, goal)
    }
}