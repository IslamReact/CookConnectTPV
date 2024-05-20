package com.islamelmrabet.cookconnect.utils

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Product

class OrderCookerManager(private val context: Context) {

    private val collectionReference = FirebaseFirestore.getInstance().collection("orders")
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("orders")


    fun updateProduct(productId: String, product: Product): TableRes<Unit> {
        return try {
            collectionReference
                .document(productId)
                .set(product)
                .addOnSuccessListener {
                    Log.d("Product", "Product updated successfully")
                }
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message?: "Error updating Product")
        }
    }

}
