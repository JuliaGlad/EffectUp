package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class UpdateEmailUseCase {
    fun invoke(email: String) : Completable{
        return DI.userRepository.verifyEmailBeforeUpdate(email)
    }
}