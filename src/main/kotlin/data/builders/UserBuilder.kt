package data.builders

import data.user.Role
import data.user.UserEntity

interface UserBuilder {
    fun setId(id: Int)
    fun setName(name: String)
    fun setSurname(surname: String)
    fun setLogin(login: String)
    fun setPassword(password: String)
    fun setRole(role: Role)
    fun getResult(): UserEntity
}
