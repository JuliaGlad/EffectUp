package myapplication.android.mindall.presentation.dialog.deleteAccount

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.di.user.UserDI

class DeleteAccountViewModel : ViewModel()  {

    private val _isDeleted : MutableLiveData<Boolean> = MutableLiveData(false)
    var isDeleted = _isDeleted

    fun deleteAccount(password: String){
        UserDI.deleteAccountUseCase.invoke(password)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver{
                override fun onSubscribe(d: Disposable) {
                    Log.i("deleteAccount onSubscribe", "onSubscribe")
                }

                override fun onComplete() {
                    _isDeleted.value = true
                }

                override fun onError(e: Throwable) {
                    Log.e("deleteAccount onError", e.message.toString())
                }

            })
    }
}