package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.utils.TableManager
import com.islamelmrabet.cookconnect.utils.TableRes
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Class TableViewModel
 *
 */
class TableViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _tableNumber = MutableLiveData<Int>()
    val tableNumber: LiveData<Int> = _tableNumber

    /**
     * Adds a table
     *
     * @param table
     * @param tableManager
     * @param context
     * @param onSuccess
     */
    fun addTable(
        table: Table,
        tableManager: TableManager,
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val tableExists = tableManager.tableExists(table.number).await()
            if (tableExists) {
                Toast.makeText(context, "Table already exists", Toast.LENGTH_LONG).show()
            } else {
                val result = tableManager.addTable(table).await()
                if (result) {
                    Toast.makeText(context, "Table added successfully", Toast.LENGTH_LONG).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, "Error adding table", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Fetches all tables from the database
     *
     * @return Flow<List<Table>>
     */
    fun fetchTableDataFlow(): Flow<List<Table>> = callbackFlow {
        val collectionReference = firestore.collection("tables")

        val subscription = collectionReference.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }
            snapshot?.let { querySnapshot ->
                val allTables = mutableListOf<Table>()
                for (document in querySnapshot.documents) {
                    val order = document.toObject(Table::class.java)
                    order?.let { allTables.add(it) }
                }
                trySend(allTables).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

    /**
     * Update the table order status
     *
     * @param tableNumber
     * @param tableManager
     * @param alreadyGotOrder
     */
    fun updateTableOrderStatus(
        tableNumber: Int,
        tableManager: TableManager,
        alreadyGotOrder: Boolean
    ) {
        val result = tableManager.updateTableOrderStatus(tableNumber, alreadyGotOrder)
        when (result) {
            is TableRes.Success -> {
                Log.d(
                    "Estado de la mesa sobre el pedido",
                    "Correctamente establecido a $alreadyGotOrder"
                )
            }

            is TableRes.Error -> {
                Log.d(
                    "Estado de la mesa sobre el pedido",
                    "No se puedo establecer el estado a $alreadyGotOrder de la mesa $tableNumber"
                )
            }
        }
    }

    /**
     * Update the order status in case is ready
     *
     * @param tableNumber
     * @param tableManager
     * @param isReadyOrder
     */
    fun updateReadyOrderStatus(
        tableNumber: Int,
        tableManager: TableManager,
        isReadyOrder: Boolean
    ) {
        when (tableManager.updateIsReadyOrderStatus(tableNumber, isReadyOrder)) {
            is TableRes.Success -> {
                Log.d(
                    "Estado del pedido de cocina a mesa",
                    "Correctamente establecido a $isReadyOrder"
                )
            }

            is TableRes.Error -> {
                Log.d(
                    "Estado del pedido de cocina a mesa",
                    "Correctamente establecido a $isReadyOrder"
                )
            }
        }
    }

    /**
     * Gets the table object using the table number
     *
     * @param tableNumber
     * @return Table
     */
    suspend fun getTable(tableNumber: Int): Table? {
        val productsRef = FirebaseFirestore.getInstance().collection("tables")
        val querySnapshot = productsRef.whereEqualTo("number", tableNumber).get().await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents.firstOrNull()?.toObject(Table::class.java)
        } else {
            null
        }
    }

// ----------------------------------------------------------------
// TODO: THIS FUNCITION MAY BE VALUABLE IN A FUTURE TO UPDATE TABLES
// ----------------------------------------------------------------
//    fun updateTable(originalProductName : String, product: Product, productManager: ProductManager, context: Context) {
//        viewModelScope.launch {
//            val productId = productManager.getProductDocumentIdByName(originalProductName)
//            if (productId!= null) {
//                val productName: String = product.productName
//                val quantity: Int = product.quantity
//                val unitPrice = product.unitPrice
//                val category = product.category
//                val newProduct = Product(
//                    productName = productName,
//                    unitPrice = unitPrice,
//                    quantity = quantity,
//                    category = category
//                )
//                val result = productManager.updateProduct(productId, newProduct)
//                if (result is TableRes.Success) {
//                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(context, "Error updating product", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}