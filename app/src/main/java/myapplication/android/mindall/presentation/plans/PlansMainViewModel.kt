package myapplication.android.mindall.presentation.plans

import androidx.lifecycle.ViewModel
import myapplication.android.mindall.di.user.UserDI

class PlansMainViewModel : ViewModel(){

    fun checkUser() : Boolean{
        return UserDI.checkCurrentUserUseCase.invoke()
    }

}