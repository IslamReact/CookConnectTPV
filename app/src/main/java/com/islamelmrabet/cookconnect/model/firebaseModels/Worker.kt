package com.islamelmrabet.cookconnect.model.firebaseModels

/**
 * Class: Worker
 *
 * @property name
 * @property email
 * @property password
 * @property userRole
 * @property lastLogin
 * @property isANewPassword
 */
data class Worker(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val userRole: String = "",
    val lastLogin: String = "",
    val isANewPassword: Boolean = false
)