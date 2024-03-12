package presentation.input.commands

import presentation.model.OutputModel

interface FeedbackCommandsHandler {
    fun addFeedback(): OutputModel
    fun getAllFeedback(): OutputModel
    fun getFeedbackByMeal(): OutputModel
    fun getFeedbackByUser(): OutputModel
    fun getAllFeedbackByRating(): OutputModel
    fun getMeanRatingByMeal(): OutputModel
}