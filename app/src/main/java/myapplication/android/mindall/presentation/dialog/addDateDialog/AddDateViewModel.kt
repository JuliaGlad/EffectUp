package myapplication.android.mindall.presentation.dialog.addDateDialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.yearly.YearlyDatesDI

class AddDateViewModel : ViewModel() {

    private val _isAdded = MutableLiveData(false)
    var isAdded = _isAdded

    fun addDate(
        isNew: Boolean,
        yearId: String,
        year: String,
        dateId: String,
        date: String,
        event: String
    ) {
        YearlyDatesDI.addYearlyDateUseCase.invoke(isNew, yearId, year, dateId, date, event)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.e("Add yearly date on subscribe", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.e("Add yearly date on subscribe", "on error ${e.message}")
                }
            })
    }
}