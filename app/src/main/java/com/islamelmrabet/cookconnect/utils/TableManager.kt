package com.islamelmrabet.cookconnect.utils

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import java.util.concurrent.CompletableFuture


sealed class TableRes<out T> {
    data class Success<T>(val data: T) : TableRes<T>()
    data class Error(val errorMessage: String) : TableRes<Nothing>()
}

class TableManager(context: Context) {
    private val databaseReference = FirebaseFirestore.getInstance().collection("tables")
    private val collectionReference = FirebaseFirestore.getInstance().collection("tables")


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

    fun updateTableOrderStatus(tableNumber: Int, alreadyGotOrder : Boolean): TableRes<Unit> {
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

    fun updateIsReadyOrderStatus(tableNumber: Int, orderIsReady : Boolean): TableRes<Unit> {
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


// ----------------------------------------------------------------
    // TODO: THIS FUNCITION MAY BE VALUABLE IN A FUTURE TO IMPLEMENT UID IN TABLES
// ----------------------------------------------------------------
//    fun isUidExists(uid: String, callback: (Boolean) -> Unit) {
//        databaseReference.orderByChild("uid").equalTo(uid)
//            .addListenerForSingleValueEvent(object :
//                ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    callback(snapshot.exists())
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    callback(false)
//                }
//            }
//        )
//    }
}