package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.BasicLongButton
import com.islamelmrabet.cookconnect.tools.BasicLongButtonWithIcon
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.managers.TableManager
import com.islamelmrabet.cookconnect.viewModel.OrderViewModel
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel
import com.islamelmrabet.cookconnect.viewModel.TableViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Composable Screen OrderScreen
 *
 * @param navController
 * @param productViewModel
 * @param orderViewModel
 * @param tableNumber
 * @param tableViewModel
 * @param tableManager
 */
@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DefaultLocale")
@Composable
fun OrderScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    orderViewModel: OrderViewModel,
    tableNumber: Int?,
    tableViewModel: TableViewModel,
    tableManager: TableManager
) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val productCountMap = remember { mutableStateMapOf<Product, Int>() }
    var chipsExpanded by remember { mutableStateOf(false) }
    val categoryOptions = listOf(
        stringResource(id = R.string.drink),
        stringResource(id = R.string.sweet),
        stringResource(id = R.string.salt),
        stringResource(id = R.string.liquor),
        stringResource(id = R.string.vegetables)
    )
    val totalPrice = remember { mutableDoubleStateOf(0.0) }
    val context = LocalContext.current
    val tableGotAnOrder = remember { mutableStateOf(Table()) }
    var selectedCategory by rememberSaveable { mutableStateOf("") }
    val totalProductCount = productCountMap.values.sum()

    LaunchedEffect(Unit) {
        productViewModel.fetchProductData()
        val table = tableNumber?.let { tableViewModel.getTable(it) }
        if (table != null) {
            tableGotAnOrder.value = table
        }
        if (table != null && table.gotOrder) {
            val order = tableNumber.let { orderViewModel.getOrder(it) }
            if (order != null) {
                orderViewModel.setOrderOrderSummary(order)
            }
            order?.let { orderData ->
                totalPrice.doubleValue = orderData.price
                val products = productViewModel.response.value
                if (products is Result.Success) {
                    val productMap = products.data.associateBy { it.productName }
                    orderData.productQuantityMap.forEach { (productName, quantity) ->
                        productMap[productName]?.let { product ->
                            productCountMap[product] = quantity
                        }
                    }
                }
            }
        }
    }

    val onProductCountChanged: (Product, Int) -> Unit = { product, count ->
        productCountMap[product] = count
        totalPrice.doubleValue = calculateTotalPrice(productCountMap)
    }

    Scaffold(topBar = {
        AppBar(
            navController,
            stringResource(id = R.string.order_screen_header),
        )
    }, content = { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 10.dp,
                    end = 15.dp,
                    bottom = 10.dp
                )
            ) {
                Icon(imageVector = Icons.Outlined.NoteAlt, contentDescription = "table")
                Text(
                    text = stringResource(id = R.string.order_products_header),
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier
                        .size(1.dp, 30.dp)
                        .background(color = Color.Gray)
                        .fillMaxHeight()
                )
                Spacer(modifier = Modifier.width(15.dp))
                IconButton(
                    onClick = {
                        chipsExpanded = !chipsExpanded
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = if (chipsExpanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                        contentDescription = ""
                    )
                }
            }
            if (chipsExpanded) {
                FlowRow(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .wrapContentHeight()
                ) {
                    categoryOptions.forEach { category ->
                        FilterChip(
                            selected = category == selectedCategory,
                            onClick = {
                                selectedCategory = if (selectedCategory == category) "" else category
                            },
                            label = { Text(text = category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp)
                    .shadow(5.dp)
                    .height(1.dp)
                    .background(color = Color.Transparent)
            )
            if (tableNumber != null) {
                SetDataForOrder(
                    productViewModel,
                    productCountMap,
                    onProductCountChanged,
                    !tableGotAnOrder.value.gotOrder,
                    selectedCategory
                )
            } else {
                Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show()
            }
        }
    }, bottomBar = {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            BasicLongButton(
                buttonText = stringResource(id = R.string.sent_to_kitchen),
                onClick = {
                    val currentTime =
                        SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    val productQuantityMap =
                        productCountMap.mapKeys { (product, _) -> product.productName }
                    val orderCreated = Order(
                        orderDateCreated = currentTime,
                        tableNumber = tableNumber ?: 0,
                        price = totalPrice.doubleValue,
                        productQuantityMap = productQuantityMap
                    )
                    orderViewModel.addOrder(orderCreated, context)
                    productViewModel.updateProductQuantities(productCountMap, context)
                    if (tableNumber != null) {
                        tableViewModel.updateTableOrderStatus(
                            tableNumber = tableNumber,
                            tableManager = tableManager,
                            alreadyGotOrder = true
                        )
                    }
                    navController.navigate(Routes.TableScreen.route)
                },
                lessRoundedShape = lessRoundedShape,
                buttonColors = buttonColors,
                enabled = !tableGotAnOrder.value.gotOrder && productCountMap.size > 0
            )
            Spacer(modifier = Modifier.height(10.dp))
            BasicLongButtonWithIcon(
                buttonText = "$totalProductCount items",
                secondaryText = String.format("%.2f $", totalPrice.doubleValue),
                onClick = {
                    navController.navigate(Routes.OrderSummaryScreen.route)
                },
                lessRoundedShape = lessRoundedShape,
                buttonColors = buttonColors,
                enabled = tableGotAnOrder.value.gotOrder
            )
        }
    })
}


/**
 * Composable function that return the result of the list of products
 *
 * @param productViewModel
 * @param selectedProducts
 * @param onProductCountChanged
 * @param alreadyGotAnOrder
 * @param selectedCategory
 */
@Composable
fun SetDataForOrder(
    productViewModel: ProductViewModel,
    selectedProducts: MutableMap<Product, Int>,
    onProductCountChanged: (Product, Int) -> Unit,
    alreadyGotAnOrder: Boolean,
    selectedCategory: String
) {
    when (val result = productViewModel.response.value) {
        is Result.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                val composition by rememberLottieComposition(
                    spec = LottieCompositionSpec.Url("https://lottie.host/28d9d00d-fd59-47a3-9274-84ae50025090/NQicxfwezK.json")
                )
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
            }
        }

        is Result.Success -> {
            val filteredProducts = if (selectedCategory.isNotEmpty()) {
                result.data.filter { it.category == selectedCategory }
            } else {
                result.data
            }

            if (filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.Url("https://lottie.host/4cda72c3-0622-4905-968e-509757c3368b/eqpZmzRMHU.json")
                    )
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )
                }
                Text(text = stringResource(id = R.string.products_not_found))
            } else {
                ShowLazyListOfProductsForOrder(
                    products = filteredProducts,
                    selectedProducts = selectedProducts,
                    onProductCountChanged = onProductCountChanged,
                    alreadyGotAnOrder = alreadyGotAnOrder
                )
            }
        }

        is Result.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = result.message)
            }
        }

        is Result.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.products_not_found))
            }
        }
    }
}


/**
 * Composable function that displays the lazyColumn of the list of products.
 *
 * @param products
 * @param selectedProducts
 * @param onProductCountChanged
 * @param alreadyGotAnOrder
 */
@Composable
fun ShowLazyListOfProductsForOrder(
    products: List<Product>,
    selectedProducts: MutableMap<Product, Int>,
    onProductCountChanged: (Product, Int) -> Unit,
    alreadyGotAnOrder: Boolean
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ProductCardForOrder(
                product = product,
                selectedProducts = selectedProducts,
                productCount = selectedProducts[product] ?: 0,
                onProductCountChanged = onProductCountChanged,
                alreadyGotAnOrder = alreadyGotAnOrder,
            )
        }
    }
}

/**
 * Composable function that displays the information of the product in a card
 *
 * @param product
 * @param selectedProducts
 * @param productCount
 * @param onProductCountChanged
 * @param alreadyGotAnOrder
 */
@Composable
fun ProductCardForOrder(
    product: Product,
    selectedProducts: MutableMap<Product, Int>,
    productCount: Int,
    onProductCountChanged: (Product, Int) -> Unit,
    alreadyGotAnOrder: Boolean
) {
    val selectedCount = selectedProducts[product] ?: 0

    Card(
        modifier = Modifier.padding(4.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        border = BorderStroke(
            0.3.dp, MaterialTheme.colorScheme.primary
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
            contentColor = MaterialTheme.colorScheme.scrim,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
            disabledContentColor = MaterialTheme.colorScheme.outline
        ),
    ) {
        Row(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Column {
                Text(
                    text = product.productName,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = stringResource(id = R.string.quantity_with_field, productCount),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (selectedCount < product.quantity) {
                        val newCount = selectedCount + 1
                        selectedProducts[product] = newCount
                        onProductCountChanged(product, newCount)
                    }
                },
                shape = RoundedCornerShape(5.dp),
                enabled = alreadyGotAnOrder && product.quantity > 0,
                modifier = Modifier.size(24.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Sharp.Add,
                    contentDescription = "Add product to list",
                    modifier = Modifier.size(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    if (selectedCount > 0) {
                        val updatedMap = selectedProducts.toMutableMap()
                        updatedMap[product] = selectedCount - 1
                        onProductCountChanged(product, selectedCount - 1)
                        if(updatedMap[product] == 0){
                            selectedProducts.remove(product)
                        }
                    }
                },
                shape = RoundedCornerShape(5.dp),
                enabled = alreadyGotAnOrder && selectedCount > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceDim
                ),
                modifier = Modifier.size(24.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "Remove product from list",
                    modifier = Modifier.size(10.dp)
                )
            }
        }

    }
}

/**
 * Returns the total price of all selected products.
 *
 * @param productCountMap
 * @return Double
 */
fun calculateTotalPrice(productCountMap: Map<Product, Int>): Double {
    var totalPrice = 0.0
    for ((product, count) in productCountMap) {
        totalPrice += product.unitPrice * count
    }
    return totalPrice
}
