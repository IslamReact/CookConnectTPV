package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.utils.OrderManager

class SharedViewModel : ViewModel() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("orders")

    val response: MutableState<Result<Order>> = mutableStateOf(Result.Empty)

    init {
        fetchOrderData()
    }

    fun fetchOrderData() {
        response.value = Result.Loading
        val orderTempList = mutableListOf<Order>()
        val db = FirebaseFirestore.getInstance()
        val mQuery = db.collection("orders")

        mQuery.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val orderItem = document.toObject(Order::class.java)
                    orderTempList.add(orderItem)
                }
                response.value = Result.Success(orderTempList)
                Log.d("FetchProductData", "Products fetched successfully: ${orderTempList.size}")
            } else {
                Log.e("FetchProductData", "Error fetching products", task.exception)
                response.value = Result.Failure("Error fetching products: ${task.exception?.message}")
            }
        }
    }


    fun addOrder(order: Order, orderManager: OrderManager, context: Context) {
        response.value = Result.Loading

        orderManager.addOrderManager(order).thenAccept { success ->
            if (success) {
                fetchOrderData()
                Toast.makeText(context, "Order successfully sent to kitchen", Toast.LENGTH_SHORT).show()
            } else {
                response.value = Result.Failure("Error adding order")
                Toast.makeText(context, "Error adding order", Toast.LENGTH_SHORT).show()
            }
        }
    }
}