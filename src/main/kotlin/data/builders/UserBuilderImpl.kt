package data.builders

import data.user.Role
import data.user.UserEntity

class UserBuilderImpl : UserBuilder {
    var id: Int = 0
    var name: String = ""
    var surname: String = ""
    var login: String = ""
    var password: String = ""
    var role: Role = Role.User
    override fun setId(id: Int) {
        this.id = id
    }

    override fun setName(name: String) {
        this.name = name
    }

    override fun setSurname(surname: String) {
        this.surname = surname
    }

    override fun setLogin(login: String) {
        this.login = login
    }

    override fun setPassword(password: String) {
        this.password = password
    }

    override fun setRole(role: Role) {
        this.role = role
    }

    override fun getResult(): UserEntity {
        return UserEntity(id, name, surname, login, password, role)
    }

}
