package myapplication.android.mindall.data.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.common.Constants
import myapplication.android.mindall.common.Constants.Companion.MOOD_TRACKERS_LIST
import myapplication.android.mindall.data.dto.trackers.MonthDto
import myapplication.android.mindall.data.dto.trackers.TrackerDto
import myapplication.android.mindall.di.DI

class MoodTrackerRepository {

    fun getYearId(year: String): Single<String> {
        return Single.create { emitter ->
            DI.service?.auth?.currentUser?.uid?.let { uid ->
                DI.service.fireStore
                    .collection(Constants.USER_LIST)
                    .document(uid)
                    .collection(MOOD_TRACKERS_LIST)
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
                                emitter.onError(Throwable(Constants.NO_PLANS))
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
                DI.service?.auth?.currentUser?.uid?.let { uid ->
                    DI.service.fireStore
                        .collection(Constants.USER_LIST)
                        .document(uid)
                        .collection(MOOD_TRACKERS_LIST)
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
                                    emitter.onError(Throwable(Constants.NO_PLANS))
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
                DI.service?.auth?.currentUser?.uid?.let { uid ->
                    DI.service.fireStore
                        .collection(Constants.USER_LIST)
                        .document(uid)
                        .collection(MOOD_TRACKERS_LIST)
                        .document(yearId)
                        .collection(Constants.MONTH_LIST)
                        .document(monthId)
                        .collection(Constants.TRACKERS_LIST)
                        .get().addOnCompleteListener {
                            if (it.isComplete) {
                                val docs = it.result.documents
                                val trackers = mutableListOf<TrackerDto>()

                                for (i in docs) {
                                    trackers.add(
                                        TrackerDto(
                                            i.id,
                                            i.getString(Constants.DATA).toString(),
                                            i.getString(Constants.VALUE).toString()
                                        )
                                    )
                                }
                                Log.i("Trackers data", "${trackers.size}")
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
                DI.service?.auth?.currentUser?.uid?.let { uid ->
                    val document = DI.service.fireStore
                        .collection(Constants.USER_LIST)
                        .document(uid)
                        .collection(MOOD_TRACKERS_LIST)
                        .document(yearId)
                        .collection(Constants.MONTH_LIST)
                        .document(monthId)
                        .collection(Constants.TRACKERS_LIST)
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
            value: String,
            data: String
        ): Task<Void> {

            val map = HashMap<String, String>().apply {
                put(Constants.VALUE, value)
                put(Constants.DATA, data)
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
            MoodTrackerRepository().createYearDocument(yearId, year)
                .addOnCompleteListener { taskYear ->
                    if (taskYear.isComplete) {
                        MoodTrackerRepository().createMonthDocument(yearId, monthId, month).addOnCompleteListener {taskMonth ->
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
        DI.service?.auth?.currentUser?.uid?.let { uid ->
            yearDoc = DI.service.fireStore
                .collection(Constants.USER_LIST)
                .document(uid)
                .collection(MOOD_TRACKERS_LIST)
                .document(yearId)
        }
        val map = HashMap<String, String>().apply {
            put(Constants.YEAR, year)
        }

        return yearDoc.set(map)

    }

    private fun createMonthDocument(yearId: String, monthId: String, month: String): Task<Void> {
        lateinit var monthDoc: DocumentReference
        DI.service?.auth?.currentUser?.uid?.let { uid ->
            monthDoc = DI.service.fireStore
                .collection(Constants.USER_LIST)
                .document(uid)
                .collection(MOOD_TRACKERS_LIST)
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