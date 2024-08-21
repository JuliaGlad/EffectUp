package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Completable
import myapplication.android.mindall.di.DI

class SignInWithEmailAndPasswordUseCase {
    fun invoke(email: String, password: String) : Completable{
        return DI.userRepository.signInWithEmailAndPassword(email, password)
    }
}