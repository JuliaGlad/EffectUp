package myapplication.android.mindall.domain.model.weekly

import io.reactivex.rxjava3.core.Single

data class AllDaysModel (
    val days: Single<List<DayOfWeekModel>>
)