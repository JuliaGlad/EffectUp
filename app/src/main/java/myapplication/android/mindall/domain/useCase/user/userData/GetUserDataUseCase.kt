package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.user.UserEmailAndNameModel

class GetUserDataUseCase {
    fun invoke(): Single<UserEmailAndNameModel> {
        return DI.userRepository.getUserData().flatMap {
            val model = UserEmailAndNameModel(it.name, it.email)
            Single.just(model)
        }
    }
}