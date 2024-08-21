package myapplication.android.mindall.domain.useCase.user.userData

import io.reactivex.rxjava3.core.Single
import myapplication.android.mindall.di.DI
import myapplication.android.mindall.domain.model.user.EditUserModel

class GetDataForEditingUseCase {
    fun invoke() : Single<EditUserModel>{
        return DI.userRepository.getUserData().flatMap {
            val editModel = EditUserModel(it.name, it.email, it.phoneNumber, it.status)
            Single.just(editModel)
        }
    }
}