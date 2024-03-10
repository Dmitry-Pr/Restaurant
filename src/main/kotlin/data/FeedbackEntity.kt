package data

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackEntity(
    val id: Int,
    val mealId: Int,
    val userId: Int,
    val rating: Int,
    val comment: String,
)