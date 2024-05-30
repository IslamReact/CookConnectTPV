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

/**
 * Class OrderViewModel
 *
 */
class OrderViewModel : ViewModel() {

    private val orderManager = OrderManager()

    private val _orderOrderSummary = MutableLiveData<Order>()
    val orderOrderSummary: LiveData<Order> = _orderOrderSummary

    /**
     * Set order status
     *
     * @param order
     */
    fun setOrderOrderSummary(order: Order) {
        _orderOrderSummary.value = order
    }

    /**
     * Adds an order
     *
     * @param order
     * @param context
     */
    fun addOrder(order: Order, context: Context) {
        orderManager.addOrderManager(order)
            .thenAccept { success ->
                if (success) {
                    Toast.makeText(
                        context,
                        "Order successfully sent to kitchen",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(context, "Failed to send order to kitchen", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    /**
     * Gets the order using the table number
     *
     * @param tableNumber
     * @return Order
     */
    suspend fun getOrder(tableNumber: Int): Order? {
        val productsRef = FirebaseFirestore.getInstance().collection("orders")
        val querySnapshot = productsRef.whereEqualTo("tableNumber", tableNumber).get().await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents.firstOrNull()?.toObject(Order::class.java)
        } else {
            null
        }
    }

    /**
     * Delete the order using the table number
     *
     * @param tableNumber
     * @param context
     * @param isForDelete
     */
    fun deleteOrder(tableNumber: Int, context: Context, isForDelete: Boolean) {
        orderManager.deleteOrderManager(tableNumber)
            .thenAccept { success ->
                if (success) {
                    if (isForDelete) {
                        Toast.makeText(context, "Order successfully deleted", Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        Toast.makeText(context, "Order successfully completed", Toast.LENGTH_SHORT)
                            .show()

                    }
                } else {
                    Toast.makeText(context, "Failed to delete order", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * Clears all the order summary data.
     *
     */
    fun clearOrderOrderSummary() {
        _orderOrderSummary.value = Order()
    }
}
