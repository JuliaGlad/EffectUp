package myapplication.android.mindall.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.common.Constants.Companion.DAILY_PLANS_LIST
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.DURATION
import myapplication.android.mindall.common.Constants.Companion.FLAG
import myapplication.android.mindall.common.Constants.Companion.GREEN
import myapplication.android.mindall.common.Constants.Companion.GREEN_TASK_COUNT
import myapplication.android.mindall.common.Constants.Companion.IS_COMPLETE
import myapplication.android.mindall.common.Constants.Companion.IS_NOTIFICATION_ON
import myapplication.android.mindall.common.Constants.Companion.NOTIFICATION_START
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.common.Constants.Companion.RED
import myapplication.android.mindall.common.Constants.Companion.RED_TASK_COUNT
import myapplication.android.mindall.common.Constants.Companion.SCHEDULE_DAY_LIST
import myapplication.android.mindall.common.Constants.Companion.TASK
import myapplication.android.mindall.common.Constants.Companion.TASK_LIST
import myapplication.android.mindall.common.Constants.Companion.TITLE
import myapplication.android.mindall.common.Constants.Companion.USER_LIST
import myapplication.android.mindall.common.Constants.Companion.YELLOW
import myapplication.android.mindall.common.Constants.Companion.YELLOW_TASK_COUNT
import myapplication.android.mindall.data.dto.dailyDtos.DailyPlanDto
import myapplication.android.mindall.data.dto.dailyDtos.DayScheduleDto
import myapplication.android.mindall.data.dto.commonDtos.TaskDto
import myapplication.android.mindall.di.DI.Companion.service

class DailyTaskRepository {

    fun getDailyPlanByData(data: String): Single<DailyPlanDto> {
        return Single.create { emitter ->
            service?.auth?.currentUser?.uid?.let {
                service.fireStore
                    .collection(USER_LIST)
                    .document(it)
                    .collection(DAILY_PLANS_LIST)
                    .get().addOnCompleteListener { query ->
                        val doc: List<DocumentSnapshot> = query.result.documents
                        var havePlans = false
                        for (i in doc) {
                            val currentDoc = doc[doc.indexOf(i)]
                            if (currentDoc.getString(DATA) == data) {
                                havePlans = true
                                val dailyPlanDto = DailyPlanDto(
                                    currentDoc.id,
                                    currentDoc.getString(DATA),
                                    currentDoc.getString(GREEN_TASK_COUNT),
                                    currentDoc.getString(YELLOW_TASK_COUNT),
                                    currentDoc.getString(RED_TASK_COUNT)
                                )
                                emitter.onSuccess(dailyPlanDto)
                                break
                            }
                        }
                        if (!havePlans) {
                            emitter.onError(Throwable(NO_PLANS))
                        }
                    }
            }

        }
    }

    private fun increaseGreenTasks(docRef: DocumentReference, emitter: CompletableEmitter) {
        docRef.get().addOnCompleteListener { task ->
            if (task.isComplete) {
                val greenTask = task.result.getString(GREEN_TASK_COUNT)
                docRef.update(
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

    private fun increaseYellowTasks(docRef: DocumentReference, emitter: CompletableEmitter) {
        docRef.get().addOnCompleteListener { task ->
            if (task.isComplete) {
                val yellowTask = task.result.getString(YELLOW_TASK_COUNT)
                docRef.update(
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

    private fun increaseRedTasks(docRef: DocumentReference, emitter: CompletableEmitter) {
        docRef.get().addOnCompleteListener { task ->
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

    class Tasks {
        fun getTasks(dailyPlanId: String): Single<List<TaskDto>> {
            return Single.create { emitter ->
                service?.auth?.currentUser?.uid?.let {
                    service.fireStore
                        .collection(USER_LIST)
                        .document(it)
                        .collection(DAILY_PLANS_LIST)
                        .document(dailyPlanId)
                        .collection(TASK_LIST).let { collRef ->
                            collRef.get().addOnCompleteListener { query ->
                                if (query.isComplete) {
                                    val docs: MutableList<DocumentSnapshot> =
                                        query.result.documents
                                    val dtos: MutableList<TaskDto> = mutableListOf()
                                    for (currentDoc in docs) {
                                        dtos.add(
                                            TaskDto(
                                                currentDoc.id,
                                                currentDoc.getString(TITLE),
                                                currentDoc.getBoolean(IS_COMPLETE),
                                                currentDoc.getString(FLAG)
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

        fun addTask(
            isNew: Boolean?,
            data: String,
            planId: String,
            taskId: String,
            title: String,
            flag: String
        ): Completable {
            return Completable.create { emitter ->
                if (!isNew!!) {
                    createTask(planId, taskId, title, flag, emitter)
                } else {
                    service?.auth?.currentUser?.uid?.let { uid ->
                        createDailyPlan(planId, data, uid)?.addOnCompleteListener { task ->
                            if (task.isComplete) {
                                createTask(planId, taskId, title, flag, emitter)
                            }
                        }
                    }
                }
            }
        }

        fun editTask(
            planId: String,
            taskId: String,
            title: String,
            flag: String
        ): Completable {
            return Completable.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    val docRef: DocumentReference = service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(DAILY_PLANS_LIST)
                        .document(planId)
                        .collection(TASK_LIST)
                        .document(taskId)
                    docRef.update(
                        TITLE, title,
                        FLAG, flag
                    ).addOnCompleteListener { update ->
                        if (update.isComplete) {
                            emitter.onComplete()
                        }
                    }
                }
            }
        }

        fun deleteTask(
            planId: String,
            taskId: String
        ): Completable {
            return Completable.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    val docRef: DocumentReference = service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(DAILY_PLANS_LIST)
                        .document(planId)
                        .collection(TASK_LIST)
                        .document(taskId)
                    docRef.delete().addOnCompleteListener { delete ->
                        if (delete.isComplete) {
                            emitter.onComplete()
                        }
                    }
                }
            }
        }

        fun updateListTasksStatus(
            planId: String,
            tasks: List<String>,
            status: Boolean
        ): Completable {
            return Completable.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    val collection: CollectionReference = service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(DAILY_PLANS_LIST)
                        .document(planId)
                        .collection(TASK_LIST)
                    for (current in tasks) {
                        val docRef: DocumentReference = collection.document(current)
                        docRef.update(IS_COMPLETE, status).addOnCompleteListener { update ->
                            if (update.isComplete) {
                                emitter.onComplete()
                            }
                        }
                    }
                }
            }
        }

        private fun createDailyPlan(
            planId: String,
            data: String,
            uid: String,
        ): Task<Void>? {


            val planDocument = service?.fireStore
                ?.collection(USER_LIST)
                ?.document(uid)
                ?.collection(DAILY_PLANS_LIST)
                ?.document(planId)

            val map: HashMap<String, String> = HashMap<String, String>().apply {
                put(DATA, data)
                put(GREEN_TASK_COUNT, "0")
                put(YELLOW_TASK_COUNT, "0")
                put(RED_TASK_COUNT, "0")
            }

            return planDocument?.set(map)

        }

        private fun createTask(
            planId: String,
            taskId: String,
            title: String,
            flag: String,
            emitter: CompletableEmitter
        ) {
            service?.auth?.currentUser?.uid?.let { uid ->
                val planDocument = service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(DAILY_PLANS_LIST)
                    .document(planId)

                val docRef = planDocument
                    .collection(TASK_LIST)
                    .document(taskId)

                val task: HashMap<String, Any> = HashMap<String, Any>()
                    .apply {
                        put(TITLE, title)
                        put(IS_COMPLETE, false)
                        put(FLAG, flag)
                    }
                docRef.set(task).addOnCompleteListener { taskAdd ->
                    if (taskAdd.isSuccessful) {
                        when (flag) {
                            GREEN -> DailyTaskRepository().increaseGreenTasks(planDocument, emitter)
                            YELLOW -> DailyTaskRepository().increaseYellowTasks(planDocument, emitter)
                            RED -> DailyTaskRepository().increaseRedTasks(planDocument, emitter)
                        }
                    }
                }
            }
        }

    }

    class DaySchedule {
        fun getSchedules(dailyPlanId: String): Single<List<DayScheduleDto>> {
            return Single.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(DAILY_PLANS_LIST)
                        .document(dailyPlanId)
                        .collection(SCHEDULE_DAY_LIST)
                        .get().addOnCompleteListener { query ->
                            if (query.isComplete) {
                                val documents = query.result.documents
                                val dtos: MutableList<DayScheduleDto> = mutableListOf()
                                for (i in documents) {
                                    dtos.add(
                                        DayScheduleDto(
                                            i.id,
                                            i.getString(DURATION),
                                            i.getString(TASK),
                                            i.getBoolean(IS_NOTIFICATION_ON),
                                            i.getString(NOTIFICATION_START)
                                        )
                                    )
                                }
                                emitter.onSuccess(dtos)
                            }
                        }
                }
            }
        }

        fun addSchedule(
            dailyPlanId: String,
            scheduleId: String,
            task: String?,
            duration: String,
            isNotificationOn: Boolean,
            notificationStart: String?
        ): Completable {
            return Completable.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    val docRef = service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(DAILY_PLANS_LIST)
                        .document(dailyPlanId)
                        .collection(SCHEDULE_DAY_LIST)
                        .document(scheduleId)

                    val schedule: HashMap<String, Any> = HashMap<String, Any>().apply {
                        put(DURATION, duration)
                        task?.let { put(TASK, it) }
                        put(IS_NOTIFICATION_ON, isNotificationOn)
                        notificationStart?.let { put(NOTIFICATION_START, it) }
                    }

                    docRef.set(schedule).addOnCompleteListener { task ->
                        if (task.isComplete) {
                            emitter.onComplete()
                        }
                    }
                }
            }
        }

        fun editSchedule(
            dailyPlanId: String,
            scheduleId: String,
            task: String,
            duration: String,
            isNotificationOn: Boolean,
            notificationStart: String
        ): Completable {
            return Completable.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(DAILY_PLANS_LIST)
                        .document(dailyPlanId)
                        .collection(SCHEDULE_DAY_LIST)
                        .document(scheduleId)
                        .update(
                            DURATION, duration,
                            TASK, task,
                            IS_NOTIFICATION_ON, isNotificationOn,
                            NOTIFICATION_START, notificationStart
                        )
                        .addOnCompleteListener { task ->
                            if (task.isComplete) {
                                emitter.onComplete()
                            }
                        }
                }
            }
        }

        fun deleteSchedule(
            dailyPlanId: String,
            scheduleId: String
        ): Completable {
            return Completable.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(DAILY_PLANS_LIST)
                        .document(dailyPlanId)
                        .collection(SCHEDULE_DAY_LIST)
                        .document(scheduleId)
                        .delete().addOnCompleteListener { task ->
                            if (task.isComplete) {
                                emitter.onComplete()
                            }
                        }
                }
            }
        }
    }
}
