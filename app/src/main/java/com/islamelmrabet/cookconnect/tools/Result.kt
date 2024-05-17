package com.islamelmrabet.cookconnect.tools

import com.islamelmrabet.cookconnect.model.firebaseModels.Product

sealed class Result{
    class Success(val data: MutableList<Product>) : Result()
    class Failure(val message: String) : Result()
    data object Loading : Result()
    data object Empty : Result()
}
