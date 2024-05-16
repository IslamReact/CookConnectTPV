package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.utils.ProductManager

sealed class ProductRes<out T> {
    data class Success<T>(val data: T): ProductRes<T>()
    data class Error(val errorMessage: String): ProductRes<Nothing>()
}

class ProductViewModel : ViewModel() {
    fun addProduct(product: Product, productManager: ProductManager, context: Context) {
        when (val result = productManager.addProduct(product)) {
            else -> {
                Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }
}