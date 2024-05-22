package com.islamelmrabet.cookconnect.utils

import android.content.Context
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Invoice
import java.util.concurrent.CompletableFuture

class InvoiceManager(context : Context) {
    private val collectionReference = FirebaseFirestore.getInstance().collection("invoices")
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("invoices")

    fun addInvoiceManager(invoice: Invoice): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()
        collectionReference
            .add(invoice)
            .addOnSuccessListener {
                Log.d("Invoice", "Invoice created successfully")
                completableFuture.complete(true)
            }
            .addOnFailureListener { e ->
                Log.d("Invoice", "Error creating invoice: ${e.message}")
                completableFuture.complete(false)
            }
        return completableFuture
    }
}