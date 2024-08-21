package myapplication.android.mindall.presentation.plans.basicPlans.daily.dailyPlanDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.daily.DailyPlansDI
import myapplication.android.mindall.di.daily.DailyScheduleDI
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.DailyPlanDetailsModel
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.ScheduleDayModel
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.TaskDailyPlanModel

class DailyPlanDetailsViewModel : ViewModel() {

    private val _state : MutableLiveData<State<DailyPlanDetailsModel>> = MutableLiveData(State.loading())
    var state = _state

    private val _statusCompletedUpdated: MutableLiveData<Boolean> = MutableLiveData(false)
    var statusCompletedUpdated = _statusCompletedUpdated

    private val _statusNotCompletedUpdated: MutableLiveData<Boolean> = MutableLiveData(false)
    var statusNotCompletedUpdated = _statusNotCompletedUpdated

    fun getDailyPlanData(dailyPlanId: String){
        DailyPlansDI.getDailyTasksUseCase.invoke(dailyPlanId)
            .zipWith(DailyScheduleDI.getDailyScheduleUseCase.invoke(dailyPlanId)) { tasks, schedules ->
                val taskModels: MutableList<TaskDailyPlanModel> = mutableListOf()
                val scheduleModels: MutableList<ScheduleDayModel> = mutableListOf()
                for (i in tasks) {
                    taskModels.add(
                        TaskDailyPlanModel(
                            i.id,
                            i.title,
                            i.isCompleted,
                            i.flag
                        )
                    )
                }
                for (i in schedules) {
                    scheduleModels.add(
                        ScheduleDayModel(
                            i.id,
                            i.duration,
                            i.task,
                            i.isNotificationOn,
                            i.notificationStart
                        )
                    )
                }
                DailyPlanDetailsModel(
                    taskModels,
                    scheduleModels
                )
            }
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<DailyPlanDetailsModel> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Get daily plan data by id", "onSubscribe ${d.isDisposed}")
                }

                override fun onError(e: Throwable) {
                    Log.e("Get daily plan data by id", "onError ${e.message}")
                }

                override fun onSuccess(t: DailyPlanDetailsModel) {
                    _state.value = State.success(t)
                }

            })
    }

    fun changeCompletedTaskStatus(
        planId: String,
        completedTasks: MutableList<String>,
    ) {
        if (completedTasks.isNotEmpty()){
            DailyPlansDI.updateTaskStatusUseCase.invoke(planId, completedTasks, true)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver{
                    override fun onSubscribe(d: Disposable) {
                        Log.i("change completed tasks status", "onSubscribe ${d.isDisposed}")
                    }

                    override fun onComplete() {
                        _statusCompletedUpdated.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.e("change completed tasks status", "onError ${e.message}")
                    }

                })
        } else {
            _statusCompletedUpdated.value = true
        }
    }

    fun changeNotCompletedTaskStatus(
        planId: String,
        notCompletedTasks: MutableList<String>
    ){
        if (notCompletedTasks.isNotEmpty()){
            DailyPlansDI.updateTaskStatusUseCase.invoke(planId, notCompletedTasks, false)
                .subscribeOn(Schedulers.io())
                .subscribe(object : CompletableObserver{
                    override fun onSubscribe(d: Disposable) {
                        Log.i("change not completed tasks status", "onSubscribe ${d.isDisposed}")
                    }

                    override fun onComplete() {
                        _statusNotCompletedUpdated.value = true
                    }

                    override fun onError(e: Throwable) {
                        Log.e("change not completed tasks status", "onError ${e.message}")
                    }

                })
        } else{
            _statusNotCompletedUpdated.value = true
        }
    }
}