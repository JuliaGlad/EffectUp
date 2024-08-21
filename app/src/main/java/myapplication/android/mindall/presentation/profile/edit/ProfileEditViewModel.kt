package myapplication.android.mindall.presentation.profile.edit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.core.CompletableObserver
import io.reactivex.rxjava3.core.SingleObserver
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import myapplication.android.mindall.common.State
import myapplication.android.mindall.di.user.UserDI
import myapplication.android.mindall.domain.model.user.EditUserModel
import myapplication.android.mindall.presentation.profile.edit.model.EditModel

class ProfileEditViewModel : ViewModel() {

    private val _state: MutableLiveData<State<EditModel>> = MutableLiveData(State.loading())
    var state = _state

    private val _isSaved: MutableLiveData<String> = MutableLiveData(null)
    var isSaved = _isSaved

    fun getUserData() {
        UserDI.getDataForEditingUseCase.invoke()
            .subscribeOn(Schedulers.io())
            .subscribe(object : SingleObserver<EditUserModel> {
                override fun onSubscribe(d: Disposable) {
                    Log.i("ProfileEdit onSubscribe", "OnSubscribe")
                }

                override fun onError(e: Throwable) {
                    _state.value = State.error(e.message.toString())
                }

                override fun onSuccess(model: EditUserModel) {
                    val editModel = EditModel(model.name, model.phone, model.email, model.status)
                    _state.value = State.success(editModel)
                }

            })
    }

    fun updateData(name: String, phone: String, status: String) {
        UserDI.updateUserDataUseCase.invoke(name, phone, status)
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    Log.i("UpdateData onSubscribe", "OnSubscribe")
                }

                override fun onComplete() {
                    _isSaved.value = name
                }

                override fun onError(e: Throwable) {
                    Log.e("UpdateData onError", e.message.toString())
                }

            })
    }

}