package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.SpeakerNotes
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.BasicLongButton
import com.islamelmrabet.cookconnect.tools.BasicLongButtonWithIcon
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.utils.OrderManager
import com.islamelmrabet.cookconnect.utils.TableManager
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.OrderViewModel
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel
import com.islamelmrabet.cookconnect.viewModel.TableViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DefaultLocale")
@Composable
fun OrderScreen(auth: AuthManager, navController: NavHostController, productViewModel: ProductViewModel, authViewModel: AuthViewModel, orderViewModel: OrderViewModel, orderManager: OrderManager ,tableNumber : Int?, tableViewModel: TableViewModel, tableManager: TableManager){
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val productCountMap = remember { mutableStateMapOf<Product, Int>() }
    val showDialogState = remember { mutableStateOf(false) }
    val totalPrice = remember { mutableDoubleStateOf(0.0) }
    val context = LocalContext.current
    val tableGotAnOrder = remember { mutableStateOf(Table()) }

    LaunchedEffect(Unit) {
        productViewModel.fetchProductData()
        val table = tableNumber?.let { tableViewModel.getTable(it) }
        if (table != null) {
            tableGotAnOrder.value = table
        }
        if (table != null && table.gotOrder ) {
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

    Scaffold(
        topBar = {
            AppBar(
                navController,
                stringResource(id = R.string.order_screen_header),
                Routes.TableScreen.route
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
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
                        onClick = { showDialogState.value = true },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Search,
                            contentDescription = ""
                        )
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
                        !tableGotAnOrder.value.gotOrder
                    )
                }else {
                    Toast.makeText(context, "An error Ocurred", Toast.LENGTH_LONG).show()
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .padding(16.dp),
            ) {
                BasicLongButton(
                    buttonText = "Pedir",
                    onClick = {
                        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        val productQuantityMap = productCountMap.mapKeys { (product, _) -> product.productName }
                        val orderCreated = Order(
                            orderDateCreated = currentTime,
                            tableNumber = tableNumber ?: 0,
                            price = totalPrice.doubleValue,
                            productQuantityMap = productQuantityMap
                        )
                        orderViewModel.addOrder(orderCreated,context)
                        productViewModel.updateProductQuantities(productCountMap, context)
                        if (tableNumber != null) {
                            tableViewModel.updateTableOrderStatus(
                                tableNumber = tableNumber,
                                tableManager = tableManager,
                                context = context,
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
                    buttonText = "${productCountMap.size} items",
                    secondaryText = String.format("%.2f", totalPrice.doubleValue),
                    onClick = {
                        navController.navigate(Routes.OrderSummaryScreen.route)
                    },
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = buttonColors,
                    enabled = tableGotAnOrder.value.gotOrder
                )
            }
        }
    )
}

@Composable
fun SetDataForOrder(
    productViewModel: ProductViewModel,
    selectedProducts: MutableMap<Product, Int>,
    onProductCountChanged: (Product, Int) -> Unit,
    alreadyGotAnOrder: Boolean
) {
    when (val result = productViewModel.response.value) {
        is Result.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Result.Success -> {
            ShowLazyListOfProductsForOrder(
                products = result.data,
                selectedProducts = selectedProducts,
                onProductCountChanged = onProductCountChanged,
                alreadyGotAnOrder = alreadyGotAnOrder
            )
        }
        is Result.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = result.message)
            }
        }
        is Result.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No products found")
            }
        }
    }
}


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
        modifier = Modifier
            .fillMaxSize()
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
            0.3.dp,
            MaterialTheme.colorScheme.primary
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
                    text = "Cantidad: $productCount",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {},
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.SpeakerNotes,
                        contentDescription = ""
                    )
                }
            )
            Button(
                onClick = {
                    if (selectedCount < product.quantity) {
                        onProductCountChanged(product, selectedCount + 1)
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
                        selectedProducts.remove(product)
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

fun calculateTotalPrice(productCountMap: Map<Product, Int>): Double {
    var totalPrice = 0.0
    for ((product, count) in productCountMap) {
        totalPrice += product.unitPrice * count
    }
    return totalPrice
}
