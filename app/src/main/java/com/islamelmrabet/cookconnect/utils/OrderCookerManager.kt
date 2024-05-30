package com.islamelmrabet.cookconnect.utils

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Class OrderCookerManager
 *
 */
class OrderCookerManager() {

    private val collectionReference = FirebaseFirestore.getInstance().collection("orders")

    /**
     * Update order status in case the order is ready.
     *
     * @param orderDateCreated
     * @return
     */
    fun updateOrderReadyStatus(orderDateCreated: String): TableRes<Unit> {
        return try {
            collectionReference
                .whereEqualTo("orderDateCreated", orderDateCreated)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        Log.e("Order", "No order found with the given orderDateCreated")
                        TableRes.Error("No order found with the given orderDateCreated")
                    } else {
                        val document = querySnapshot.documents[0]
                        document.reference
                            .update("ready", true)
                            .addOnSuccessListener {
                                Log.d("Order", "Order status updated successfully")
                                TableRes.Success(Unit)
                            }
                            .addOnFailureListener { e ->
                                Log.e("Order", "Error updating order status", e)
                                TableRes.Error(e.message ?: "Error updating order status")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Order", "Error finding order", e)
                    TableRes.Error(e.message ?: "Error finding order")
                }
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message ?: "Error updating order status")

        }
    }
}
