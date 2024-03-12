package data

import data.user.Role

object Session {
    var currentUserId: Int = 0
    var currentUserRole: Role = Role.User
    var currentOrderId: Int = 0
}