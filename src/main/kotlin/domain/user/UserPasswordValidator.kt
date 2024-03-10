package domain.user

import domain.Error
import domain.Result
import presentation.model.OutputModel

const val MIN_PASSWORD_LENGTH = 6
class UserPasswordValidator: UserValidator() {
    override fun check(login: String, password: String, name: String, surname: String): Result {
        if (password.length < MIN_PASSWORD_LENGTH) {
            return Error(OutputModel("Password is too short"))
        }
        return checkNext(login, password, name, surname)
    }
}