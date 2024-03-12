package domain.user

import data.user.Role
import data.Session
import data.user.UserDao
import data.user.UserEntity
import domain.Error
import domain.Result
import domain.Success
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.mindrot.jbcrypt.BCrypt
import presentation.model.OutputModel
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

const val USERS_JSON_PATH = "data/users.json"

interface UserController {
    fun addUser(name: String, surname: String, login: String, password: String, role: Role): Result
    fun loginUser(login: String, password: String): Result
    fun deserialize(): Result
}

class UserControllerImpl(
    private val userDao: UserDao,
    private val loginValidator: UserValidator,
    private val registrationValidator: UserValidator,
) : UserController {
    override fun addUser(name: String, surname: String, login: String, password: String, role: Role): Result {
        val checkResult = registrationValidator.check(login, password, name, surname)
        return when {
            checkResult is Error -> checkResult
            else -> {
                userDao.add(
                    name = name,
                    surname = surname,
                    login = login,
                    password = BCrypt.hashpw(password, BCrypt.gensalt()),
                    role = role
                )
                Session.currentUserId = userDao.get(login)!!.id
                Session.currentUserRole = role
                when (val res = serialize()) {
                    is Success -> Success(OutputModel("Successfully added user"))
                    is Error -> res
                }
            }
        }

    }

    override fun loginUser(login: String, password: String): Result {
        val checkResult = loginValidator.check(login, password, "NotEmpty", "NotEmpty")
        return when {
            checkResult is Error -> checkResult
            else -> {
                Session.currentUserId = userDao.get(login)!!.id
                Session.currentUserRole = userDao.get(login)!!.role
                Success(OutputModel("Successfully logged in"))
            }
        }
    }

    override fun deserialize(): Result {
        return try {
            val file = File(USERS_JSON_PATH)
            val jsonString = file.readText()
            val users = Json.decodeFromString<List<UserEntity>>(jsonString)
            userDao.load(users)
            Success(OutputModel("Successfully loaded users data"))
        } catch (ex: FileNotFoundException) {
            Error(OutputModel("The file with users is not found"))
        } catch (ex: Exception) {
            Error(OutputModel("Unable to load users data"))
        }
    }

    private fun serialize(): Result {
        return try {
            val file = File(USERS_JSON_PATH)
            val jsonString = Json.encodeToString(userDao.getAll())
            file.writeText(jsonString)
            Success(OutputModel("Successfully saved users data"))
        } catch (ex: FileNotFoundException) {
            Error(OutputModel("The changes are not saved, users file is not found"))
        } catch (ex: Exception) {
            Error(OutputModel("The changes are not saved, unpredicted problem with saving users file"))
        }
    }

}