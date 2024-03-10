package domain.feedback

import domain.Result
import domain.Success
import domain.Error
import presentation.model.OutputModel

interface FeedbackValidator {
    fun validateRating(rating: Int): Result

}

class FeedbackValidatorImpl : FeedbackValidator {
    override fun validateRating(rating: Int): Result {
        return when {
            rating in 1..5 -> Success(OutputModel("Rating is correct"))
            else -> Error(OutputModel("Rating is incorrect"))
        }
    }
}
