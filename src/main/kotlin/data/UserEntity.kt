package org.example.data

import kotlinx.serialization.Serializable

enum class Role {
    User, Admin
}

@Serializable
data class UserEntity(
    val id: Int,
    val name: String,
    val surname: String,
    val login: String,
    val password: String,
    val role: Role,
)
