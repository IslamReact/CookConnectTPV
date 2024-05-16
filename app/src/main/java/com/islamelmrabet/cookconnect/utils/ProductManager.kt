package com.islamelmrabet.cookconnect.utils

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import java.util.concurrent.CompletableFuture

class ProductManager(private val context: Context) {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("products")

    fun addProduct(product: Product): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        FirebaseFirestore.getInstance().collection("products")
            .add(product)
            .addOnSuccessListener {
                Log.d("Product", "Product created successfully")
                completableFuture.complete(true)
            }
            .addOnFailureListener { e ->
                Log.d("Product", "Error creating Product: ${e.message}")
                completableFuture.complete(false)
            }
        return completableFuture
    }

    fun deleteProduct(productKey: String): TableRes<Unit> {
        return try {
            databaseReference.child(productKey).removeValue()
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message ?: "Error deleting Product")
        }
    }
}
