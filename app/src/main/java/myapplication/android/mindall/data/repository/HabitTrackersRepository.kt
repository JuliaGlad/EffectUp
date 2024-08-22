package myapplication.android.mindall.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.HABIT
import myapplication.android.mindall.common.Constants.Companion.HABIT_TRACKERS_LIST
import myapplication.android.mindall.common.Constants.Companion.IS_COMPLETE
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.common.Constants.Companion.USER_LIST
import myapplication.android.mindall.common.Constants.Companion.YEAR_LIST
import myapplication.android.mindall.data.dto.trackers.HabitDto
import myapplication.android.mindall.data.dto.trackers.HabitTrackerDto
import myapplication.android.mindall.di.DI

class HabitTrackersRepository {

    fun getHabits(): Single<List<HabitDto>> {
        return Single.create { emitter ->
            DI.service?.auth?.currentUser?.uid?.let { uid ->
                DI.service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(HABIT_TRACKERS_LIST)
                    .get().addOnCompleteListener { query ->
                        if (query.isComplete) {
                            val dtos = mutableListOf<HabitDto>()
                            val docs = query.result.documents
                            for (i in docs) {
                                dtos.add(HabitDto(i.id, i.getString(HABIT).toString()))
                            }
                            emitter.onSuccess(dtos)
                        }
                    }
            }
        }
    }

    fun addHabit(habitId: String, habit: String): Completable {
        return Completable.create { emitter ->
            DI.service?.auth?.currentUser?.uid?.let { uid ->
                val document = DI.service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(HABIT_TRACKERS_LIST)
                    .document(habitId)

                val map = HashMap<String, String>().apply {
                    put(HABIT, habit)
                }

                document.set(map).addOnCompleteListener {
                    if (it.isComplete) {
                        emitter.onComplete()
                    }
                }
            }
        }
    }


    fun getHabitId(habit: String): Single<String> {
        return Single.create { emitter ->
            DI.service?.auth?.currentUser?.uid?.let { uid ->
                DI.service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(HABIT_TRACKERS_LIST)
                    .get().addOnCompleteListener { query ->
                        if (query.isComplete) {
                            var isFound = false
                            val docs = query.result.documents
                            for (i in docs) {
                                if (i.getString(HABIT) == habit) {
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

    fun getYearId(year: String, habitId: String): Single<String> {
        return Single.create { emitter ->
            DI.service?.auth?.currentUser?.uid?.let { uid ->
                DI.service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(HABIT_TRACKERS_LIST)
                    .document(habitId)
                    .collection(YEAR_LIST)
                    .get().addOnCompleteListener { query ->
                        if (query.isComplete) {
                            var isFound = false
                            val docs = query.result.documents
                            for (i in docs) {
                                if (i.getString(Constants.YEAR) == year) {
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

    class Trackers {
        fun getMonthId(
            yearId: String,
            habitId: String,
            month: String
        ): Single<String> {
            return Single.create { emitter ->
                DI.service?.auth?.currentUser?.uid?.let { uid ->
                    DI.service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(HABIT_TRACKERS_LIST)
                        .document(habitId)
                        .collection(YEAR_LIST)
                        .document(yearId)
                        .collection(Constants.MONTH_LIST)
                        .get().addOnCompleteListener {
                            if (it.isComplete) {
                                val docs = it.result.documents
                                var isFound = false
                                for (i in docs) {
                                    if (i.getString(Constants.MONTH) == month) {
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

        fun getMonthTrackers(
            yearId: String,
            monthId: String,
            habitId: String
        ): Single<List<HabitTrackerDto>> {
            return Single.create { emitter ->
                DI.service?.auth?.currentUser?.uid?.let { uid ->
                    DI.service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(HABIT_TRACKERS_LIST)
                        .document(habitId)
                        .collection(YEAR_LIST)
                        .document(yearId)
                        .collection(Constants.MONTH_LIST)
                        .document(monthId)
                        .collection(Constants.TRACKERS_LIST)
                        .get().addOnCompleteListener { query ->
                            if (query.isComplete) {
                                val docs = query.result.documents
                                val trackers = mutableListOf<HabitTrackerDto>()

                                for (i in docs) {
                                    var isComplete = false
                                    i.getBoolean(IS_COMPLETE)?.let { isComplete = it }
                                    trackers.add(
                                        HabitTrackerDto(
                                            i.id,
                                            i.getString(DATA).toString(),
                                            isComplete
                                        )
                                    )
                                }
                                emitter.onSuccess(trackers)
                            }
                        }
                }
            }
        }

        fun addTracker(
            isNew: Boolean,
            yearId: String,
            year: String,
            monthId: String,
            month: String,
            habitId: String,
            trackerId: String,
            date: String,
            isComplete: Boolean
        ): Completable {
            return Completable.create { emitter ->
                DI.service?.auth?.currentUser?.uid?.let { uid ->
                    val document = DI.service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(HABIT_TRACKERS_LIST)
                        .document(habitId)
                        .collection(YEAR_LIST)
                        .document(yearId)
                        .collection(Constants.MONTH_LIST)
                        .document(monthId)
                        .collection(Constants.TRACKERS_LIST)
                        .document(trackerId)

                    if (isNew) {
                        createHabitYearMonthAndTrackerDocs(
                            document,
                            date,
                            habitId,
                            yearId,
                            year,
                            monthId,
                            month,
                            isComplete,
                            emitter
                        )
                    } else {
                        createTrackerDocument(document, date, isComplete).addOnCompleteListener {
                            if (it.isComplete) {
                                emitter.onComplete()
                            }
                        }
                    }
                }
            }
        }

//        fun editTracker(
//            trackerId: String,
//            value: String
//        ): Completable {
//            return Completable.create { emitter ->
//
//            }
//        }

        private fun createTrackerDocument(
            document: DocumentReference,
            data: String,
            isComplete: Boolean
        ): Task<Void> {
            val map = HashMap<String, Any>().apply {
                put(IS_COMPLETE, isComplete)
                put(DATA, data)
            }
            return document.set(map)
        }

        private fun createHabitYearMonthAndTrackerDocs(
            doc: DocumentReference,
            date: String,
            habitId: String,
            yearId: String,
            year: String,
            monthId: String,
            month: String,
            isComplete: Boolean,
            emitter: CompletableEmitter
        ) {
            HabitTrackersRepository().createYearDocument(yearId, year, habitId)
                .addOnCompleteListener { taskYear ->
                    if (taskYear.isComplete) {
                        HabitTrackersRepository().createMonthDocument(
                            habitId,
                            yearId,
                            monthId,
                            month
                        )
                            .addOnCompleteListener { taskMonth ->
                                if (taskMonth.isComplete) {
                                    createTrackerDocument(
                                        doc,
                                        date,
                                        isComplete
                                    ).addOnCompleteListener {
                                        if (it.isComplete) {
                                            emitter.onComplete()
                                        }
                                    }
                                }
                            }
                    }
                }
        }
    }

    private fun createYearDocument(yearId: String, year: String, habitId: String): Task<Void> {
        lateinit var yearDoc: DocumentReference
        DI.service?.auth?.currentUser?.uid?.let { uid ->
            yearDoc = DI.service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(HABIT_TRACKERS_LIST)
                .document(habitId)
                .collection(YEAR_LIST)
                .document(yearId)
        }
        val map = HashMap<String, String>().apply {
            put(Constants.YEAR, year)
        }

        return yearDoc.set(map)

    }

    private fun createMonthDocument(
        habitId: String,
        yearId: String,
        monthId: String,
        month: String
    ): Task<Void> {
        lateinit var monthDoc: DocumentReference
        DI.service?.auth?.currentUser?.uid?.let { uid ->
            monthDoc = DI.service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(HABIT_TRACKERS_LIST)
                .document(habitId)
                .collection(YEAR_LIST)
                .document(yearId)
                .collection(Constants.MONTH_LIST)
                .document(monthId)
        }

        val map = HashMap<String, String>().apply {
            put(Constants.MONTH, month)
        }

        return monthDoc.set(map)
    }

}