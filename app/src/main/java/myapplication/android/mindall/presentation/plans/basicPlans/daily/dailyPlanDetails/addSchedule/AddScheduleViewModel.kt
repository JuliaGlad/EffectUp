package myapplication.android.mindall.presentation.plans.basicPlans.daily.dailyPlanDetails.addSchedule

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.daily.DailyScheduleDI
import java.util.Random

class AddScheduleViewModel : ViewModel() {

    private val _isAdded : MutableLiveData<Boolean> = MutableLiveData(false)
    var isAdded = _isAdded

    fun addSchedule(dailyPlanId: String, title: String?, duration: String, notificationOn: Boolean, startTime: String?) {
        val scheduleId = generateId()
        DailyScheduleDI.addDailyScheduleUseCase.invoke(dailyPlanId, scheduleId, title, duration, notificationOn, startTime)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add schedule on subscribe", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.value = true
                }

                override fun onError(e: Throwable) {
                    Log.e("Add schedule on error", "on error ${e.message.toString()}")
                }

            })
    }

    private fun generateId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }

}