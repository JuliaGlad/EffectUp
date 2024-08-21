package myapplication.android.mindall.presentation.dialog.verifyEmail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.user.UserDI

class VerifyEmailViewModel : ViewModel() {

    private val _isVerified : MutableLiveData<Boolean> = MutableLiveData(false)
    var isVerified = _isVerified

    fun verify(email: String, password: String){
        UserDI.logoutUseCase.invoke()
        UserDI.signInWithEmailAndPasswordUseCase.invoke(email, password)
            .andThen(UserDI.checkVerificationUseCase.invoke())
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<Boolean> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("Check verification onSubscribe", "onSubscribe")
                }

                override fun onSuccess(isVerified: Boolean) {
                    _isVerified.value = isVerified
                }

                override fun onError(e: Throwable) {
                    Log.e("Check verification onError", e.message.toString())
                }
            })
    }
}