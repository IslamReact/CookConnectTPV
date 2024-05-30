package com.islamelmrabet.cookconnect.model.firebaseModels

/**
 * Class: Worker
 *
 * @property key
 * @property name
 * @property email
 * @property password
 * @property uid
 * @property userRole
 * @property lastLogin
 * @property isANewPassword
 */
data class Worker(
    val key: String? = null,
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val uid: String = "",
    val userRole: String = "",
    val lastLogin: String = "",
    val isANewPassword: Boolean = false
)