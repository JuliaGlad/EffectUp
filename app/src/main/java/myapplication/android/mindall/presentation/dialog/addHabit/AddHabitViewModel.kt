package myapplication.android.mindall.presentation.dialog.addHabit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants.Companion.MOOD_TRACKER
import myapplication.android.mindall.common.Constants.Companion.NIGHT_TRACKER
import myapplication.android.mindall.di.trackers.HabitTrackerDI
import myapplication.android.mindall.di.trackers.MoodTrackersDI
import myapplication.android.mindall.di.trackers.NightTrackersDI

class AddHabitViewModel : ViewModel() {

    private val _isAdded = MutableLiveData(false)
    var isAdded = _isAdded

    fun addHabit(
        habitId: String,
        habit: String,

    ){
        HabitTrackerDI.addHabitUseCase.invoke(habitId, habit)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add habit", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.i("Add habit", "on error ${e.message}")
                }

            })
    }

}