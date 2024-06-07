package com.islamelmrabet.cookconnect.model.firebaseModels

/**
 * Class: Product
 *
 * @property productName
 * @property quantity
 * @property unitPrice
 * @property category
 */
data class Product(
    val productName: String = "",
    val quantity: Int = 0,
    val unitPrice: Double = 0.00,
    val category: String = ""
) {
    constructor() : this("",0, 0.00, "")
}