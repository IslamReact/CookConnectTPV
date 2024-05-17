package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.utils.ProductManager
import kotlinx.coroutines.tasks.await


class ProductViewModel : ViewModel() {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("products")

    val response: MutableState<Result> = mutableStateOf(Result.Empty)
    init {
        fetchProductData()
    }

    fun addProduct(product: Product, productManager: ProductManager, context: Context) {
        when (val result = productManager.addProduct(product)) {
            else -> {
                Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun fetchProductData() {
        response.value = Result.Loading
        val productTempList = mutableListOf<Product>()
        val db = FirebaseFirestore.getInstance()
        val mQuery = db.collection("products")

        mQuery.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val productItem = document.toObject(Product::class.java)
                    productTempList.add(productItem)
                }
                response.value = Result.Success(productTempList)
                Log.d("FetchProductData", "Products fetched successfully: ${productTempList.size}")
            } else {
                Log.e("FetchProductData", "Error fetching products", task.exception)
                response.value = Result.Failure("Error fetching products: ${task.exception?.message}")
            }
        }
    }

    suspend fun getProduct(productName: String): Product? {
        val productsRef = FirebaseFirestore.getInstance().collection("products")
        val querySnapshot = productsRef.whereEqualTo("productName", productName).get().await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents.firstOrNull()?.toObject(Product::class.java)
        } else {
            null
        }
    }


}