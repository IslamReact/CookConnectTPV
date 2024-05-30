package com.islamelmrabet.cookconnect.model.firebaseModels

/**
 * CLASS: Invoice
 *
 * @property key
 * @property invoiceDateCreated
 * @property isPayedByCash
 * @property isPayed
 * @property orderDateCreated
 * @property tableNumber
 * @property price
 * @property productQuantityMap
 */
data class Invoice(
    val key: String? = null,
    val invoiceDateCreated: String? = "",
    val isPayedByCash: Boolean = false,
    val isPayed: Boolean = false,
    val orderDateCreated: String = "",
    val tableNumber: Int = 0,
    val price: Double = 0.0,
    val productQuantityMap: Map<String, Int>
) {
    constructor() : this(
        invoiceDateCreated = "",
        isPayedByCash = false,
        isPayed = false,
        orderDateCreated = "",
        tableNumber = 0,
        price = 0.0,
        productQuantityMap = emptyMap()
    )
}