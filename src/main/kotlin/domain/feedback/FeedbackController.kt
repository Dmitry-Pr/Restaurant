package domain.feedback

import data.Session
import data.feedback.FeedbackDao
import data.feedback.FeedbackEntity
import data.meal.MealDao
import data.order.OrderDao
import presentation.model.OutputModel
import domain.Result
import domain.Error
import domain.Success
import domain.order.OrderController
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

const val FEEDBACK_JSON_PATH = "data/feedback.json"

interface FeedbackController {
    fun addFeedback(mealId: Int, userId: Int, rating: Int, comment: String): OutputModel
    fun getAllFeedback(): OutputModel
    fun getFeedbackByMealId(mealId: Int): OutputModel
    fun getFeedbackByUserId(userId: Int): OutputModel
    fun getAllFeedbackByRating(): OutputModel
    fun getMeanRatingByMealId(mealId: Int): OutputModel
    fun deserialize(): Result

}

class FeedbackControllerImpl(
    private val feedbackDao: FeedbackDao,
    private val mealDao: MealDao,
    private val orderDao: OrderDao,
    private val feedbackValidator: FeedbackValidator
) : FeedbackController {
    override fun addFeedback(mealId: Int, userId: Int, rating: Int, comment: String): OutputModel {
        if (orderDao.get(Session.currentOrderId)?.meals?.contains(mealId) == false) {
            return OutputModel("You can't leave feedback for the meal that is not in your order")
        }
        mealDao.get(mealId) ?: return OutputModel("Meal with id $mealId is not found")
        return when (val validateRating = feedbackValidator.validateRating(rating)) {
            is Error -> validateRating.outputModel
            else -> {
                feedbackDao.add(mealId, userId, rating, comment)
                OutputModel("Feedback added " + serialize().message)
            }
        }
    }

    override fun getAllFeedback(): OutputModel {
        val feedback = feedbackDao.getAll().joinToString("\n")
        return OutputModel(feedback).takeIf { it.message.isNotEmpty() } ?: OutputModel("No feedback found")
    }

    override fun getFeedbackByMealId(mealId: Int): OutputModel {
        val feedback = feedbackDao.getAll().filter { it.mealId == mealId }.joinToString("\n")
        return OutputModel(feedback).takeIf { it.message.isNotEmpty() } ?: OutputModel("No feedback found")
    }

    override fun getFeedbackByUserId(userId: Int): OutputModel {
        val feedback = feedbackDao.getAll().filter { it.userId == userId }.joinToString("\n")
        return OutputModel(feedback).takeIf { it.message.isNotEmpty() } ?: OutputModel("No feedback found")
    }

    override fun getAllFeedbackByRating(): OutputModel {
        val feedback = feedbackDao.getAll().groupBy { it.rating }
        var res = ""
        for (i in 5 downTo 1) {
            for (j in feedback[i] ?: emptyList()) {
                res += "${i} stars: ${j}\n"
            }
        }
        return OutputModel(res).takeIf { it.message.isNotEmpty() } ?: OutputModel("No feedback found")
    }

    override fun getMeanRatingByMealId(mealId: Int): OutputModel {
        val feedback = feedbackDao.getAll().filter { it.mealId == mealId }
        if (feedback.isEmpty()) return OutputModel("No feedback found")
        val meanRating = feedback.map { it.rating }.average()
        return OutputModel(meanRating.toString())
    }

    override fun deserialize(): Result {
        return try {
            val file = File(FEEDBACK_JSON_PATH)
            val jsonString = file.readText()
            val feedback = Json.decodeFromString<List<FeedbackEntity>>(jsonString)
            feedbackDao.load(feedback)
            Success(OutputModel("Successfully loaded feedback data"))
        } catch (ex: FileNotFoundException) {
            Error(OutputModel("The file with feedback is not found"))
        } catch (ex: Exception) {
            Error(OutputModel("Unpredicted problem with feedback file"))
        }
    }

    private fun serialize(): OutputModel {
        return try {
            val file = File(FEEDBACK_JSON_PATH)
            val jsonString = Json.encodeToString(feedbackDao.getAll())
            file.writeText(jsonString)
            OutputModel("Successfully saved feedback data")
        } catch (ex: FileNotFoundException) {
            OutputModel("\nThe changes are not saved, feedback file is not found")
        } catch (ex: Exception) {
            OutputModel("\nThe changes are not saved, unpredicted problem with saving feedback file")
        }
    }
}