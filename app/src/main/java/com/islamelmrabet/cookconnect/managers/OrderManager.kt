package com.islamelmrabet.cookconnect.managers

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import java.util.concurrent.CompletableFuture

/**
 * Class OrderManager
 *
 */
class OrderManager {

    private val collectionReference = FirebaseFirestore.getInstance().collection("orders")

    /**
     * Adds a new order
     *
     * @param order
     * @return CompletableFuture<Boolean>
     */
    fun addOrderManager(order: Order): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        collectionReference
            .add(order)
            .addOnSuccessListener {
                Log.d("Order", "Order created successfully")
                completableFuture.complete(true)
            }
            .addOnFailureListener { e ->
                Log.d("Order", "Error creating Order: ${e.message}")
                completableFuture.complete(false)
            }
        return completableFuture
    }

    /**
     * Delete an order
     *
     * @param tableThatGotAnOrder
     * @return CompletableFuture<Boolean>
     */
    fun deleteOrderManager(tableThatGotAnOrder: Int): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()

        collectionReference
            .whereEqualTo("tableNumber", tableThatGotAnOrder)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    document.reference
                        .delete()
                        .addOnSuccessListener {
                            Log.d("Order", "Order deleted successfully")
                            completableFuture.complete(true)
                        }
                        .addOnFailureListener { e ->
                            Log.d("Order", "Error deleting Order: ${e.message}")
                            completableFuture.complete(false)
                        }
                } else {
                    Log.d("Order", "No order found for table number: $tableThatGotAnOrder")
                    completableFuture.complete(false)
                }
            }
            .addOnFailureListener { e ->
                Log.d("Order", "Error finding Order: ${e.message}")
                completableFuture.complete(false)
            }

        return completableFuture
    }
}
