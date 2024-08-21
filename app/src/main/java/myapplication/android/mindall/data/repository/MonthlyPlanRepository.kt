package myapplication.android.mindall.data.repository

import com.google.android.gms.tasks.Task
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.common.Constants.Companion.FLAG
import myapplication.android.mindall.common.Constants.Companion.GOAL
import myapplication.android.mindall.common.Constants.Companion.GOALS_LIST
import myapplication.android.mindall.common.Constants.Companion.IS_COMPLETE
import myapplication.android.mindall.common.Constants.Companion.MONTH
import myapplication.android.mindall.common.Constants.Companion.MONTHLY_PLANS_LIST
import myapplication.android.mindall.common.Constants.Companion.MONTH_LIST
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.common.Constants.Companion.TASK_LIST
import myapplication.android.mindall.common.Constants.Companion.TITLE
import myapplication.android.mindall.common.Constants.Companion.USER_LIST
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.data.dto.commonDtos.TaskDto
import myapplication.android.mindall.data.dto.monthlyDto.GoalDto
import myapplication.android.mindall.di.DI.Companion.service

class MonthlyPlanRepository {

    fun getYearId(year: String): Single<String> {
        return Single.create { emitter ->
            service?.auth?.uid?.let { uid ->
                service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(MONTHLY_PLANS_LIST)
                    .get().addOnCompleteListener { task ->
                        if (task.isComplete) {
                            val documents = task.result.documents
                            var isFound = false
                            for (i in documents) {
                                if (i.getString(YEAR) == year) {
                                    isFound = true
                                    emitter.onSuccess(i.id)
                                }
                            }
                            if (!isFound) {
                                emitter.onError(Throwable(NO_PLANS))
                            }
                        }
                    }
            }
        }
    }

    fun getMonthId(
        month: String,
        yearId: String
    ): Single<String> {
        return Single.create { emitter ->
            service?.auth?.uid?.let { uid ->
                service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(MONTHLY_PLANS_LIST)
                    .document(yearId)
                    .collection(MONTH_LIST)
                    .get().addOnCompleteListener { task ->
                        if (task.isComplete) {
                            val documents = task.result.documents
                            var isFound = false
                            for (i in documents) {
                                if (i.getString(MONTH) == month) {
                                    isFound = true
                                    emitter.onSuccess(i.id)
                                }
                            }
                            if (!isFound) {
                                emitter.onError(Throwable(NO_PLANS))
                            }
                        }
                    }
            }
        }
    }

    class MonthlyTasks {

        fun getMonthlyTasks(
            yearId: String,
            monthId: String
        ): Single<List<TaskDto>> {
            return Single.create { emitter ->
                service?.auth?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(MONTHLY_PLANS_LIST)
                        .document(yearId)
                        .collection(MONTH_LIST)
                        .document(monthId)
                        .collection(TASK_LIST)
                        .get().addOnCompleteListener { task ->
                            if (task.isComplete) {
                                val documents = task.result.documents
                                val tasksDtos = mutableListOf<TaskDto>()
                                for (i in documents) {
                                    tasksDtos.add(
                                        TaskDto(
                                            i.id,
                                            i.getString(TITLE),
                                            i.getBoolean(IS_COMPLETE),
                                            i.getString(FLAG)
                                        )
                                    )
                                }
                                emitter.onSuccess(tasksDtos)
                            }
                        }
                }
            }
        }

        fun updateMonthlyTaskStatus(
            yearId: String,
            monthId: String,
            tasks: List<String>,
            status: Boolean
        ): Completable {
            return Completable.create { emitter ->
                service?.auth?.uid?.let { uid ->
                    val taskCollection = service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(MONTHLY_PLANS_LIST)
                        .document(yearId)
                        .collection(MONTH_LIST)
                        .document(monthId)
                        .collection(TASK_LIST)

                    for (i in tasks) {
                        taskCollection.document(i).update(IS_COMPLETE, status)
                    }
                    emitter.onComplete()
                }
            }
        }

        fun addMonthlyTask(
            isNew: Boolean,
            yearId: String,
            year: String,
            monthId: String,
            month: String,
            taskId: String,
            title: String,
            flag: String
        ): Completable {
            return Completable.create { emitter ->
                if (!isNew) {
                    MonthlyPlanRepository().createMonthlyTaskDocument(
                        yearId,
                        monthId,
                        taskId,
                        title,
                        flag
                    )
                        ?.addOnCompleteListener { task ->
                            if (task.isComplete) {
                                emitter.onComplete()
                            }
                        }
                } else {
                    MonthlyPlanRepository().createMonthYearAndTaskDocuments(
                        yearId,
                        year,
                        monthId,
                        month,
                        taskId,
                        title,
                        flag,
                        emitter
                    )
                }
            }
        }
    }

    class MonthlyGoals {
        fun getMonthlyGoals(
            yearId: String,
            monthId: String
        ): Single<List<GoalDto>> {
            return Single.create { emitter ->
                service?.auth?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(MONTHLY_PLANS_LIST)
                        .document(yearId)
                        .collection(MONTH_LIST)
                        .document(monthId)
                        .collection(GOALS_LIST)
                        .get().addOnCompleteListener { task ->
                            if (task.isComplete) {
                                val documents = task.result.documents
                                val goals = mutableListOf<GoalDto>()
                                for (i in documents) {
                                    goals.add(GoalDto(i.id, i.getString(GOAL).toString()))
                                }
                                emitter.onSuccess(goals)
                            }
                        }
                }
            }
        }


        fun addMonthlyGoal(
            isNew: Boolean,
            yearId: String,
            year: String,
            monthId: String,
            month: String,
            goalId: String,
            goal: String
        ): Completable {
            return Completable.create { emitter ->
                if (!isNew) {
                    MonthlyPlanRepository().createMonthlyGoalDocument(yearId, monthId, goalId, goal)
                        ?.addOnCompleteListener { task ->
                            if (task.isComplete) {
                                emitter.onComplete()
                            }
                        }
                } else {
                    MonthlyPlanRepository().createMonthYearAndGoalDocuments(
                        yearId,
                        year,
                        monthId,
                        month,
                        goalId,
                        goal,
                        emitter
                    )
                }
            }
        }
    }

    private fun createMonthYearAndGoalDocuments(
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        goalId: String,
        goal: String,
        emitter: CompletableEmitter
    ) {
        createYearDocument(yearId, year)?.addOnCompleteListener { task ->
            if (task.isComplete) {
                createMonthDocument(yearId, monthId, month)?.addOnCompleteListener { taskMonth ->
                    if (taskMonth.isComplete) {
                        createMonthlyGoalDocument(yearId, monthId, goalId, goal)
                            ?.addOnCompleteListener { addTask ->
                                if (addTask.isComplete) {
                                    emitter.onComplete()
                                }
                            }
                    }
                }
            }
        }
    }

    private fun createMonthlyGoalDocument(
        yearId: String,
        monthId: String,
        goalId: String,
        goal: String
    ): Task<Void>? {
        val goalDocument = service?.auth?.uid?.let { uid ->
            service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(MONTHLY_PLANS_LIST)
                .document(yearId)
                .collection(MONTH_LIST)
                .document(monthId)
                .collection(GOALS_LIST)
                .document(goalId)
        }

        val map = HashMap<String, Any>().apply {
            put(GOAL, goal)
        }

        return goalDocument?.set(map)
    }

    private fun createMonthYearAndTaskDocuments(
        yearId: String,
        year: String,
        monthId: String,
        month: String,
        taskId: String,
        title: String,
        flag: String,
        emitter: CompletableEmitter
    ) {
        createYearDocument(yearId, year)?.addOnCompleteListener { task ->
            if (task.isComplete) {
                createMonthDocument(yearId, monthId, month)?.addOnCompleteListener { taskMonth ->
                    if (taskMonth.isComplete) {
                        createMonthlyTaskDocument(yearId, monthId, taskId, title, flag)
                            ?.addOnCompleteListener { addTask ->
                                if (addTask.isComplete) {
                                    emitter.onComplete()
                                }
                            }
                    }
                }
            }
        }
    }

    private fun createMonthDocument(
        yearId: String,
        monthId: String,
        month: String
    ): Task<Void>? {
        val monthDocument = service?.auth?.uid?.let { uid ->
            service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(MONTHLY_PLANS_LIST)
                .document(yearId)
                .collection(MONTH_LIST)
                .document(monthId)
        }
        val map = HashMap<String, Any>().apply {
            put(MONTH, month)
        }
        return monthDocument?.set(map)
    }

    private fun createYearDocument(
        yearId: String,
        year: String
    ): Task<Void>? {
        val yearDocument = service?.auth?.uid?.let { uid ->
            service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(MONTHLY_PLANS_LIST)
                .document(yearId)
        }

        val map = HashMap<String, Any>().apply {
            put(YEAR, year)
        }
        return yearDocument?.set(map)
    }

    private fun createMonthlyTaskDocument(
        yearId: String,
        monthId: String,
        taskId: String,
        title: String,
        flag: String
    ): Task<Void>? {
        val taskDocument = service?.auth?.uid?.let { uid ->
            service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(MONTHLY_PLANS_LIST)
                .document(yearId)
                .collection(MONTH_LIST)
                .document(monthId)
                .collection(TASK_LIST)
                .document(taskId)
        }

        val map = HashMap<String, Any>().apply {
            put(TITLE, title)
            put(FLAG, flag)
            put(IS_COMPLETE, false)
        }

        return taskDocument?.set(map)
    }

}