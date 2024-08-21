package myapplication.android.mindall.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.DAYS
import myapplication.android.mindall.common.Constants.Companion.DAY_OF_WEEK
import myapplication.android.mindall.common.Constants.Companion.FLAG
import myapplication.android.mindall.common.Constants.Companion.GREEN_TASK_COUNT
import myapplication.android.mindall.common.Constants.Companion.IS_COMPLETE
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.common.Constants.Companion.RED_TASK_COUNT
import myapplication.android.mindall.common.Constants.Companion.TASK_LIST
import myapplication.android.mindall.common.Constants.Companion.TITLE
import myapplication.android.mindall.common.Constants.Companion.USER_LIST
import myapplication.android.mindall.common.Constants.Companion.WEEKLY_PLANS_LIST
import myapplication.android.mindall.common.Constants.Companion.YELLOW_TASK_COUNT
import myapplication.android.mindall.data.dto.weeklyDtos.DayOfWeekPlansDto
import myapplication.android.mindall.data.dto.commonDtos.TaskDto
import myapplication.android.mindall.di.DI.Companion.service
import myapplication.android.mindall.presentation.plans.basicPlans.weekly.model.DayOfWeekPresModel

class WeeklyPlansRepository {

    fun getWeeklyPlans(date: String): Single<String> {
        return Single.create { emitter ->
            service?.auth?.currentUser?.uid?.let { uid ->
                service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(WEEKLY_PLANS_LIST)
                    .get().addOnCompleteListener { task ->
                        if (task.isComplete) {
                            val documents: List<DocumentSnapshot> = task.result.documents
                            var isFound = false
                            for (i in documents) {
                                if (i.getString(DATA) == date) {
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

    class DaysOfWeekPlans {

        fun getDaysOfWeek(weekPlansId: String?): Single<List<DayOfWeekPlansDto>> {
            return Single.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    if (weekPlansId != null) {
                        service.fireStore
                            .collection(USER_LIST)
                            .document(uid)
                            .collection(WEEKLY_PLANS_LIST)
                            .document(weekPlansId)
                            .collection(DAYS)
                            .get().addOnCompleteListener { task ->
                                if (task.isComplete) {
                                    val documents: List<DocumentSnapshot> = task.result.documents
                                    val days = mutableListOf<DayOfWeekPlansDto>()
                                    for (i in documents) {
                                        days.add(
                                            DayOfWeekPlansDto(
                                                i.id,
                                                i.getString(DAY_OF_WEEK),
                                                i.getString(DATA),
                                                i.getString(GREEN_TASK_COUNT),
                                                i.getString(YELLOW_TASK_COUNT),
                                                i.getString(GREEN_TASK_COUNT)
                                            )
                                        )
                                    }
                                    emitter.onSuccess(days)
                                }
                            }
                    }
                }
            }
        }

        fun getDayOfWeekTasks(
            weekPlansId: String?,
            dayOfWeekId: String?
        ): Single<List<TaskDto>> {
            return Single.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    if (weekPlansId != null && dayOfWeekId != null) {
                        service.fireStore
                            .collection(USER_LIST)
                            .document(uid)
                            .collection(WEEKLY_PLANS_LIST)
                            .document(weekPlansId)
                            .collection(DAYS)
                            .document(dayOfWeekId)
                            .collection(TASK_LIST)
                            .get().addOnCompleteListener { task ->
                                if (task.isComplete) {
                                    val documents: List<DocumentSnapshot> = task.result.documents
                                    val dtos = mutableListOf<TaskDto>()
                                    for (i in documents) {
                                        dtos.add(
                                            TaskDto(
                                                i.id,
                                                i.getString(TITLE),
                                                i.getBoolean(IS_COMPLETE),
                                                i.getString(FLAG)
                                            )
                                        )
                                    }
                                    emitter.onSuccess(dtos)
                                }
                            }
                    }
                }
            }
        }

        fun updateTaskStatus(
            weekPlansId: String,
            dayOfWeekId: String,
            tasks: List<String>,
            status: Boolean
        ): Completable {
            return Completable.create { emitter ->
                val collection = service?.auth?.currentUser?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(WEEKLY_PLANS_LIST)
                        .document(weekPlansId)
                        .collection(DAYS)
                        .document(dayOfWeekId)
                        .collection(TASK_LIST)
                }
                for (current in tasks) {
                    Log.i("Current id", current)
                    val docRef: DocumentReference? = collection?.document(current)
                    docRef?.update(IS_COMPLETE, status)
                }
                emitter.onComplete()
            }
        }

        fun addDayOfWeeksPlans(
            isNew: Boolean?,
            duration: String,
            days: List<DayOfWeekPresModel>,
            weeklyPlanId: String,
            dayOfWeekId: String,
            taskId: String,
            title: String,
            flag: String
        ): Completable {
            return Completable.create { emitter ->

                service?.auth?.currentUser?.uid.let { uid ->
                    if (!isNew!!) {
                        addDayOfWeekTask(
                            weeklyPlanId,
                            dayOfWeekId,
                            uid,
                            taskId,
                            title,
                            flag,
                            emitter
                        )
                    } else {
                        createWeeklyPlan(weeklyPlanId, duration, uid)
                            ?.addOnCompleteListener { taskWeek ->
                                if (taskWeek.isComplete) {
                                    val planDocument = uid?.let {
                                        service?.fireStore
                                            ?.collection(USER_LIST)
                                            ?.document(it)
                                            ?.collection(WEEKLY_PLANS_LIST)
                                            ?.document(weeklyPlanId)
                                    }
                                    if (planDocument != null) {
                                        setDaysOfWeek(days, planDocument)
                                    }
                                    addDayOfWeekTask(
                                        weeklyPlanId,
                                        dayOfWeekId,
                                        uid,
                                        taskId,
                                        title,
                                        flag,
                                        emitter
                                    )
                                }
                            }
                    }
                }
            }
        }

        private fun addDayOfWeekTask(
            weeklyPlanId: String,
            dayOfWeekId: String,
            uid: String?,
            taskId: String,
            title: String,
            flag: String,
            emitter: CompletableEmitter
        ) {
            val taskDocument = getTaskDocument(uid, weeklyPlanId, dayOfWeekId, taskId)
            val weeklyPlanDocument = getWeeklyDocument(uid, weeklyPlanId, dayOfWeekId)
            val map = HashMap<String, Any>().apply {
                put(TITLE, title)
                put(FLAG, flag)
                put(IS_COMPLETE, false)
            }
            increaseTasks(taskDocument, map, flag, weeklyPlanDocument, emitter)
        }

        private fun increaseTasks(
            taskDocument: DocumentReference?,
            map: HashMap<String, Any>,
            flag: String,
            weeklyPlanDocument: DocumentReference?,
            emitter: CompletableEmitter
        ) {
            taskDocument?.set(map)?.addOnCompleteListener { task ->
                if (task.isComplete) {
                    when (flag) {
                        Constants.GREEN -> WeeklyPlansRepository().increaseGreenTasks(
                            weeklyPlanDocument,
                            emitter
                        )

                        Constants.YELLOW -> WeeklyPlansRepository().increaseYellowTasks(
                            weeklyPlanDocument,
                            emitter
                        )

                        Constants.RED -> WeeklyPlansRepository().increaseRedTasks(
                            weeklyPlanDocument,
                            emitter
                        )
                    }
                }
            }
        }

        private fun getTaskDocument(
            uid: String?,
            weeklyPlanId: String,
            dayOfWeekId: String,
            taskId: String
        ) = getWeeklyDocument(uid, weeklyPlanId, dayOfWeekId)
            ?.collection(TASK_LIST)
            ?.document(taskId)

        private fun getWeeklyDocument(
            uid: String?,
            weeklyPlanId: String,
            dayOfWeekId: String
        ) = uid?.let {
            service?.fireStore
                ?.collection(USER_LIST)
                ?.document(it)
                ?.collection(WEEKLY_PLANS_LIST)
                ?.document(weeklyPlanId)
                ?.collection(DAYS)
                ?.document(dayOfWeekId)
        }

        private fun createWeeklyPlan(
            weeklyPlanId: String,
            duration: String,
            uid: String?
        ): Task<Void>? {
            val planDocument = uid?.let {
                service?.fireStore
                    ?.collection(USER_LIST)
                    ?.document(it)
                    ?.collection(WEEKLY_PLANS_LIST)
                    ?.document(weeklyPlanId)
            }

            val map = HashMap<String, String>().apply {
                put(DATA, duration)
            }

            return planDocument?.set(map)
        }

        private fun setDaysOfWeek(
            days: List<DayOfWeekPresModel>,
            planDocument: DocumentReference
        ) {

            for (day in days) {
                val dayOfWeekDoc = planDocument.collection(DAYS).document(day.id)

                val dayOfWeekMap = HashMap<String, String>().apply {
                    day.date?.let { put(DATA, it) }
                    day.dayOfWeek?.let { put(DAY_OF_WEEK, it) }
                    put(GREEN_TASK_COUNT, "0")
                    put(YELLOW_TASK_COUNT, "0")
                    put(RED_TASK_COUNT, "0")
                }

                dayOfWeekDoc.set(dayOfWeekMap)
            }
        }
    }

    private fun increaseRedTasks(
        docRef: DocumentReference?,
        emitter: CompletableEmitter
    ) {
        docRef?.get()?.addOnCompleteListener { task ->
            if (task.isComplete) {
                val redTask = task.result.getString(RED_TASK_COUNT)
                docRef.update(
                    RED_TASK_COUNT,
                    redTask?.let { (Integer.parseInt(it) + 1).toString() })
                    .addOnCompleteListener { update ->
                        if (update.isComplete) {
                            emitter.onComplete()
                        }
                    }
            }
        }
    }

    private fun increaseYellowTasks(
        weeklyPlanDocument: DocumentReference?,
        emitter: CompletableEmitter
    ) {
        weeklyPlanDocument?.get()?.addOnCompleteListener { task ->
            if (task.isComplete) {
                val yellowTask = task.result.getString(YELLOW_TASK_COUNT)
                weeklyPlanDocument.update(
                    YELLOW_TASK_COUNT,
                    yellowTask?.let { (Integer.parseInt(it) + 1).toString() })
                    .addOnCompleteListener { update ->
                        if (update.isComplete) {
                            emitter.onComplete()
                        }
                    }
            }
        }
    }

    private fun increaseGreenTasks(
        weeklyPlanDocument: DocumentReference?,
        emitter: CompletableEmitter
    ) {
        weeklyPlanDocument?.get()?.addOnCompleteListener { task ->
            if (task.isComplete) {
                val greenTask = task.result.getString(GREEN_TASK_COUNT)
                weeklyPlanDocument.update(
                    GREEN_TASK_COUNT,
                    greenTask?.let { (Integer.parseInt(it) + 1).toString() })
                    .addOnCompleteListener { update ->
                        if (update.isComplete) {
                            emitter.onComplete()
                        }
                    }
            }
        }
    }

}