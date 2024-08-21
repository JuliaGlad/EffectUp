package myapplication.android.mindall.presentation.dialog.logout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import myapplication.android.mindall.di.user.UserDI

class LogoutViewModel : ViewModel() {

    private val _isLogOut : MutableLiveData<Boolean> = MutableLiveData(false)
    var isLogOut = _isLogOut

    fun logout(){
        UserDI.logoutUseCase.invoke()
        _isLogOut.value = true
    }

}