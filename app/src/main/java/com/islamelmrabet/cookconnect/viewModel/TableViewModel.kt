package com.islamelmrabet.cookconnect.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import com.islamelmrabet.cookconnect.managers.TableManager
import com.islamelmrabet.cookconnect.managers.TableRes
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
                Toast.makeText(context, R.string.table_exists, Toast.LENGTH_LONG).show()
            } else {
                val result = tableManager.addTable(table).await()
                if (result) {
                    Toast.makeText(context, R.string.table_added_succesfully, Toast.LENGTH_LONG).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, R.string.error_table, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Deletes the table
     *
     * @param tableNumber
     * @param tableManager
     * @param context
     */
    fun deleteTable(tableNumber: Int, tableManager: TableManager, context: Context) {
        viewModelScope.launch {
            val tableId = tableManager.getTableDocumentIdByName(tableNumber)
            if (tableId != null) {
                val result = tableManager.deleteTable(tableId)
                if (result is TableRes.Success) {
                    Toast.makeText(context,  R.string.table_deleted, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context,  R.string.table_not_deleted, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, R.string.table_not_found, Toast.LENGTH_SHORT).show()
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
                    "Order status",
                    "Order successfully updated to$alreadyGotOrder"
                )
            }

            is TableRes.Error -> {
                Log.d(
                    "Order status",
                    "Failed to set $alreadyGotOrder status to table $tableNumber"
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
                    "Order status",
                    "Successfully set to $isReadyOrder"
                )
            }

            is TableRes.Error -> {
                Log.d(
                    "Order status",
                    "Failed to set $isReadyOrder status"
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
}