package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.managers.OrderCookerManager
import com.islamelmrabet.cookconnect.managers.TableRes
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Class OrderCookerViewModel
 *
 */
class OrderCookerViewModel : ViewModel() {
    /**
     * Fetches all order data from firestore
     *
     * @return Flow<List<Order>>
     */
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

    /**
     * Update the order data flow
     *
     * @param orderDateCreated
     * @param orderCookerManager
     * @param context
     */
    fun updateOrderReadyStatus(
        orderDateCreated: String,
        orderCookerManager: OrderCookerManager,
        context: Context
    ) {
        when (orderCookerManager.updateOrderReadyStatus(orderDateCreated)) {
            is TableRes.Success -> {
                Toast.makeText(context, R.string.order_updated, Toast.LENGTH_SHORT)
                    .show()
            }

            is TableRes.Error -> {
                Toast.makeText(context, R.string.order_not_updated, Toast.LENGTH_SHORT).show()
            }
        }
    }

}