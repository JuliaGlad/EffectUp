package myapplication.android.mindall.presentation.plans.basicPlans.yearly.yearPlanDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.yearly.YearlyDatesDI
import myapplication.android.mindall.di.yearly.YearlyGoalsDI
import myapplication.android.mindall.domain.model.monthly.GoalModel
import myapplication.android.mindall.domain.model.yearly.ImportantDateModel
import myapplication.android.mindall.presentation.plans.basicPlans.monthly.model.GoalPresModel
import myapplication.android.mindall.presentation.plans.basicPlans.yearly.models.DatesModel
import myapplication.android.mindall.presentation.plans.basicPlans.yearly.models.GoalAndDatesModels

class YearlyGoalsDetailsViewModel : ViewModel() {

    private val _state = MutableLiveData<State<GoalAndDatesModels>>(State.loading())
    var state = _state

    fun getDatesAndGoals(yearId: String){
        YearlyGoalsDI.getYearlyGoalsUseCase.invoke(yearId)
            .zipWith(YearlyDatesDI.getYearlyDatesUseCase.invoke(yearId)) { goals, dates ->
                val goalsList = mutableListOf<GoalPresModel>()
                initGoalsList(goals, goalsList)

                val datesList = mutableListOf<DatesModel>()
                initDatesList(dates, datesList)

                val goalAndDateModel = GoalAndDatesModels(goalsList, datesList)
                goalAndDateModel
            }
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<GoalAndDatesModels> {
                override fun onSubscribe(d: Disposable) {
                   Log.i("get yearly goals and dates", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(model: GoalAndDatesModels) {
                    _state.postValue(State.success(model))
                }

                override fun onError(e: Throwable) {
                    Log.e("get yearly goals and dates", "on error ${e.message}")
                }
            })
    }

    private fun initGoalsList(
        goals: List<GoalModel>,
        goalsList: MutableList<GoalPresModel>
    ) {
        for (i in goals) {
            goalsList.add(
                GoalPresModel(
                    i.id,
                    i.goal
                )
            )
        }
    }

    private fun initDatesList(
        dates: List<ImportantDateModel>,
        datesList: MutableList<DatesModel>
    ) {
        for (i in dates) {
            datesList.add(
                DatesModel(
                    i.id,
                    i.date,
                    i.event
                )
            )
        }
    }
}