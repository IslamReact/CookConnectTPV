package com.islamelmrabet.cookconnect.viewModel

import androidx.lifecycle.ViewModel
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.utils.ProductManager
import com.islamelmrabet.cookconnect.utils.TableManager
import com.islamelmrabet.cookconnect.utils.TableRes
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TableViewModel : ViewModel() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("tables")

    val response: MutableState<Result<Table>> = mutableStateOf(Result.Empty)
    init {
        fetchTableData()
    }

    fun addTable(table: Table, tableManager: TableManager, context: Context,onSuccess: () -> Unit) {
        when (val result = tableManager.addTable(table)) {
            else -> {
                Toast.makeText(context, "Table added successfully", Toast.LENGTH_LONG).show()
                onSuccess()
            }
        }
    }

    fun deleteTable(productName: String, productManager: ProductManager, context: Context) {
        viewModelScope.launch {
            val productId = productManager.getProductDocumentIdByName(productName)
            if (productId != null) {
                val result = productManager.deleteProduct(productId)
                if (result is TableRes.Success) {
                    Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error deleting product", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateTable(originalProductName : String,product: Product, productManager: ProductManager, context: Context) {
        viewModelScope.launch {
            val productId = productManager.getProductDocumentIdByName(originalProductName)
            if (productId!= null) {
                val productName: String = product.productName
                val quantity: Int = product.quantity
                val unitPrice = product.unitPrice
                val category = product.category
                val newProduct = Product(
                    productName = productName,
                    unitPrice = unitPrice,
                    quantity = quantity,
                    category = category
                )
                val result = productManager.updateProduct(productId, newProduct)
                if (result is TableRes.Success) {
                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error updating product", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun fetchTableData() {
        response.value = Result.Loading
        val tableTempList = mutableListOf<Table>()
        val db = FirebaseFirestore.getInstance()
        val mQuery = db.collection("tables")

        mQuery.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val tableItenm = document.toObject(Table::class.java)
                    tableTempList.add(tableItenm)
                }
                response.value = Result.Success(tableTempList)
                Log.d("FetchProductData", "Tables fetched successfully: ${tableTempList.size}")
            } else {
                Log.e("FetchProductData", "Error fetching tables", task.exception)
                response.value = Result.Failure("Error fetching tables: ${task.exception?.message}")
            }
        }
    }

    suspend fun getProduct(productName: String): Product? {
        val productsRef = FirebaseFirestore.getInstance().collection("products")
        val querySnapshot = productsRef.whereEqualTo("productName", productName).get().await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents.firstOrNull()?.toObject(Product::class.java)
        } else {
            null
        }
    }

}