package domain.meal
import domain.Result
import domain.Error
import domain.Success
import presentation.model.OutputModel
import kotlin.time.Duration

interface MealValidator {
    fun validateName(name: String): Result
    fun validatePrice(price: Int): Result
    fun validateAmount(amount: Int): Result
    fun validateDuration(duration: String): Result
}

class MealValidatorImpl : MealValidator {
    override fun validateName(name: String): Result {
        return when {
            name.isEmpty() -> Error(OutputModel("Name is incorrect"))
            else -> Success(OutputModel("Name is correct"))
        }
    }

    override fun validatePrice(price: Int): Result {
        return when {
            price < 0 -> Error(OutputModel("Price is incorrect"))
            else -> Success(OutputModel("Price is correct"))
        }
    }

    override fun validateAmount(amount: Int): Result {
        return when {
            amount < 0 -> Error(OutputModel("Amount is incorrect"))
            else -> Success(OutputModel("Amount is correct"))
        }
    }

    override fun validateDuration(duration: String): Result {
        return try {
            Duration.parse(duration)
            Success(OutputModel("Duration is correct"))
        } catch (ex: IllegalArgumentException) {
            Error(OutputModel("Duration is incorrect"))
        }
    }
}