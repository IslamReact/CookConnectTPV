package com.islamelmrabet.cookconnect.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.tools.Result
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class OrderCookerViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    val response: MutableState<Result<Order>> = mutableStateOf(Result.Empty)

    fun fetchOrderDataFlow(): Flow<List<Order>> = callbackFlow{
        val collectionReference = firestore.collection("orders")
            .whereEqualTo("ready", false)

        val subscription = collectionReference.addSnapshotListener{ snapshot, exception  ->
            if (exception  != null) {
                close(exception )
                return@addSnapshotListener
            }
            snapshot?.let {querySnapshot ->
                val allOrders = mutableListOf<Order>()
                for (document in querySnapshot.documents){
                    val order = document.toObject(Order::class.java)
                    order?.let { allOrders.add(it) }
                }
                trySend(allOrders).isSuccess
            }
        }
        awaitClose { subscription.remove()}
    }

}