package myapplication.android.mindall.presentation.plans.basicPlans.monthly.chooseYearAndMonth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.di.monthly.MonthlyDI
import java.util.Random

class ChooseYearAndMonthViewModel : ViewModel() {

    private val _monthId = MutableLiveData<String?>(null)
    var  monthId = _monthId

    private val _yearId = MutableLiveData<String?>(null)
    var yearId = _yearId

    private val _isNew = MutableLiveData<Boolean?>(null)
    var isNew = _isNew

    fun getYearId(year: String){
        MonthlyDI.getYearIdUseCase.invoke(year)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get year id", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(yearId: String) {
                    _yearId.postValue(yearId)
                }

                override fun onError(e: Throwable) {
                    if (e.message.toString() == NO_PLANS){
                        _yearId.postValue(generateId())
                    }
                }
            })
    }

    private fun generateId(): String {
        val rand = Random()
        val id = rand.nextInt(90000000) + 10000000
        return "@$id"
    }

    fun getMonthId(month: String, yearId: String){
        MonthlyDI.getMonthIdUseCase.invoke(month, yearId)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get month id", "on subscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {
                    Log.e("Get month id", "on error ${e.message}")
                    if (e.message.toString() == NO_PLANS){
                        _monthId.postValue(generateId())
                        _isNew.postValue(true)
                    }
                }

                override fun onSuccess(monthId: String) {
                    _monthId.postValue(monthId)
                    _isNew.postValue(false)
                }

            })
    }
}