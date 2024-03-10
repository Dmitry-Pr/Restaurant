package domain.user

import domain.Error
import domain.Result
import presentation.model.OutputModel

class UserFieldsValidator: UserValidator() {
    override fun check(login: String, password: String, name: String, surname: String): Result {
        if (name.isEmpty() or surname.isEmpty() or login.isEmpty()) {
            return Error(OutputModel("Invalid user data"))
        }
        return checkNext(login, password, name, surname)
    }
}