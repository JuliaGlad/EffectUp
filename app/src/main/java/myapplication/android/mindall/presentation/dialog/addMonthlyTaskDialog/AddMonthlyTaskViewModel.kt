package myapplication.android.mindall.presentation.dialog.addMonthlyTaskDialog

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.monthly.MonthlyPlansDI

class AddMonthlyTaskViewModel : ViewModel() {

    private val _isAdded = MutableLiveData<Boolean>(false)
    var isAdded = _isAdded

    fun addTask(
        isNew: Boolean,
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        taskId: String,
        title: String,
        flag: String
    ){
        MonthlyPlansDI.addMonthlyPlansUseCase.invoke(isNew, yearId, year, monthId, month, taskId, title, flag)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add task", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete(){
                    _isAdded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.e("Add task", "on error ${e.message}")
                }
            })
    }
}