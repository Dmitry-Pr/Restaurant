package domain.meal

import data.MealDao
import data.MealEntity
import domain.Result
import domain.Error
import domain.Success
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import presentation.model.OutputModel
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import kotlin.time.Duration

const val MEALS_JSON_PATH = "data/meals.json"
interface MealController {
    fun addMeal(name: String, amount: Int, price: Int, duration: String): OutputModel
    fun changeName(id: Int, name: String): OutputModel
    fun changePrice(id: Int, price: Int): OutputModel
    fun changeAmount(id: Int, amount: Int): OutputModel
    fun changeDuration(id: Int, duration: String): OutputModel
    fun getAllMeals(): OutputModel
    fun deserialize(): Result
}

class MealControllerImpl(
    private val mealDao: MealDao,
    private val mealValidator: MealValidator
) : MealController {
    override fun addMeal(name: String, amount: Int, price: Int, duration: String): OutputModel {
        val nameValidation = mealValidator.validateName(name)
        val priceValidation = mealValidator.validatePrice(price)
        val amountValidation = mealValidator.validateAmount(amount)
        val durationValidation = mealValidator.validateDuration(duration)
        return when {
            nameValidation is Error -> nameValidation.outputModel
            priceValidation is Error -> priceValidation.outputModel
            amountValidation is Error -> amountValidation.outputModel
            durationValidation is Error -> durationValidation.outputModel
            else -> {
                mealDao.add(name, amount, price, Duration.parse(duration))
                OutputModel("Added the meal" + serialize().message)
            }
        }
    }

    override fun changeName(id: Int, name: String): OutputModel {
        val meal = mealDao.get(id) ?: return OutputModel("Incorrect meal id")
        return when (val result = mealValidator.validateName(name)) {
            is Success -> {
                val updatedMeal = meal.copy(name = name)
                mealDao.update(updatedMeal)
                OutputModel("Changed name of the meal" + serialize().message)
            }

            is Error -> result.outputModel
        }
    }

    override fun changePrice(id: Int, price: Int): OutputModel {
        val meal = mealDao.get(id) ?: return OutputModel("Incorrect meal id")
        return when (val result = mealValidator.validatePrice(price)) {
            is Success -> {
                val updatedMeal = meal.copy(price = price)
                mealDao.update(updatedMeal)
                OutputModel("Changed price of the meal" + serialize().message)
            }

            is Error -> result.outputModel
        }
    }

    override fun changeAmount(id: Int, amount: Int): OutputModel {
        val meal = mealDao.get(id) ?: return OutputModel("Incorrect meal id")
        return when (val result = mealValidator.validateAmount(amount)) {
            is Success -> {
                val updatedMeal = meal.copy(amount = amount)
                mealDao.update(updatedMeal)
                OutputModel("Changed amount of the meal" + serialize().message)
            }

            is Error -> result.outputModel
        }
    }

    override fun changeDuration(id: Int, duration: String): OutputModel {
        val meal = mealDao.get(id) ?: return OutputModel("Incorrect meal id")
        return when (val result = mealValidator.validateDuration(duration)) {
            is Success -> {
                val updatedMeal = meal.copy(duration = Duration.parse(duration))
                mealDao.update(updatedMeal)
                OutputModel("Changed duration of the meal" + serialize().message)
            }

            is Error -> result.outputModel
        }
    }

    override fun getAllMeals(): OutputModel {
        val meals = mealDao.getAll().joinToString("\n")
        return OutputModel(meals).takeIf { it.message.isNotEmpty() } ?: OutputModel("List of meals is empty")
    }

    override fun deserialize(): Result {
        return try {
            val file = File(MEALS_JSON_PATH)
            val jsonString = file.readText()
            val meals = Json.decodeFromString<List<MealEntity>>(jsonString)
            mealDao.load(meals)
            Success(OutputModel("Successfully loaded meals data"))
        } catch (ex: FileNotFoundException) {
            Error(OutputModel("The file with meals is not found"))
        } catch (ex: Exception) {
            Error(OutputModel("Unpredicted problem with meals file"))
        }
    }
    private fun serialize(): OutputModel {
        return try {
            val file = File(MEALS_JSON_PATH)
            val jsonString = Json.encodeToString(mealDao.getAll())
            file.writeText(jsonString)
            OutputModel("Successfully saved meals data")
        } catch (ex: FileNotFoundException) {
            OutputModel("\nThe changes are not saved, saving file is not found")
        } catch (ex: Exception) {
            OutputModel("\nThe changes are not saved, unpredicted problem with saving file")
        }
    }
}