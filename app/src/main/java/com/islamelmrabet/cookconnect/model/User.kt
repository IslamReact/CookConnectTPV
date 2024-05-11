package com.islamelmrabet.cookconnect.model

enum class UserRole {
    ADMINISTRATOR,
    WAITER,
    CHEF
}

data class User(
    val key: String? = null,
    val name: String = "",
    val email: String = "",
    val role: UserRole = UserRole.WAITER,
    val uid: String = ""
)
