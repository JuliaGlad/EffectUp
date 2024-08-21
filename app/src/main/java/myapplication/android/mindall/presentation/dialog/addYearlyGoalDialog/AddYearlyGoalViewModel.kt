package myapplication.android.mindall.presentation.dialog.addYearlyGoalDialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.yearly.YearlyGoalsDI

class AddYearlyGoalViewModel : ViewModel() {

    private val _isAdded = MutableLiveData(false)
    var isAdded = _isAdded

    fun addGoal(
        isNew: Boolean,
        yearId: String,
        year: String,
        goalId: String,
        goal: String
    ) {
        YearlyGoalsDI.addYearlyGoalsUseCase.invoke(isNew, yearId, year, goalId, goal)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add yearly goal use case", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.e("Add yearly goal use case", "on error ${e.message}")
                }
            })
    }

}