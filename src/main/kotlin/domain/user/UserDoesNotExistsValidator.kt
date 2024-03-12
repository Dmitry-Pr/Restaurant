package domain.user

import data.user.UserDao
import domain.Error
import domain.Result
import presentation.model.OutputModel

class UserDoesNotExistsValidator(
    private val userDao: UserDao
) : UserValidator() {
    override fun check(login: String, password: String, name: String, surname: String): Result {
        userDao.get(login) ?: return Error(OutputModel("User with login $login already exists"))
        return checkNext(login, password, name, surname)
    }
}