package myapplication.android.mindall.presentation.dialog.addTracker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants.Companion.MOOD_TRACKER
import myapplication.android.mindall.common.Constants.Companion.NIGHT_TRACKER
import myapplication.android.mindall.di.trackers.MoodTrackersDI
import myapplication.android.mindall.di.trackers.NightTrackersDI

class AddTrackerViewModel : ViewModel() {

    private val _isAdded = MutableLiveData(false)
    var isAdded = _isAdded

    fun addTracker(
        type: String,
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        trackerId: String,
        date: String,
        value: String
    ){
        if (type == NIGHT_TRACKER) {
            addNightTracker(isNew, yearId, year, monthId, month, trackerId, date, value)
        } else if (type == MOOD_TRACKER) {
            addMoodTracker(isNew, yearId, year, monthId, month, trackerId, date, value)
        }
    }

    private fun addNightTracker(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        trackerId: String,
        date: String,
        value: String
    ) {
        NightTrackersDI.addNightTrackerUseCase.invoke(isNew, yearId, year, monthId, month, trackerId, date, value)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add night tracker", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.e("Add night tracker", "on error ${e.message}")
                }
            })
    }

    private fun addMoodTracker(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        trackerId: String,
        date: String,
        value: String
    ) {
        MoodTrackersDI.addMoodTrackerUseCase.invoke(isNew, yearId, year, monthId, month, trackerId, date, value)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add night tracker", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.e("Add night tracker", "on error ${e.message}")
                }
            })
    }
}