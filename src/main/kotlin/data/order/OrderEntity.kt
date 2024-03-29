package data.order

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration

enum class OrderState {
    Created, InProgress, Ready, Paid
}

@Serializable
data class OrderEntity(
    val id: Int,
    val userId: Int,
    val duration: Duration,
    val meals: MutableMap<Int, Int>,
    val totalPrice: Int,
    val startedOn: LocalDateTime?,
    val state: OrderState
)