package com.islamelmrabet.cookconnect.managers

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import java.util.concurrent.CompletableFuture

/**
 * Sealed class for the result.
 *
 * @param T
 */
sealed class TableRes<out T> {
    data class Success<T>(val data: T) : TableRes<T>()
    data class Error(val errorMessage: String) : TableRes<Nothing>()
}

/**
 * Class TableManager
 *
 */
class TableManager() {
    private val databaseReference = FirebaseFirestore.getInstance().collection("tables")
    private val collectionReference = FirebaseFirestore.getInstance().collection("tables")

    /**
     *  Adds a new table
     *
     * @param table
     * @return CompletableFuture<Boolean>
     */
    fun addTable(table: Table): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        databaseReference
            .add(table)
            .addOnSuccessListener {
                Log.d("Table", "Table created successfully")
                completableFuture.complete(true)
            }
            .addOnFailureListener { e ->
                Log.d("Table", "Error creating table: ${e.message}")
                completableFuture.complete(false)
            }
        return completableFuture
    }

    /**
     * Update the field got order in case the table got an order
     *
     * @param tableNumber
     * @param alreadyGotOrder
     * @return TableRes<Unit>
     */
    fun updateTableOrderStatus(tableNumber: Int, alreadyGotOrder: Boolean): TableRes<Unit> {
        return try {
            collectionReference
                .whereEqualTo("number", tableNumber)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        Log.e("Table", "No order found with the given orderDateCreated")
                        TableRes.Error("No order found with the given orderDateCreated")
                    } else {
                        val document = querySnapshot.documents[0]
                        document.reference
                            .update("gotOrder", alreadyGotOrder)
                            .addOnSuccessListener {
                                Log.d("Table", "Table status updated successfully")
                                TableRes.Success(Unit)
                            }
                            .addOnFailureListener { e ->
                                Log.e("Table", "Error updating Table status", e)
                                TableRes.Error(e.message ?: "Error updating order status")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Order", "Error finding Table", e)
                    TableRes.Error(e.message ?: "Error finding Table")
                }
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message ?: "Error updating Table status")

        }
    }

    /**
     * Updates the field gotOrderReady if the order of a table is ready.
     *
     * @param tableNumber
     * @param orderIsReady
     * @return TableRes<Unit>
     */
    fun updateIsReadyOrderStatus(tableNumber: Int, orderIsReady: Boolean): TableRes<Unit> {
        return try {
            collectionReference
                .whereEqualTo("number", tableNumber)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        Log.e("Table", "No order found with the given orderDateCreated")
                        TableRes.Error("No order found with the given orderDateCreated")
                    } else {
                        val document = querySnapshot.documents[0]
                        document.reference
                            .update("gotOrderReady", orderIsReady)
                            .addOnSuccessListener {
                                Log.d("Table", "Table status updated successfully")
                                TableRes.Success(Unit)
                            }
                            .addOnFailureListener { e ->
                                Log.e("Table", "Error updating Table status", e)
                                TableRes.Error(e.message ?: "Error updating order status")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Order", "Error finding Table", e)
                    TableRes.Error(e.message ?: "Error finding Table")
                }
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message ?: "Error updating Table status")

        }
    }

    /**
     * This method checks if the table number already exists.
     *
     * @param tableNumber
     * @return CompletableFuture<Boolean>
     */
    fun tableExists(tableNumber: Int): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        collectionReference
            .whereEqualTo("number", tableNumber)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    completableFuture.complete(false)
                } else {
                    completableFuture.complete(true)
                }
            }
            .addOnFailureListener { e ->
                Log.e("Table", "Error checking table existence: ${e.message}")
                completableFuture.complete(false)
            }
        return completableFuture
    }
}