package com.islamelmrabet.cookconnect.utils

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CompletableFuture

/**
 * Class ProductManager
 *
 */
class ProductManager {

    private val collectionReference = FirebaseFirestore.getInstance().collection("products")

    /**
     * Adds a product
     *
     * @param product
     * @return CompletableFuture<Boolean>
     */
    fun addProduct(product: Product): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        collectionReference
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

    /**
     * Delete a product
     *
     * @param productId
     * @return TableRes<Unit>
     */
    fun deleteProduct(productId: String): TableRes<Unit> {
        return try {
            collectionReference
                .document(productId)
                .delete()
                .addOnSuccessListener {
                    Log.d("Product", "Product deleted successfully")
                }
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message ?: "Error deleting Product")
        }
    }

    /**
     * Update a Product
     *
     * @param productId
     * @param product
     * @return TableRes<Unit>
     */
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
            TableRes.Error(e.message ?: "Error updating Product")
        }
    }

    /**
     * Get product document id by name
     *
     * @param productName
     * @return String?
     */
    suspend fun getProductDocumentIdByName(productName: String): String? {
        return try {
            val querySnapshot = FirebaseFirestore.getInstance().collection("products")
                .whereEqualTo("productName", productName)
                .get().await()
            if (!querySnapshot.isEmpty) {
                querySnapshot.documents.firstOrNull()?.id
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("ProductManager", "Error getting product document ID: ${e.message}")
            null
        }
    }
}
