package com.islamelmrabet.cookconnect.model.firebaseModels

/*
 * Order model
 *
 *
 * OrderDateCreated and TableNumber are the primary keys
 */
data class Order(
    val key: String? = null,
    // TODO: OrderDateCreatd and TableNumber are the primary keys
    val orderDateCreated: String = "",
    val tableNumber: Int = 0,
    val price : Double = 0.0,
    val isReady: Boolean = false,
    val isPaidByCash: Boolean = false,
    val isPaid: Boolean = false,
    val orderNote: String = "",
    val products: MutableList<Product> = mutableListOf(),
)