package myapplication.android.mindall.presentation.plans.trackers.habiitTrackers.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.trackers.HabitTrackerDI
import myapplication.android.mindall.domain.model.trackers.HabitTrackersModel
import myapplication.android.mindall.presentation.plans.trackers.habiitTrackers.model.HabitPresModel

class HabitTrackersViewModel : ViewModel() {

    private val _state = MutableLiveData<State<List<HabitPresModel>>>(State.loading())
    var state = _state

    fun getTrackers(
        yearId: String,
        monthId: String,
        habitId: String
    ){
        HabitTrackerDI.getMonthHabitTrackerUseCase.invoke(yearId, monthId, habitId)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<HabitTrackersModel>> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("On subscribe get trackers", "${d.isDisposed}")
                }

                override fun onSuccess(models: List<HabitTrackersModel>) {
                    val items = mutableListOf<HabitPresModel>()
                    for (i in models){
                        items.add(
                            HabitPresModel(
                            i.id,
                            i.date,
                            i.isCompleted
                        )
                        )
                    }
                    _state.postValue(State.success(items))
                }

                override fun onError(e: Throwable) {
                    Log.e("On error get trackers", "${e.message}")
                }
            })
    }

    fun setStateSuccess(){
        _state.postValue(State.success(emptyList()))
    }
}