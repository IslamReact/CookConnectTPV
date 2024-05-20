package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.utils.OrderManager


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

//    fun addOrder(order: Order, orderManager: OrderManager, context: Context) {
//        when (val result = orderManager.addOrderManager(order)) {
//            else -> {
//                Toast.makeText(context, "Order successfully sent to kitchen", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    fun addProductToList( selectedProducts: MutableList<Product>, product: Product) {
        selectedProducts.add(product)
        _totalPrice.value = _totalPrice.value?.plus(product.unitPrice)
    }

    fun removeProductFromList( selectedProducts: MutableList<Product>, product: Product) {
        selectedProducts.remove(product)
        if (selectedProducts.size != 0) {
            _totalPrice.value = _totalPrice.value?.minus(product.unitPrice)
        }
    }

}
