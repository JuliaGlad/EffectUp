package myapplication.android.mindall.presentation.plans.basicPlans.weekly.weeklyPlansDetails.taskDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.logger.Logger
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.weekly.WeeklyPlansDI
import myapplication.android.mindall.domain.model.common.TaskMainModel
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.TaskDailyPlanModel
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekPresModel

class WeeklyTaskDetailsViewModel : ViewModel() {

    private val _completedTasksUpdated : MutableLiveData<Boolean> = MutableLiveData(false)
    var completedTasksUpdated = _completedTasksUpdated

    private val _notCompletedTasksUpdated : MutableLiveData<Boolean> = MutableLiveData(false)
    var notCompletedTasksUpdated = _notCompletedTasksUpdated

    private val _state: MutableLiveData<State<List<TaskDailyPlanModel>>> = MutableLiveData(State.loading())
    var state = _state

    fun getTasks(weeklyPlanId: String?, dayId: String?) {
        WeeklyPlansDI.getDayOfWeekTaskUseCase.invoke(weeklyPlanId, dayId)
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<List<TaskMainModel>> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("get weekly tasks", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(tasks: List<TaskMainModel>) {
                    val tasksModel = mutableListOf<TaskDailyPlanModel>()
                    for (i in tasks) {
                        tasksModel.add(
                            TaskDailyPlanModel(
                                i.id,
                                i.title,
                                i.isCompleted,
                                i.flag
                            )
                        )
                    }
                    _state.value = State.success(tasksModel)
                }

                override fun onError(e: Throwable) {
                    Log.e("get weekly tasks", "on error ${e.message}")
                }
            })
    }

    fun updateStatusCompleted(
        weekPlansId: String,
        dayOfWeekId: String,
        tasks: List<String>,
    ) {
        WeeklyPlansDI.updateTaskStatusUseCase.invoke(weekPlansId, dayOfWeekId, tasks, true)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("Update status weekly", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                   _completedTasksUpdated.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.e("Update status weekly", "on error ${e.message}")
                }

            })
    }

    fun updateStatusNotCompleted(
        weekPlansId: String,
        dayOfWeekId: String,
        tasks: List<String>,
    ) {
        WeeklyPlansDI.updateTaskStatusUseCase.invoke(weekPlansId, dayOfWeekId, tasks, false)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("Update status weekly", "on subscribe ${d.isDisposed}")
                }

                override fun onComplete() {
                    _notCompletedTasksUpdated.postValue(true)
                }

                override fun onError(e: Throwable) {
                    Log.e("Update status weekly", "on error ${e.message}")
                }

            })
    }


}