package domain.user

import domain.Result
import domain.Error
import data.user.UserDao
import org.mindrot.jbcrypt.BCrypt
import presentation.model.OutputModel

class UserLoginValidator(
    val userDao: UserDao
) : UserValidator() {
    override fun check(login: String, password: String, name: String, surname: String): Result {
        val user = userDao.get(login) ?: return Error(OutputModel("User with login $login is not found"))
        return when {
            !BCrypt.checkpw(password, user.password) -> Error(OutputModel("Incorrect password"))
            else -> checkNext(login, password, name, surname)
        }
    }
}