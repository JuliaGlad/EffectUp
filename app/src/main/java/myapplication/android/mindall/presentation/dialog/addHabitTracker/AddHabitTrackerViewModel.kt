package myapplication.android.mindall.presentation.dialog.addHabitTracker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.trackers.HabitTrackerDI

class AddHabitTrackerViewModel : ViewModel() {

    private val _isAdded = MutableLiveData(false)
    var isAdded = _isAdded

    fun addTracker(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        habitId: String,
        trackerId: String,
        date: String,
        isComplete: Boolean
    ){
        HabitTrackerDI.addHabitTrackerUseCase.invoke(isNew, yearId, year, monthId, month, habitId, trackerId, date, isComplete)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add habit tracker", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.i("Add habit tracker", "on error ${e.message}")
                }

            })
    }

}