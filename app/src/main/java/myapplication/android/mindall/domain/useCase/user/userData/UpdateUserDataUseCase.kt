package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI

class UpdateUserDataUseCase {
    fun invoke(name: String, phone: String, status: String) : Completable {
        return DI.userRepository.updateUserData(name, phone, status)
    }
}