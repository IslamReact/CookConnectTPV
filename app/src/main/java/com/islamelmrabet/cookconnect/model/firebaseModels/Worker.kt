package com.islamelmrabet.cookconnect.model.firebaseModels

data class Worker(
    val key: String? = null,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val uid: String = "",
    val userRole: String = "",
    val lastLogin: String = "",
)