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
import com.google.firebase.firestore.FirebaseFirestore
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.utils.ProductManager
import com.islamelmrabet.cookconnect.utils.TableRes
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

/**
 * Class ProductViewModel
 *
 */
class ProductViewModel : ViewModel() {

    private val productManager = ProductManager()
    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> get() = _productList

    val response: MutableState<Result<Product>> = mutableStateOf(Result.Empty)

    init {
        fetchProductData()
    }

    /**
     * Adds a product
     *
     * @param product
     * @param productManager
     * @param context
     */
    fun addProduct(product: Product, productManager: ProductManager, context: Context) {
        when (productManager.addProduct(product)) {
            else -> {
                Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Deletes the product
     *
     * @param productName
     * @param productManager
     * @param context
     */
    fun deleteProduct(productName: String, productManager: ProductManager, context: Context) {
        viewModelScope.launch {
            val productId = productManager.getProductDocumentIdByName(productName)
            if (productId != null) {
                val result = productManager.deleteProduct(productId)
                if (result is TableRes.Success) {
                    Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Error deleting product", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Update the product
     *
     * @param originalProductName
     * @param product
     * @param productManager
     * @param context
     */
    fun updateProduct(
        originalProductName: String,
        product: Product,
        productManager: ProductManager,
        context: Context
    ) {
        viewModelScope.launch {
            val productId = productManager.getProductDocumentIdByName(originalProductName)
            if (productId != null) {
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
                    Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Error updating product", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Fetches all product data in a live data list.
     *
     */
    fun fetchProductData() {
        response.value = Result.Loading
        val productTempList = mutableListOf<Product>()
        val db = FirebaseFirestore.getInstance()
        val mQuery = db.collection("products")

        mQuery.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val productItem = document.toObject(Product::class.java)
                    productTempList.add(productItem)
                }
                _productList.value = productTempList
                response.value = Result.Success(productTempList)
                Log.d("FetchProductData", "Products fetched successfully: ${productTempList.size}")
            } else {
                Log.e("FetchProductData", "Error fetching products", task.exception)
                response.value =
                    Result.Failure("Error fetching products: ${task.exception?.message}")
            }
        }
    }

    /**
     * Get product using his name.
     *
     * @param productName
     * @return Product
     */
    suspend fun getProduct(productName: String): Product? {
        val productsRef = FirebaseFirestore.getInstance().collection("products")
        val querySnapshot = productsRef.whereEqualTo("productName", productName).get().await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents.firstOrNull()?.toObject(Product::class.java)
        } else {
            null
        }
    }

    /**
     * Update all products quantity from a list.
     *
     * @param productCountMap
     * @param context
     */
    fun updateProductQuantities(productCountMap: Map<Product, Int>, context: Context) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            productCountMap.forEach { (product, count) ->
                val newQuantity = product.quantity - count
                if (newQuantity >= 0) {
                    val productId = productManager.getProductDocumentIdByName(product.productName)
                    if (productId != null) {
                        val productRef = db.collection("products").document(productId)
                        productRef.update("quantity", newQuantity).addOnSuccessListener {
                            Log.d("UpdateProduct", "Product quantity updated successfully")
                        }.addOnFailureListener { e ->
                            Log.e("UpdateProduct", "Error updating product quantity", e)
                            Toast.makeText(
                                context,
                                "Error updating product quantity",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Not enough quantity for product: ${product.productName}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /**
     * Cheks if the product already exists
     *
     * @param productName
     * @return Boolean
     */
    suspend fun productExists(productName: String): Boolean {
        val normalizedProductName = productName.trim().lowercase(Locale.ROOT)
        val productsRef = FirebaseFirestore.getInstance().collection("products")
        val querySnapshot = productsRef.get().await()
        for (document in querySnapshot.documents) {
            val product = document.toObject(Product::class.java)
            if (product != null && product.productName.trim()
                    .lowercase(Locale.ROOT) == normalizedProductName
            ) {
                return true
            }
        }
        return false
    }
}