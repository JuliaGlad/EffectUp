package myapplication.android.mindall.presentation.dialog.addGoalDialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.monthly.MonthlyGoalsDI

class AddMonthlyGoalViewModel : ViewModel() {

    private val _isAdded = MutableLiveData(false)
    var isAdded = _isAdded

    fun addGoal(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        goalId: String,
        goal: String
    ) {
        MonthlyGoalsDI.addMonthlyGoalUseCase.invoke(isNew, yearId, year, monthId, month, goalId, goal)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add monthly goal", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.e("Add monthly goal", "on error ${e.message}")
                }
            })
    }

}