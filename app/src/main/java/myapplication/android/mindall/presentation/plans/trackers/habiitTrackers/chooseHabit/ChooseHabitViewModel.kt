package myapplication.android.mindall.presentation.plans.trackers.habiitTrackers.chooseHabit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.trackers.HabitTrackerDI
import myapplication.android.mindall.di.trackers.MoodTrackersDI
import myapplication.android.mindall.domain.model.trackers.HabitModel
import myapplication.android.mindall.presentation.plans.trackers.habiitTrackers.model.HabitTitleAndIDModel

class ChooseHabitViewModel : ViewModel() {
    private val _state: MutableLiveData<State<List<HabitTitleAndIDModel>>> =
        MutableLiveData<State<List<HabitTitleAndIDModel>>>(State.loading())
    var state = _state

    private val _habitId: MutableLiveData<String?> = MutableLiveData(null)
    var habitId = _habitId

    private val _yearId: MutableLiveData<String?> = MutableLiveData(null)
    var yearId = _yearId

    private val _monthId: MutableLiveData<String?> = MutableLiveData(null)
    var monthId = _monthId

    private val _isNew: MutableLiveData<Boolean?> = MutableLiveData(null)
    var isNew = _isNew

    fun getHabits(){
        HabitTrackerDI.getHabitsUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<HabitModel>> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get habits", "on subscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {
                    Log.e("Get habits", "on error ${e.message}")
                }

                override fun onSuccess(models: List<HabitModel>) {

                    val habits = mutableListOf<HabitTitleAndIDModel>()

                    for (i in models){
                        habits.add(HabitTitleAndIDModel(i.id, i.habit))
                    }

                    _state.postValue(State.success(habits))
                }

            })
    }

    fun getHabitId(habit: String){
        HabitTrackerDI.getHabitIdUseCase.invoke(habit)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get habit id trackers", "on subscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {
                    if (e.message == NO_PLANS){
                        _isNew.postValue(true)
                    }
                }

                override fun onSuccess(t: String) {
                    _habitId.postValue(t)
                }

            })
    }

    fun getYearId(habitId: String, year: String) {
        HabitTrackerDI.getYearIdUseCase.invoke(habitId, year)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get year id habit trackers", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(id: String) {
                    _yearId.postValue(id)
                }

                override fun onError(e: Throwable) {
                    if (e.message.toString() == NO_PLANS) {
                        _isNew.postValue(true)
                    }
                }
            })
    }

    fun getMonthId(yearId: String, habitId: String, month: String) {
        HabitTrackerDI.getMonthIdUseCase.invoke(yearId, habitId, month)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get month id trackers", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(id: String) {
                    _monthId.postValue(id)
                }

                override fun onError(e: Throwable) {
                    if (e.message.toString() == NO_PLANS) {
                        _isNew.postValue(true)
                    }
                }
            })
    }
}