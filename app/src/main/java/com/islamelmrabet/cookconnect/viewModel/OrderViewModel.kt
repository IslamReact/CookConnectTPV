package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.utils.OrderManager
import kotlinx.coroutines.launch


class OrderViewModel : ViewModel() {

    private val orderManager = OrderManager()

    private val _orders = MutableLiveData<MutableList<Order>>().apply {
        value = mutableListOf()
    }

    val orders: LiveData<MutableList<Order>> = _orders

    init {
        fetchOrders()
    }

    private fun fetchOrders() {
        viewModelScope.launch {
            val orderList = orderManager.getOrders()
            _orders.value = orderList.toMutableList()
        }
    }

    fun addOrder(order: Order) {
        viewModelScope.launch {
            orderManager.addOrder(order)
            fetchOrders() // Refresh orders after adding
        }
    }

    fun updateOrder(order: Order) {
        viewModelScope.launch {
            orderManager.updateOrder(order)
            fetchOrders() // Refresh orders after updating
        }
    }

    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            orderManager.deleteOrder(order)
            fetchOrders() // Refresh orders after deleting
        }
    }

    fun clearOrders() {
        _orders.value?.clear()
        _orders.value = _orders.value
    }
}
