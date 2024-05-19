package com.islamelmrabet.cookconnect.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import kotlinx.coroutines.tasks.await

class OrderManager {

    private val db = FirebaseFirestore.getInstance()
    private val ordersCollection = db.collection("orders")

    // Function to add a new order
    suspend fun addOrder(order: Order) {
        try {
            val newOrderRef = ordersCollection.document()
            val key = newOrderRef.id
            val orderWithKey = order.copy(key = key)
            newOrderRef.set(orderWithKey).await()
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }

    // Function to get all orders
    suspend fun getOrders(): List<Order> {
        return try {
            val snapshot = ordersCollection.get().await()
            snapshot.toObjects(Order::class.java)
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
            emptyList()
        }
    }

    // Function to update an existing order
    suspend fun updateOrder(order: Order) {
        try {
            order.key?.let { key ->
                ordersCollection.document(key).set(order).await()
            }
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }

    // Function to delete an order
    suspend fun deleteOrder(order: Order) {
        try {
            order.key?.let { key ->
                ordersCollection.document(key).delete().await()
            }
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }

    // Function to get an order by key
    suspend fun getOrderByKey(key: String): Order? {
        return try {
            val document = ordersCollection.document(key).get().await()
            document.toObject(Order::class.java)
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
            null
        }
    }
}
