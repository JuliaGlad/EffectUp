package myapplication.android.mindall.presentation.plans.basicPlans.weekly.calendar

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.ID
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.di.weekly.WeeklyPlansDI

class WeeklyPlanCalendarViewModel : ViewModel() {

    private val _havePlans : MutableLiveData<Bundle> = MutableLiveData(null)
    var havePlans = _havePlans

    fun checkPlans(week: String) {

        val bundle = Bundle()
        bundle.putString(DATA, week)

        WeeklyPlansDI.getWeeklyPlansUseCase.invoke(week)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("OnSubscribe check weekly plans", d.isDisposed.toString())
                }

                override fun onSuccess(id: String) {
                    bundle.putString(ID, id)
                    _havePlans.value = bundle
                }

                override fun onError(e: Throwable) {
                    if (e.message.toString() == NO_PLANS){
                        _havePlans.value = bundle
                    }
                }
            })
    }

}