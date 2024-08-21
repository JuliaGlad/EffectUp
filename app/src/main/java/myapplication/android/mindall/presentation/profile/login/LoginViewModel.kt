package myapplication.android.mindall.presentation.profile.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.user.UserDI

class LoginViewModel : ViewModel() {

    private val _isLogged: MutableLiveData<Boolean> = MutableLiveData(null)
    var isLogged = _isLogged

    fun signIn(email: String, password: String) {
        UserDI.signInWithEmailAndPasswordUseCase.invoke(email, password)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Sign in onSubscribe", "onSubscribe")
                }

                override fun onComplete() {
                    _isLogged.value = true
                }

                override fun onError(e: Throwable) {
                    Log.e("Sign in onError", e.message.toString())
                }
            })
    }
}