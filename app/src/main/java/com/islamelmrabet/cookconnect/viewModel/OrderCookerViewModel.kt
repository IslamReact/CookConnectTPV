package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.utils.OrderCookerManager
import com.islamelmrabet.cookconnect.utils.TableRes
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class OrderCookerViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    val response: MutableState<Result<Order>> = mutableStateOf(Result.Empty)

    fun fetchOrderDataFlow(): Flow<List<Order>> = callbackFlow {
        val collectionReference = FirebaseFirestore.getInstance().collection("orders")
            .whereEqualTo("ready", false)

        val subscription = collectionReference.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }
            snapshot?.let { querySnapshot ->
                val allOrders = mutableListOf<Order>()
                for (document in querySnapshot.documents) {
                    val order = document.toObject(Order::class.java)
                    order?.let { allOrders.add(it) }
                }
                trySend(allOrders).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    fun updateOrderReadyStatus(
        orderDateCreated: String,
        orderCookerManager: OrderCookerManager,
        context: Context
    ) {
        val result = orderCookerManager.updateOrderReadyStatus(orderDateCreated)
        when (result) {
            is TableRes.Success -> {
                Toast.makeText(context, "Order status updated successfully", Toast.LENGTH_SHORT)
                    .show()
            }

            is TableRes.Error -> {
                Toast.makeText(context, "Error updating Order", Toast.LENGTH_SHORT).show()
            }
        }
    }

}