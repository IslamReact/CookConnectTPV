package com.islamelmrabet.cookconnect.tools

import com.islamelmrabet.cookconnect.model.firebaseModels.Product

sealed class Result<out T> {
    data class Success<T>(val data: MutableList<T>) : Result<T>()
    data class Failure(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
    object Empty : Result<Nothing>()
}