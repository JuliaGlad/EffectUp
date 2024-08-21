package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class ChangePasswordUseCase {
    fun invoke(newPassword: String, oldPassword: String) : Completable{
        return DI.userRepository.changePassword(newPassword, oldPassword)
    }
}