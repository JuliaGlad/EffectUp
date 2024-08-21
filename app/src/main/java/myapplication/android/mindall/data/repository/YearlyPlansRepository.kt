package myapplication.android.mindall.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.EVENT
import myapplication.android.mindall.common.Constants.Companion.GOAL
import myapplication.android.mindall.common.Constants.Companion.GOALS_LIST
import myapplication.android.mindall.common.Constants.Companion.IMPORTANT_DATES_LIST
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.common.Constants.Companion.USER_LIST
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.common.Constants.Companion.YEARLY_PLANS_LIST
import myapplication.android.mindall.data.dto.monthlyDto.GoalDto
import myapplication.android.mindall.data.dto.yearly.ImportantDateDto
import myapplication.android.mindall.di.DI.Companion.service

class YearlyPlansRepository {

    fun getYearId(year: String): Single<String> {
        return Single.create { emitter ->
            service?.auth?.currentUser?.uid?.let { uid ->
                service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(YEARLY_PLANS_LIST)
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

    class Goals {

        fun getGoals(yearId: String): Single<List<GoalDto>> {
            return Single.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(YEARLY_PLANS_LIST)
                        .document(yearId)
                        .collection(GOALS_LIST)
                        .get().addOnCompleteListener { task ->
                            if (task.isComplete) {
                                val documents = task.result.documents
                                val goals = mutableListOf<GoalDto>()
                                for (i in documents) {
                                    i.getString(GOAL)?.let {
                                        GoalDto(
                                            i.id,
                                            it
                                        )
                                    }?.let {
                                        goals.add(
                                            it
                                        )
                                    }
                                }
                                emitter.onSuccess(goals)
                            }
                        }
                }
            }
        }

        fun addGoal(
            isNew: Boolean,
            yearId: String,
            year: String,
            goalId: String,
            goal: String
        ): Completable {
            return Completable.create { emitter ->
                if (!isNew) {
                    createGoalDocument(yearId, goalId, goal)?.addOnCompleteListener {
                        if (it.isComplete) {
                            emitter.onComplete()
                        }
                    }
                } else {
                    YearlyPlansRepository().setYearDocument(yearId, year)
                        ?.addOnCompleteListener { task ->
                            if (task.isComplete) {
                                createGoalDocument(
                                    yearId,
                                    goalId,
                                    goal
                                )?.addOnCompleteListener {
                                    if (it.isComplete) {
                                        emitter.onComplete()
                                    }
                                }
                            }
                        }
                }
            }
        }

        private fun createGoalDocument(
            yearId: String,
            goalId: String,
            goal: String
        ): Task<Void>? {
            var document: DocumentReference? = null
            service?.auth?.currentUser?.uid?.let { uid ->
                document = service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(YEARLY_PLANS_LIST)
                    .document(yearId)
                    .collection(GOALS_LIST)
                    .document(goalId)
            }

            val map = HashMap<String, String>().apply {
                put(GOAL, goal)
            }

            return document?.set(map)
        }

    }

    class Dates {
        fun getDates(yearId: String): Single<List<ImportantDateDto>> {
            return Single.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(YEARLY_PLANS_LIST)
                        .document(yearId)
                        .collection(IMPORTANT_DATES_LIST)
                        .get().addOnCompleteListener { task ->
                            if (task.isComplete) {
                                val documents = task.result.documents
                                val dates = mutableListOf<ImportantDateDto>()
                                for (i in documents) {
                                    i.getString(DATA)?.let {
                                        ImportantDateDto(
                                            i.id,
                                            it,
                                            i.getString(EVENT).toString()
                                        )
                                    }?.let {
                                        dates.add(
                                            it
                                        )
                                    }
                                }
                                emitter.onSuccess(dates)
                            }
                        }
                }
            }
        }

        fun addDate(
            isNew: Boolean,
            yearId: String,
            year: String,
            dateId: String,
            date: String,
            event: String
        ): Completable {
            return Completable.create { emitter ->
                if (!isNew) {
                    createDateDocument(yearId, dateId, date, event)?.addOnCompleteListener {
                        if (it.isComplete) {
                            emitter.onComplete()
                        }
                    }
                } else {
                    YearlyPlansRepository().setYearDocument(yearId, year)
                        ?.addOnCompleteListener { task ->
                            if (task.isComplete) {
                                createDateDocument(
                                    yearId,
                                    dateId,
                                    date,
                                    event
                                )?.addOnCompleteListener {
                                    if (it.isComplete) {
                                        emitter.onComplete()
                                    }
                                }
                            }
                        }
                }
            }
        }

        private fun createDateDocument(
            yearId: String,
            dateId: String,
            date: String,
            event: String
        ): Task<Void>? {
            var document: DocumentReference? = null
            service?.auth?.currentUser?.uid?.let { uid ->
                document = service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(YEARLY_PLANS_LIST)
                    .document(yearId)
                    .collection(IMPORTANT_DATES_LIST)
                    .document(dateId)
            }

            val map = HashMap<String, String>().apply {
                put(DATA, date)
                put(EVENT, event)
            }

            return document?.set(map)
        }
    }

    private fun setYearDocument(
        yearId: String,
        year: String
    ): Task<Void>? {
        var document: DocumentReference? = null
        service?.auth?.currentUser?.uid?.let { uid ->
            document = service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(YEARLY_PLANS_LIST)
                .document(yearId)
        }
        val map = HashMap<String, String>().apply {
            put(YEAR, year)
        }

        return document?.set(map)
    }
}