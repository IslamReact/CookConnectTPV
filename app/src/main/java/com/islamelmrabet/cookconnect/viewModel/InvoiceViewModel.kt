package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Invoice
import com.islamelmrabet.cookconnect.managers.InvoiceManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Class InvoiceViewModel
 *
 */
class InvoiceViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    /**
     * Adds Invoice
     *
     * @param invoice
     * @param invoiceManager
     */
    fun addInvoice(invoice: Invoice, invoiceManager: InvoiceManager) {
        return when (val result = invoiceManager.addInvoiceManager(invoice)) {
            else -> {
            }
        }
    }

    /**
     * Fetch Invoice Data Flow
     *
     * @return Flow<List<Invoice>>
     */
    fun fetchInvoiceDataFlow(): Flow<List<Invoice>> = callbackFlow {
        val collectionReference = firestore.collection("invoices")

        val subscription = collectionReference.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }
            snapshot?.let { querySnapshot ->
                val allInvoices = mutableListOf<Invoice>()
                for (document in querySnapshot.documents) {
                    val order = document.toObject(Invoice::class.java)
                    order?.let { allInvoices.add(it) }
                }
                trySend(allInvoices).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

}