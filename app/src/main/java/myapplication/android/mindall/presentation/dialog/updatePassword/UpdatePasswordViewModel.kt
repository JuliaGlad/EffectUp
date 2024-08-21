package myapplication.android.mindall.presentation.dialog.updatePassword

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.user.UserDI

class UpdatePasswordViewModel : ViewModel() {

    private val _isUpdated : MutableLiveData<Boolean> = MutableLiveData(false)
    var isUpdated = _isUpdated

    fun updatePassword(oldPassword: String, newPassword: String){
        UserDI.changePasswordUseCase.invoke(newPassword, oldPassword)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("UpdatePassword on Subscribe", "onSubscribe")
                }

                override fun onComplete() {
                    _isUpdated.value = true
                }

                override fun onError(e: Throwable) {
                    Log.e("UpdatePassword onError", e.message.toString())
                }

            })
    }
}