package myapplication.android.mindall.presentation.plans.basicPlans.daily.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.common.State
import myapplication.android.mindall.common.Status
import myapplication.android.mindall.di.daily.DailyPlansDI
import myapplication.android.mindall.domain.model.daily.DailyPlanMainModel
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.DailyPlanModel

class CalendarViewModel : ViewModel(){

    private val _noPlan: MutableLiveData<Boolean> = MutableLiveData(false)
    var noPlan = _noPlan

    private val _state : MutableLiveData<State<DailyPlanModel>> = MutableLiveData(State.loading())
    var state = _state

    fun getPlansInfo(date: String) {
        DailyPlansDI.getDailyPlanByDataUseCase.invoke(date)
            .subscribeOn(Schedulers.io())
            .subscribe(object: SingleObserver<DailyPlanMainModel>{
                override fun onSubscribe(d: Disposable) {
                  Log.i("GetPlansInfo Daily onSubscribe", "on subscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {

                    if (_state.value?.status?.equals(Status.LOADING) == true){
                        _state.value = State.success(null)
                    }

                    if (e.message?.equals(NO_PLANS) == true){
                        _noPlan.value = true
                    }
                }

                override fun onSuccess(model: DailyPlanMainModel) {
                    val dailyPlan = DailyPlanModel(
                        model.id,
                        model.data,
                        model.greenFlagCount,
                        model.yellowFlagCount,
                        model.redFlagCount
                    )
                    if (_state.value?.status?.equals(Status.LOADING) == true){
                        _state.value = State.success(dailyPlan)
                    }
                }

            })
    }

    fun setLoading() {
        _state.value = State.loading()
    }
}