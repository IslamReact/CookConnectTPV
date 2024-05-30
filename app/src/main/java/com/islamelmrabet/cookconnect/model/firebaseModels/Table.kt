package com.islamelmrabet.cookconnect.model.firebaseModels

/**
 * Class: Table
 *
 * @property key
 * @property number
 * @property gotOrder
 * @property gotOrderReady
 * @property capacity
 * @property isReserved
 */
data class Table(
    val key: String? = null,
    val number: Int = 0,
    val gotOrder: Boolean = false,
    val gotOrderReady: Boolean = false,
    val capacity: Int = 0, //Not implemented in project
    val isReserved: Boolean = false //Not implemented in project
)