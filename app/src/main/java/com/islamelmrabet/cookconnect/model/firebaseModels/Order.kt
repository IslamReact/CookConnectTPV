package com.islamelmrabet.cookconnect.model.firebaseModels


/**
 * CLASS: Order
 *
 * @property key
 * @property orderDateCreated
 * @property tableNumber
 * @property price
 * @property isReady
 * @property orderNote
 * @property productQuantityMap
 */
data class Order(
    val key: String? = null,
    // TODO: OrderDateCreated y TableNumber son las claves principales
    val orderDateCreated: String = "",
    val tableNumber: Int = 0,
    val price: Double = 0.0,
    var isReady: Boolean = false,
    val productQuantityMap: Map<String, Int>
) {
    constructor() : this(null, "", 0, 0.0, false, emptyMap())
}