package myapplication.android.mindall.presentation.plans.basicPlans.yearly.chooseYear

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.di.yearly.YearlyPlansDI
import java.util.Random

class ChooseYearViewModel : ViewModel() {

    private val _isNew = MutableLiveData<Boolean?>(null)
    var isNew = _isNew

    private val _yearId = MutableLiveData<String?>(null)
    var yearId = _yearId

    fun getYearId(year: String) {
        YearlyPlansDI.getYearlyPlanIdUseCase.invoke(year)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("get yearly plan id", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(id: String) {
                    _yearId.postValue(id)
                    _isNew.postValue(false)
                }

                override fun onError(e: Throwable) {
                    Log.e("get yearly plan id", "on error ${e.message}")
                    if (e.message.toString() == NO_PLANS) {
                        _yearId.postValue(generateId())
                        _isNew.postValue(true)
                    }
                }
            })
    }

    fun generateId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }

    fun setValueNull() {
        _isNew.postValue(null)
        _yearId.postValue(null)
    }
}