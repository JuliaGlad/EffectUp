package myapplication.android.mindall.presentation.profile.settings

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.Constants.Companion.EMAIL
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.user.UserDI
import myapplication.android.mindall.domain.model.user.UserEmailAndNameModel
import myapplication.android.mindall.presentation.profile.main.model.ProfileModel

class SettingsViewModel : ViewModel() {

    private val _state : MutableLiveData<State<ProfileModel>> = MutableLiveData(State.loading())
    var state = _state

    private val _isVerifyBeforeUpdate : MutableLiveData<Bundle> = MutableLiveData(null)
    var isVerifyBeforeUpdate = _isVerifyBeforeUpdate

    fun getUserData(){
        UserDI.getUserDataUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<UserEmailAndNameModel> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("GetUserData onSubscribe", "onSubscribe")
                }

                override fun onError(e: Throwable) {
                   _state.value = State.error(e.message.toString())
                }

                override fun onSuccess(model: UserEmailAndNameModel) {
                    val profile = ProfileModel(model.name, model.email)
                    _state.value = State.success(profile)
                }
            })
    }

    fun verifyBeforeUpdate(bundle: Bundle) {
        val email = bundle.getString(EMAIL).toString()
        UserDI.updateEmailUseCase.invoke(email)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("verify before update onSubscribe", "onSubscribe")
                }

                override fun onComplete() {
                    _isVerifyBeforeUpdate.value = bundle
                }

                override fun onError(e: Throwable) {
                    Log.e("verify before update onError", e.message.toString())
                }
            })
    }

}