package myapplication.android.mindall.di.user

import myapplication.android.mindall.domain.useCase.user.userData.ChangePasswordUseCase
import myapplication.android.mindall.domain.useCase.user.userData.CheckCurrentUserUseCase
import myapplication.android.mindall.domain.useCase.user.userData.CheckPasswordUseCase
import myapplication.android.mindall.domain.useCase.user.userData.CheckVerificationUseCase
import myapplication.android.mindall.domain.useCase.user.userData.CreateAccountWithEmailAndPasswordUseCase
import myapplication.android.mindall.domain.useCase.user.userData.DeleteAccountUseCase
import myapplication.android.mindall.domain.useCase.user.userData.GetDataForEditingUseCase
import myapplication.android.mindall.domain.useCase.user.userData.GetUserDataUseCase
import myapplication.android.mindall.domain.useCase.user.userData.LogoutUseCase
import myapplication.android.mindall.domain.useCase.user.userData.SignInWithEmailAndPasswordUseCase
import myapplication.android.mindall.domain.useCase.user.userData.UpdateEmailFieldUseCase
import myapplication.android.mindall.domain.useCase.user.userData.UpdateEmailUseCase
import myapplication.android.mindall.domain.useCase.user.userData.UpdateUserDataUseCase

class UserDI {
    companion object {
        val createAccountWithEmailAndPasswordUseCase: CreateAccountWithEmailAndPasswordUseCase = CreateAccountWithEmailAndPasswordUseCase()
        val checkCurrentUserUseCase: CheckCurrentUserUseCase = CheckCurrentUserUseCase()
        val signInWithEmailAndPasswordUseCase: SignInWithEmailAndPasswordUseCase = SignInWithEmailAndPasswordUseCase()
        val getUserDataUseCase: GetUserDataUseCase = GetUserDataUseCase()
        val updateUserDataUseCase: UpdateUserDataUseCase = UpdateUserDataUseCase()
        val getDataForEditingUseCase: GetDataForEditingUseCase = GetDataForEditingUseCase()
        val changePasswordUseCase: ChangePasswordUseCase = ChangePasswordUseCase()
        val updateEmailFieldUseCase: UpdateEmailFieldUseCase = UpdateEmailFieldUseCase()
        val updateEmailUseCase: UpdateEmailUseCase = UpdateEmailUseCase()
        val logoutUseCase: LogoutUseCase = LogoutUseCase()
        val checkPasswordUseCase: CheckPasswordUseCase = CheckPasswordUseCase()
        val checkVerificationUseCase: CheckVerificationUseCase = CheckVerificationUseCase()
        val deleteAccountUseCase: DeleteAccountUseCase = DeleteAccountUseCase()
    }
}