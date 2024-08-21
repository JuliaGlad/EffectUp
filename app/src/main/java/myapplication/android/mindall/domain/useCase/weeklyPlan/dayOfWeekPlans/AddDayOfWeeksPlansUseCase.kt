package myapplication.android.mindall.domain.useCase.weeklyPlan.dayOfWeekPlans

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekPresModel

class AddDayOfWeeksPlansUseCase {
    fun invoke(
        isNew: Boolean?,
        duration: String,
        days: List<DayOfWeekPresModel>,
        weeklyPlanId: String,
        dayOfWeekId: String,
        taskId: String,
        title: String,
        flag: String
    ) : Completable{
        return DI.daysOfWeekPlans.addDayOfWeeksPlans(isNew, duration, days, weeklyPlanId, dayOfWeekId, taskId, title, flag)
    }
}