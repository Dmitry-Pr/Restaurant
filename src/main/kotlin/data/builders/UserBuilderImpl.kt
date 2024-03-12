package data.builders

import data.user.Role
import data.user.UserEntity

class UserBuilderImpl : UserBuilder {
    var userId: Int = 0
    var userName: String = ""
    var userSurname: String = ""
    var userLogin: String = ""
    var userPassword: String = ""
    var userRole: Role = Role.User
    override fun setId(id: Int) {
        this.userId = id
    }

    override fun setName(name: String) {
        this.userName = name
    }

    override fun setSurname(surname: String) {
        this.userSurname = surname
    }

    override fun setLogin(login: String) {
        this.userLogin = login
    }

    override fun setPassword(password: String) {
        this.userPassword = password
    }

    override fun setRole(role: Role) {
        this.userRole = role
    }

    override fun getResult(): UserEntity {
        return UserEntity(userId, userName, userSurname, userLogin, userPassword, userRole)
    }

}
