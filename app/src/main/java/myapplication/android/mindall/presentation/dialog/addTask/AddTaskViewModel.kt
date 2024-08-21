package myapplication.android.mindall.presentation.dialog.addTask

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.daily.DailyPlansDI

class AddTaskViewModel: ViewModel() {
    private val _isAdded : MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    var isAdded = _isAdded

    fun addTask(isNew: Boolean?, data: String, planId: String, taskId: String, title: String, flag: String){
        DailyPlansDI.addDailyTaskUseCase.invoke(isNew, data, planId, taskId, title, flag)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("Add task", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _isAdded.value = true
                }

                override fun onError(e: Throwable) {
                    Log.i("Add task", "on error ${e.message}")
                }

            })
    }
}