package myapplication.android.mindall.presentation.profile.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.user.UserDI
import myapplication.android.mindall.domain.model.user.UserEmailAndNameModel
import myapplication.android.mindall.presentation.profile.main.model.ProfileModel

class ProfileViewModel : ViewModel() {

    private val _state : MutableLiveData<State<ProfileModel>> = MutableLiveData(State.loading())
    var state = _state

    fun checkCurrentUser() : Boolean{
        return UserDI.checkCurrentUserUseCase.invoke()
    }

    fun getUserData(){
        UserDI.getUserDataUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<UserEmailAndNameModel> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("GetUserData onSubscribe", "onSubscribe")
                }

                override fun onError(e: Throwable) {
                    Log.i("GetUserData onError", "onError")
                }

                override fun onSuccess(model: UserEmailAndNameModel) {
                   val profile = ProfileModel(model.name, model.email)
                    _state.value = State.success(profile)
                }
            })
    }
}