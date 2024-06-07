package com.islamelmrabet.cookconnect.model.firebaseModels

/**
 * CLASS: Invoice
 *
 * @property invoiceDateCreated
 * @property payedByCash
 * @property payed
 * @property orderDateCreated
 * @property tableNumber
 * @property price
 * @property productQuantityMap
 */
data class Invoice(
    val invoiceDateCreated: String? = "",
    val payedByCash: Boolean = false,
    val payed: Boolean = false,
    val orderDateCreated: String = "",
    val tableNumber: Int = 0,
    val price: Double = 0.0,
    val productQuantityMap: Map<String, Int>
) {
    constructor() : this(
        invoiceDateCreated = "",
        payedByCash = false,
        payed = false,
        orderDateCreated = "",
        tableNumber = 0,
        price = 0.0,
        productQuantityMap = emptyMap()
    )
}