package myapplication.android.mindall.presentation.dialog.addWeeklyTaskiDialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.weekly.WeeklyPlansDI
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekPresModel

class AddWeeklyTaskViewModel : ViewModel(){
    private val _isAdded : MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isAdded = _isAdded

    fun addTask(
        isNew: Boolean?,
        duration: String,
        days: List<DayOfWeekPresModel>,
        weeklyPlanId: String,
        dayOfWeekId: String,
        taskId: String,
        title: String,
        flag: String
    ) {
        WeeklyPlansDI.addDayOfWeekPlansUseCase.invoke(
            isNew, duration, days, weeklyPlanId, dayOfWeekId, taskId, title, flag
        )
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add weekly task", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.value = true
                }

                override fun onError(e: Throwable) {
                    Log.e("Add weekly task", "on error ${e.message}")
                }

            })
    }
}