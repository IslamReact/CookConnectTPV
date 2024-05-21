package com.islamelmrabet.cookconnect.utils

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import java.util.concurrent.CompletableFuture

class OrderManager() {

    private val collectionReference = FirebaseFirestore.getInstance().collection("orders")
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("orders")

    // Function to add a new order
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

}
