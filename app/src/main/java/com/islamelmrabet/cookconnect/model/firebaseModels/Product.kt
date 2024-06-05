package com.islamelmrabet.cookconnect.model.firebaseModels

/**
 * Class: Product
 *
 * @property key
 * @property productUID
 * @property productName
 * @property quantity
 * @property unitPrice
 * @property category
 */
data class Product(
    val key: String? = null,
    val productUID: String = "",
    val productName: String = "",
    val quantity: Int = 0,
    val unitPrice: Double = 0.00,
    val category: String = ""
) {
    constructor() : this(null, "efjnwefonw", "", 0, 0.00, "")
}