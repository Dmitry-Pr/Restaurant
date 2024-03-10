package domain.user
import domain.Result
import domain.Success
import presentation.model.OutputModel

abstract class UserValidator {
    private lateinit var next: UserValidator

    fun link(first: UserValidator, vararg chain: UserValidator): UserValidator {
        var current = first
        for (validator in chain) {
            current.next = validator
            current = validator
        }
        return current
    }

    abstract fun check(login: String, password: String, name: String, surname: String): Result

    protected fun checkNext(login: String, password: String, name: String, surname: String): Result {
        return if (this::next.isInitialized) {
            next.check(login, password, name, surname)
        } else {
            Success(OutputModel("Success"))
        }
    }
}