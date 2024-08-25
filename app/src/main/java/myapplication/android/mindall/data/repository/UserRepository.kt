package myapplication.android.mindall.data.repository

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.CompletableEmitter
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.common.Constants.Companion.EMAIL
import myapplication.android.mindall.common.Constants.Companion.PHONE_NUMBER
import myapplication.android.mindall.common.Constants.Companion.STATUS
import myapplication.android.mindall.common.Constants.Companion.USER_LIST
import myapplication.android.mindall.common.Constants.Companion.USER_NAME
import myapplication.android.mindall.data.dto.userDto.FireBaseUserService
import myapplication.android.mindall.data.dto.userDto.UserDto
import myapplication.android.mindall.di.DI.Companion.service

class UserRepository {

    fun signInWithEmailAndPassword(email: String, password: String): Completable {
        return Completable.create { emitter ->
            service?.auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener {
                    it.isSuccessful.let {
                        emitter.onComplete()
                    }
                }
        }
    }

    fun checkUser(): Boolean {
        return service?.auth?.currentUser != null
    }

    fun createAccountWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): Completable {
        return Completable.create { emitter ->
            service?.auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener {
                    if (it.isComplete) {
                        val uid: String = it.result.user?.uid.toString()
                        val docRef: DocumentReference =
                            service.fireStore.collection(USER_LIST).document(uid)
                        val user: HashMap<String, String> = HashMap<String, String>()
                            .apply {
                                put(STATUS, "")
                                put(USER_NAME, name)
                                put(EMAIL, email)
                                put(PHONE_NUMBER, "")
                            }
                        docRef.set(user).addOnCompleteListener { setter ->
                            setter.isSuccessful.let {
                                emitter.onComplete()
                            }
                        }
                    }
                }
        }
    }

    fun updateUserData(name: String, phoneNumber: String, status: String): Completable {
        return Completable.create { emitter ->
            Log.i("Service", service.toString())
            service?.auth?.currentUser?.let { user ->
                service.fireStore.collection(USER_LIST).document(user.uid)
                    .update(
                        USER_NAME, name,
                        PHONE_NUMBER, phoneNumber,
                        STATUS, status
                    ).addOnCompleteListener {
                        emitter.onComplete()
                    }

            }
        }
    }

    fun changePassword(newPassword: String, oldPassword: String): Completable {
        val authCredential = service?.auth?.currentUser?.email
            ?.let { EmailAuthProvider.getCredential(it, oldPassword) }
        return Completable.create { emitter ->
            authCredential?.let {
                service.auth.currentUser?.reauthenticate(authCredential)
                    ?.addOnCompleteListener { task ->
                        task.isComplete.let {
                            service.auth.currentUser?.updatePassword(newPassword)
                            emitter.onComplete()
                        }
                    }
            }
        }
    }

    fun checkPassword(password: String): Completable {
        return Completable.create { emitter: CompletableEmitter ->
            val authCredential = service?.auth?.currentUser?.email?.let {
                EmailAuthProvider.getCredential(
                    it, password
                )
            }
            authCredential?.let {
                service.auth.currentUser?.reauthenticate(it)
                    ?.addOnCompleteListener { task ->
                        if (task.isComplete) {
                            emitter.onComplete()
                        } else {
                            emitter.onError(Throwable(task.exception))
                        }
                    }
            }

        }
    }

    fun verifyEmailBeforeUpdate(email: String): Completable {
        return Completable.create { emitter ->
            service?.auth?.currentUser?.verifyBeforeUpdateEmail(email)
                ?.addOnCompleteListener { task ->
                    task.isSuccessful.let {
                        emitter.onComplete()
                    }
                }
        }
    }

    fun checkVerification(): Single<Boolean> {
        return Single.create { emitter ->
            service?.auth?.currentUser?.reload()?.addOnCompleteListener { task ->
                task.isSuccessful.let {
                    emitter.onSuccess(it)
                }
            }
        }
    }

    fun updateEmailField(email: String): Completable {
        return Completable.create { emitter ->
            service?.auth?.currentUser?.uid?.let {
                service.fireStore.collection(USER_LIST).document(it)
                    .update(EMAIL, email)
                    .addOnCompleteListener { task ->
                        task.isComplete.let {
                            emitter.onComplete()
                        }
                    }
            }
        }
    }

    fun logout() {
        service?.auth?.signOut()
    }

    fun deleteAccount(password: String): Completable {
        return Completable.create { emitter ->
            val user: FirebaseUser? = service?.auth?.currentUser
            user?.let {
                val authCredential =
                    it.email?.let { email -> EmailAuthProvider.getCredential(email, password) }
                authCredential?.let { authCred ->
                    reauthForDeletion(user, authCred, it, service, emitter)
                }
            }
        }
    }

    private fun reauthForDeletion(
        user: FirebaseUser,
        authCred: AuthCredential,
        it: FirebaseUser,
        service: FireBaseUserService,
        emitter: CompletableEmitter
    ) {
        user.reauthenticate(authCred).addOnCompleteListener { task ->
            task.isSuccessful.let { bool ->
                val userId = it.uid
                val docRef = service.fireStore.collection(USER_LIST).document(userId)
                deleteUserDocument(docRef, it, emitter)
            }
        }
    }

    private fun deleteUserDocument(
        docRef: DocumentReference,
        it: FirebaseUser,
        emitter: CompletableEmitter
    ) {
        docRef.delete().addOnCompleteListener { documentDelete ->
            documentDelete.isSuccessful.let { resultDocDelete ->
                deleteUser(it, emitter)
            }
        }
    }

    private fun deleteUser(
        it: FirebaseUser,
        emitter: CompletableEmitter
    ) {
        it.delete().addOnCompleteListener { resultUserDelete ->
            resultUserDelete.isSuccessful.let {
                emitter.onComplete()
            }
        }
    }

    fun getUserData(): Single<UserDto> {
        return Single.create {
            service?.auth?.currentUser?.let { user ->
                service.fireStore.collection(USER_LIST).document(user.uid)
                    .get().addOnCompleteListener { snap ->
                        if (snap.isSuccessful) {
                            val document: DocumentSnapshot = snap.result
                            val dto = document.let {
                                val name: String = it.get(USER_NAME).toString()
                                val email: String = it.get(EMAIL).toString()
                                val phone: String = it.get(PHONE_NUMBER).toString()
                                val status: String = it.get(STATUS).toString()
                                val dto = UserDto(name, phone, email, status)
                                dto
                            }
                            it.onSuccess(dto)
                        }
                    }
            }
        }
    }


    class UserStorage {

        fun uploadUserPhoto() {

        }

        fun downloadPhotoFromStorage() {

        }

    }

}