package myapplication.android.mindall.presentation.plans.basicPlans.monthly.monthPlanDetails

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.monthly.MonthlyGoalsDI
import myapplication.android.mindall.di.monthly.MonthlyPlansDI
import myapplication.android.mindall.domain.model.common.TaskMainModel
import myapplication.android.mindall.domain.model.monthly.GoalModel
import myapplication.android.mindall.presentation.plans.basicPlans.daily.model.TaskDailyPlanModel
import myapplication.android.mindall.presentation.plans.basicPlans.monthly.model.GoalAndTaskModel
import myapplication.android.mindall.presentation.plans.basicPlans.monthly.model.GoalPresModel

class MonthPlanDetailsViewModel : ViewModel() {

    private val _state: MutableLiveData<State<GoalAndTaskModel>> = MutableLiveData(State.loading())
    var state = _state

    private val _notCompletedTasksUpdated = MutableLiveData(false)
    var notCompletedTasksUpdated = _notCompletedTasksUpdated

    private val _completedTasksUpdated = MutableLiveData(false)
    var completedTasksUpdated = _completedTasksUpdated

    fun getTasksAndGoals(yearId: String, monthId: String) {
        MonthlyPlansDI.getMonthlyTaskUseCase.invoke(yearId, monthId)
            .zipWith(
                MonthlyGoalsDI.getMonthlyGoalsUseCase.invoke(yearId, monthId)
            ) { tasks, goals ->
                val tasksList = mutableListOf<TaskDailyPlanModel>()
                getTasks(tasks, tasksList)

                val goalsList = mutableListOf<GoalPresModel>()
                getGoals(goals, goalsList)

                val goalAndTaskModel = GoalAndTaskModel(tasksList, goalsList)
                goalAndTaskModel
            }
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<GoalAndTaskModel> {
                override fun onSubscribe(d: Disposable) {
                  Log.i("Get monthly tasks and goals ", "on subscribe ${d.isDisposed}")
                }

                override fun onSuccess(model: GoalAndTaskModel) {
                    _state.postValue(State.success(model))
                }

                override fun onError(e: Throwable) {
                    Log.i("Get monthly tasks and goals ", "on error ${e.message}")
                }
            })

    }

    private fun getTasks(
        tasks: List<TaskMainModel>,
        tasksList: MutableList<TaskDailyPlanModel>
    ) {
        for (i in tasks) {
            tasksList.add(
                TaskDailyPlanModel(
                    i.id,
                    i.title,
                    i.isCompleted,
                    i.flag
                )
            )
        }
    }

    private fun getGoals(
        goals: List<GoalModel>,
        goalsList: MutableList<GoalPresModel>
    ) {
        for (i in goals) {
            goalsList.add(
                GoalPresModel(
                    i.id,
                    i.goal
                )
            )
        }
    }

    fun updateStatusCompleted(
        yearId: String,
        monthId: String,
        tasks: List<String>,
    ) {
        MonthlyPlansDI.updateMonthlyTaskStatusUseCase.invoke(yearId, monthId, tasks, true)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
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
        yearId: String,
        monthId: String,
        tasks: List<String>,
    ) {
        MonthlyPlansDI.updateMonthlyTaskStatusUseCase.invoke(yearId, monthId, tasks, false)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
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