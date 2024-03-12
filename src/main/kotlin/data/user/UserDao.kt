package data.user

import data.builders.UserBuilderImpl

interface UserDao {
    fun add(name: String, surname: String, login: String, password: String, role: Role)
    fun get(login: String): UserEntity?
    fun getAll(): List<UserEntity>
    fun load(users: List<UserEntity>)
}

class RuntimeUserDao : UserDao {
    private val users = mutableMapOf<String, UserEntity>()
    private var counter = 0
    override fun add(name: String, surname: String, login: String, password: String, role: Role) {
        val builder = UserBuilderImpl()
        builder.setId(counter)
        builder.setName(name)
        builder.setSurname(surname)
        builder.setLogin(login)
        builder.setPassword(password)
        builder.setRole(role)
        val user = builder.getResult()
        users[login] = user
        counter++
    }

    override fun get(login: String): UserEntity? = users[login]
    override fun getAll(): List<UserEntity> = users.values.toList()
    override fun load(users: List<UserEntity>) {
        users.forEach { add(it.name, it.surname, it.login, it.password, it.role) }
    }
}