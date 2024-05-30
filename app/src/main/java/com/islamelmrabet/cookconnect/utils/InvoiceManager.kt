package com.islamelmrabet.cookconnect.utils

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Invoice
import java.util.concurrent.CompletableFuture

/**
 * Class InvoiceManager
 *
 */
class InvoiceManager {
    private val collectionReference = FirebaseFirestore.getInstance().collection("invoices")

    /**
     * Add invoice to firebase.
     *
     * @param invoice
     * @return CompletableFuture<Boolean>
     */
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