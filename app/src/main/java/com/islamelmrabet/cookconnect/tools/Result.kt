package com.islamelmrabet.cookconnect.tools

sealed class Result<out T> {
    data class Success<T>(val data: MutableList<T>) : Result<T>()
    data class Failure(val message: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
    data object Empty : Result<Nothing>()
}

