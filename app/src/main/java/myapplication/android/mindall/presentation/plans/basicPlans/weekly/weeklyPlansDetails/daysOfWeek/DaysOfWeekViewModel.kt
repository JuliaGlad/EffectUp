package myapplication.android.mindall.presentation.plans.basicPlans.weekly.weeklyPlansDetails.daysOfWeek

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.weekly.WeeklyPlansDI
import myapplication.android.mindall.domain.model.weekly.DayOfWeekModel
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekPresModel

class DaysOfWeekViewModel : ViewModel() {

    private val _state : MutableLiveData<State<MutableList<DayOfWeekPresModel>>?> =
        MutableLiveData(State.loading())
    var state = _state

    fun getWeekDaysData(weeklyPlansId: String?) {
        WeeklyPlansDI.getDaysOfWeekUseCase.invoke(weeklyPlansId)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<DayOfWeekModel>> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get week days data", "on subscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {
                    Log.e("Get week days data", "on error ${e.message}")
                }

                override fun onSuccess(days: List<DayOfWeekModel>) {
                   if (days.isNotEmpty()){
                       val models = mutableListOf<DayOfWeekPresModel>()
                       for (i in days){
                           models.add(
                               DayOfWeekPresModel(
                                   i.id,
                                   i.dayOfWeek,
                                   i.date,
                                   i.greenTaskCount,
                                   i.yellowTaskCount,
                                   i.redTaskCount
                               )
                           )
                       }
                      _state.value = State.success(models)
                   } else {
                       _state.value = State.success(null)
                   }
                }

            })
    }
}