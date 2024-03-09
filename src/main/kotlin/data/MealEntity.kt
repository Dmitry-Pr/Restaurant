package org.example.data

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class MealEntity(
    val id: Int,
    val name: String,
    val amount: Int,
    val price: Int,
    val duration: Duration,
)