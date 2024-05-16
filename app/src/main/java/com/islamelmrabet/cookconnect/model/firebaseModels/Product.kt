package com.islamelmrabet.cookconnect.model.firebaseModels

data class Product(
    val key: String? = null,
    val productName: String = "",
    val quantity: Int = 0,
    val unitPrice: Double = 0.00,
    val category: String = "",
)