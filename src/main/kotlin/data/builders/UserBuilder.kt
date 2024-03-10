package org.example.data.builders

import org.example.data.Role
import org.example.data.UserEntity

interface UserBuilder {
    fun setId(id: Int)
    fun setName(name: String)
    fun setSurname(surname: String)
    fun setLogin(login: String)
    fun setPassword(password: String)
    fun setRole(role: Role)
    fun getResult(): UserEntity
}