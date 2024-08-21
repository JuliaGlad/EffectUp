package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import myapplication.android.mindall.di.DI

class CreateAccountWithEmailAndPasswordUseCase {
    fun invoke(
        name: String,
        email: String,
        password: String
    ) : Completable {
        return DI.userRepository.createAccountWithEmailAndPassword(name, email, password)
    }
}