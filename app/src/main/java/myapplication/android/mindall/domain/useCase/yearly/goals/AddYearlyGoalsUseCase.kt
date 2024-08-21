package myapplication.android.mindall.domain.useCase.yearly.goals

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class AddYearlyGoalsUseCase {
    fun invoke(
        isNew: Boolean,
        yearId: String,
        year: String,
        goalId: String,
        goal: String
    ): Completable {
        return DI.yearlyGoals.addGoal(isNew, yearId, year, goalId, goal)
    }
}