package myapplication.android.mindall.presentation.profile.createAccount.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.user.UserDI

class CreateAccountViewModel : ViewModel() {

    private val _isCreated = MutableLiveData(false)
    var isCreated: LiveData<Boolean> = _isCreated

    private val _sendPasswordError: MutableLiveData<String?> = MutableLiveData(null)
    var sendPasswordError: LiveData<String?> = _sendPasswordError

    private val _sendNameError: MutableLiveData<String?> = MutableLiveData(null)
    var sendNameError: LiveData<String?> = _sendNameError

    private val _sendEmailError: MutableLiveData<String?> = MutableLiveData(null)
    var sendPhoneError: LiveData<String?> = _sendEmailError

    fun createAccountWithPhoneNumberAndPassword(name: String, email: String, password: String) {
        UserDI.createAccountWithEmailAndPasswordUseCase.invoke(name, email, password)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("CreateAccount", "CreateAccountWithEmailAndPasswordUseCase onSubscribe")
                }

                override fun onComplete() {
                    _isCreated.value = true
                }

                override fun onError(e: Throwable) {
                    Log.i("CreateAccountOnError", e.message.toString())
                }

            }
            )

    }

    fun checkCurrentUser(): Boolean {
        return UserDI.checkCurrentUserUseCase.invoke()
    }

    fun sendEmailError(message: String) {
        _sendEmailError.value = message
    }

    fun sendPasswordError(message: String) {
        _sendPasswordError.value = message
    }

    fun sendNameError(message: String) {
        _sendNameError.value = message
    }

    fun removeEmailError() {
        _sendEmailError.value = null
    }

    fun removePasswordError() {
        _sendPasswordError.value = null
    }

    fun removeNameError() {
        _sendNameError.value = null
    }
}