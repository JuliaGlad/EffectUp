package myapplication.android.mindall.domain.useCase.user.userData

import myapplication.android.mindall.di.DI

class CheckCurrentUserUseCase {
    fun invoke() : Boolean{
        return DI.userRepository.checkUser()
    }
}