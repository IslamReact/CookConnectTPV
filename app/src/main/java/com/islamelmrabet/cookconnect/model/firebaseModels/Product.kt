package com.islamelmrabet.cookconnect.model.firebaseModels

import java.util.UUID

data class Product(
    val key: String? = null,
    val productUID: String = "" ,
    val productName: String = "",
    val quantity: Int = 0,
    val unitPrice: Double = 0.00,
    val category: String = ""
) {
    // No-argument constructor for Firebase
    constructor() : this(null, "efjnwefonw", "", 0, 0.00, "")
}