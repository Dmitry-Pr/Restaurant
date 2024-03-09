package org.example.data

import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class OrderEntity(
    val id: Int,
    val duration: Duration,
    val meals: MutableList<Int>,
)