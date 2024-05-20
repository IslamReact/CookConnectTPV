package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.utils.ProductManager
import com.islamelmrabet.cookconnect.utils.TableRes
import kotlinx.coroutines.launch

class OrderCookerViewModel : ViewModel() {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("orders")

    val response: MutableState<Result<Order>> = mutableStateOf(Result.Empty)
    init {
        fetchOrderData()
    }
    fun updateProduct(originalProductName : String, product: Product, productManager: ProductManager, context: Context) {
        viewModelScope.launch {
            val productId = productManager.getProductDocumentIdByName(originalProductName)
            if (productId!= null) {
                val productName: String = product.productName
                val quantity: Int = product.quantity
                val unitPrice = product.unitPrice
                val category = product.category
                val newProduct = Product(
                    productName = productName,
                    unitPrice = unitPrice,
                    quantity = quantity,
                    category = category
                )
                val result = productManager.updateProduct(productId, newProduct)
                if (result is TableRes.Success) {
                    Toast.makeText(context, "Order updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Order updating product", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Order not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun fetchOrderData() {
        response.value = Result.Loading
        val orderTempList = mutableListOf<Order>()
        val db = FirebaseFirestore.getInstance()
        val mQuery = db.collection("orders")

        mQuery.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val ordertItem = document.toObject(Order::class.java)
                    orderTempList.add(ordertItem)
                }
                response.value = Result.Success(orderTempList)
                Log.d("FetchOrderData", "Orders fetched successfully: ${orderTempList.size}")
            } else {
                Log.e("FetchOrderData", "Error fetching orders", task.exception)
                response.value = Result.Failure("Error fetching orders: ${task.exception?.message}")
            }
        }
    }

}