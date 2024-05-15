package com.islamelmrabet.cookconnect.utils

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import java.util.concurrent.CompletableFuture

sealed class TableRes<out T> {
    data class Success<T>(val data: T) : TableRes<T>()
    data class Error(val errorMessage: String) : TableRes<Nothing>()
}

class TableManager(context: Context) {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("tables")

    fun addTable(table: Table): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        FirebaseFirestore.getInstance().collection("tables")
            .add(table)
            .addOnSuccessListener {
                Log.d("Worker", "Table created successfully")
                completableFuture.complete(true)
            }
            .addOnFailureListener { e ->
                Log.d("Worker", "Error creating table: ${e.message}")
                completableFuture.complete(false)
            }
        return completableFuture
    }

    fun deleteTable(tableKey: String): TableRes<Unit> {
        return try {
            databaseReference.child(tableKey).removeValue()
            TableRes.Success(Unit)
        } catch (e: Exception) {
            TableRes.Error(e.message ?: "Error deleting table")
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