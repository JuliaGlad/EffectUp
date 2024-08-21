package myapplication.android.mindall.presentation.dialog.updateEmail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.user.UserDI

class UpdateEmailViewModel : ViewModel() {

    private val _isChecked : MutableLiveData<Boolean> = MutableLiveData(false)
    var isChecked = _isChecked

    fun checkPassword(password: String) {
        UserDI.checkPasswordUseCase.invoke(password)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("CheckPassword onSubscribe", "onSubscribe")
                }

                override fun onComplete() {
                   _isChecked.value = true
                }

                override fun onError(e: Throwable) {
                    Log.e("CheckPassword onError", "onError")
                }
            })
    }
}