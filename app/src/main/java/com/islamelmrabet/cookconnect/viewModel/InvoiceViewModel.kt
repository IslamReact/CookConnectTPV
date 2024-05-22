package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Invoice
import com.islamelmrabet.cookconnect.utils.InvoiceManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class InvoiceViewModel : ViewModel(){

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("invoices")
    private val firestore = FirebaseFirestore.getInstance()

    fun addInvoice(invoice: Invoice, invoiceManager: InvoiceManager, context: Context) {
        when (val result = invoiceManager.addInvoiceManager(invoice)) {
            else -> {
            }
        }
    }

    fun fetchInvoiceDataFlow(): Flow<List<Invoice>> = callbackFlow{
        val collectionReference = firestore.collection("invoices")

        val subscription = collectionReference.addSnapshotListener{ snapshot, exception  ->
            if (exception  != null) {
                close(exception )
                return@addSnapshotListener
            }
            snapshot?.let {querySnapshot ->
                val allInvoices = mutableListOf<Invoice>()
                for (document in querySnapshot.documents){
                    val order = document.toObject(Invoice::class.java)
                    order?.let { allInvoices.add(it) }
                }
                trySend(allInvoices).isSuccess
            }
        }
        awaitClose { subscription.remove()}
    }

}