package myapplication.android.mindall.presentation.plans.trackers.moodTrackers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.trackers.MoodTrackersDI
import myapplication.android.mindall.domain.model.trackers.TrackerModel
import myapplication.android.mindall.presentation.plans.trackers.model.TrackersPresModel

class MoodTrackersViewModel : ViewModel() {
    private val _state: MutableLiveData<State<List<TrackersPresModel>>> =
        MutableLiveData<State<List<TrackersPresModel>>>(State.loading())
    var state = _state

    private val _yearId: MutableLiveData<String?> = MutableLiveData(null)
    var yearId = _yearId

    private val _monthId: MutableLiveData<String?> = MutableLiveData(null)
    var monthId = _monthId

    private val _isNew: MutableLiveData<Boolean?> = MutableLiveData(null)
    var isNew = _isNew

    fun getYearId(year: String) {
        MoodTrackersDI.getYearIdMoodTrackerUseCase.invoke(year)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get year id trackers", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(id: String) {
                    _yearId.postValue(id)
                }

                override fun onError(e: Throwable) {
                    if (e.message.toString() == Constants.NO_PLANS) {
                        _isNew.postValue(true)
                    }
                }
            })
    }

    fun getMonthTrackers(yearId: String, monthId: String) {
        MoodTrackersDI.getMoodTrackerUseCase.invoke(yearId, monthId)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<TrackerModel>> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get month night trackers", "on subscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {
                    Log.e("Get month night trackers", e.message.toString())
                }

                override fun onSuccess(models: List<TrackerModel>) {
                    val trackers = mutableListOf<TrackersPresModel>()
                    for (i in models) {
                        trackers.add(
                            TrackersPresModel(
                                i.id,
                                i.value,
                                i.date
                            )
                        )
                    }
                    _state.postValue(State.success(trackers))
                }

            })
    }

    fun setSuccessStateWithEmptyList(){
        _state.postValue(State.success(emptyList()))
    }

    fun getMonthId(yearId: String, month: String) {
        MoodTrackersDI.getMonthIdUseCase.invoke(yearId, month)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get month id trackers", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(id: String) {
                    _monthId.postValue(id)
                }

                override fun onError(e: Throwable) {
                    if (e.message.toString() == Constants.NO_PLANS) {
                        _isNew.postValue(true)
                    }
                }
            })
    }
}