package myapplication.android.mindall.domain.useCase.user.userData

import myapplication.android.mindall.di.DI

class LogoutUseCase {
    fun invoke(){
        return DI.userRepository.logout()
    }
}