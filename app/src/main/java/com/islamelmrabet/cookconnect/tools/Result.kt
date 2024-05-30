package com.islamelmrabet.cookconnect.tools

/**
 * Class: Result
 *
 * Description: Used to create a Result about a future result of a query.
 *
 * @param T
 */
sealed class Result<out T> {
    data class Success<T>(val data: MutableList<T>) : Result<T>()
    data class Failure(val message: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
    data object Empty : Result<Nothing>()
}

