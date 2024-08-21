package myapplication.android.mindall.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.common.Constants.Companion.DATA
import myapplication.android.mindall.common.Constants.Companion.MONTH
import myapplication.android.mindall.common.Constants.Companion.MONTH_LIST
import myapplication.android.mindall.common.Constants.Companion.NIGHT_TRACKERS_LIST
import myapplication.android.mindall.common.Constants.Companion.NO_PLANS
import myapplication.android.mindall.common.Constants.Companion.TRACKERS_LIST
import myapplication.android.mindall.common.Constants.Companion.USER_LIST
import myapplication.android.mindall.common.Constants.Companion.VALUE
import myapplication.android.mindall.common.Constants.Companion.YEAR
import myapplication.android.mindall.common.Constants.Companion.YEAR_LIST
import myapplication.android.mindall.data.dto.trackers.MonthDto
import myapplication.android.mindall.data.dto.trackers.TrackerDto
import myapplication.android.mindall.data.dto.trackers.YearDto
import myapplication.android.mindall.di.DI.Companion.service

class NightTrackersRepository {

    fun getYearMonths(year: String): Single<MonthDto> {
        return Single.create { emitter ->
            service?.auth?.currentUser?.uid?.let { uid ->
                service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(NIGHT_TRACKERS_LIST)
                    .get().addOnCompleteListener { query ->
                        if (query.isComplete) {
                            val docs = query.result.documents
                            lateinit var yearId: String
                            for (i in docs) {
                                if (i.getString(YEAR) == year) {
                                    yearId = i.id
                                }
                                //TODO("Implement statistics")
                            }
                        }
                    }
            }
        }
    }

    fun getYearId(year: String): Single<String> {
        return Single.create { emitter ->
            service?.auth?.currentUser?.uid?.let { uid ->
                service.fireStore
                    .collection(USER_LIST)
                    .document(uid)
                    .collection(NIGHT_TRACKERS_LIST)
                    .get().addOnCompleteListener { query ->
                        if (query.isComplete) {
                            var isFound = false
                            val docs = query.result.documents
                            for (i in docs) {
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

    class Trackers {
        fun getMonthId(
            yearId: String,
            month: String
        ): Single<String> {
            return Single.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(NIGHT_TRACKERS_LIST)
                        .document(yearId)
                        .collection(MONTH_LIST)
                        .get().addOnCompleteListener {
                            if (it.isComplete) {
                                val docs = it.result.documents
                                var isFound = false
                                for (i in docs) {
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

        fun getMonthTrackers(
            yearId: String,
            monthId: String
        ): Single<List<TrackerDto>> {
            return Single.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(NIGHT_TRACKERS_LIST)
                        .document(yearId)
                        .collection(MONTH_LIST)
                        .document(monthId)
                        .collection(TRACKERS_LIST)
                        .get().addOnCompleteListener {
                            if (it.isComplete) {
                                val docs = it.result.documents
                                val trackers = mutableListOf<TrackerDto>()

                                for (i in docs) {
                                    trackers.add(
                                        TrackerDto(
                                            i.id,
                                            i.getString(DATA).toString(),
                                            i.getString(VALUE).toString()
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
            trackerId: String,
            date: String,
            value: String
        ): Completable {
            return Completable.create { emitter ->
                service?.auth?.currentUser?.uid?.let { uid ->
                    val document = service.fireStore
                        .collection(USER_LIST)
                        .document(uid)
                        .collection(NIGHT_TRACKERS_LIST)
                        .document(yearId)
                        .collection(MONTH_LIST)
                        .document(monthId)
                        .collection(TRACKERS_LIST)
                        .document(trackerId)

                    if (isNew) {
                        createYearMonthAndTrackerDocs(document, value, date, yearId, year, monthId, month, emitter)
                    } else {
                        createTrackerDocument(document, value, date).addOnCompleteListener {
                            if (it.isComplete) {
                                emitter.onComplete()
                            }
                        }
                    }
                }
            }
        }

        fun editTracker(
            trackerId: String,
            value: String
        ): Completable {
            return Completable.create { emitter ->

            }
        }

        private fun createTrackerDocument(
            document: DocumentReference,
            value: String,
            data: String
        ): Task<Void> {

            val map = HashMap<String, String>().apply {
                put(VALUE, value)
                put(DATA, data)
            }
            return document.set(map)
        }

        private fun createYearMonthAndTrackerDocs(
            doc: DocumentReference,
            value: String,
            date: String,
            yearId: String,
            year: String,
            monthId: String,
            month: String,
            emitter: CompletableEmitter
        ) {
            NightTrackersRepository().createYearDocument(yearId, year)
                .addOnCompleteListener { taskYear ->
                    if (taskYear.isComplete) {
                        NightTrackersRepository().createMonthDocument(yearId, monthId, month).addOnCompleteListener {taskMonth ->
                            if (taskMonth.isComplete){
                                createTrackerDocument(doc, value, date).addOnCompleteListener {
                                    if (it.isComplete){
                                        emitter.onComplete()
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun createYearDocument(yearId: String, year: String): Task<Void> {
        lateinit var yearDoc: DocumentReference
        service?.auth?.currentUser?.uid?.let { uid ->
            yearDoc = service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(NIGHT_TRACKERS_LIST)
                .document(yearId)
        }
        val map = HashMap<String, String>().apply {
            put(YEAR, year)
        }

        return yearDoc.set(map)

    }

    private fun createMonthDocument(yearId: String, monthId: String, month: String): Task<Void> {
        lateinit var monthDoc: DocumentReference
        service?.auth?.currentUser?.uid?.let { uid ->
            monthDoc = service.fireStore
                .collection(USER_LIST)
                .document(uid)
                .collection(NIGHT_TRACKERS_LIST)
                .document(yearId)
                .collection(MONTH_LIST)
                .document(monthId)
        }

        val map = HashMap<String, String>().apply {
            put(MONTH, month)
        }

        return monthDoc.set(map)
    }


}