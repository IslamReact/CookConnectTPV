package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.utils.OrderManager
import kotlinx.coroutines.tasks.await


class OrderViewModel : ViewModel() {

    private val orderManager = OrderManager()

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    fun setTotalPrice(totalPriceOrder: Double) {
        _totalPrice.value = totalPriceOrder
    }

    fun restTotalPrice(totalPriceOrder: Double) {
        _totalPrice.value = _totalPrice.value?.minus(totalPriceOrder)
    }

    fun addOrder(order: Order, orderManager: OrderManager, context: Context) {
        when (val result = orderManager.addOrderManager(order)) {
            else -> {
                Toast.makeText(context, "Order successfully sent to kitchen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    suspend fun getOrder(tableNumber: Int): Order? {
        val productsRef = FirebaseFirestore.getInstance().collection("orders")
        val querySnapshot = productsRef.whereEqualTo("tableNumber", tableNumber).get().await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents.firstOrNull()?.toObject(Order::class.java)
        } else {
            null
        }
    }
}
