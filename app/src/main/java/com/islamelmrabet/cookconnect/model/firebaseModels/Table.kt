package com.islamelmrabet.cookconnect.model.firebaseModels

/**
 * Class: Table
 *
 * @property number
 * @property gotOrder
 * @property gotOrderReady
 * @property capacity
 */
data class Table(
    val number: Int = 0,
    val gotOrder: Boolean = false,
    val gotOrderReady: Boolean = false,
    val capacity: Int = 0,
)