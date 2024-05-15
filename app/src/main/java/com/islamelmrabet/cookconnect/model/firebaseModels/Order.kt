package com.islamelmrabet.cookconnect.model.firebaseModels

data class Order(
    val key: String? = null,
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val uid: String = ""
)