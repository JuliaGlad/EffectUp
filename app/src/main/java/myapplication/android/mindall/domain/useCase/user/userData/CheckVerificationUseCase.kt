package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class CheckVerificationUseCase {
    fun invoke() : Single<Boolean> {
        return DI.userRepository.checkVerification()
    }
}