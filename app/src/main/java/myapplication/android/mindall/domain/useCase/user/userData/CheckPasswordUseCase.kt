package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class CheckPasswordUseCase {
    fun invoke(password: String) : Completable{
        return DI.userRepository.checkPassword(password)
    }
}