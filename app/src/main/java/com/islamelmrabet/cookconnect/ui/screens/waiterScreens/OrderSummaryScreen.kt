package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Invoice
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.BasicLongButton
import com.islamelmrabet.cookconnect.managers.InvoiceManager
import com.islamelmrabet.cookconnect.managers.TableManager
import com.islamelmrabet.cookconnect.tools.Result
import com.islamelmrabet.cookconnect.viewModel.InvoiceViewModel
import com.islamelmrabet.cookconnect.viewModel.OrderViewModel
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel
import com.islamelmrabet.cookconnect.viewModel.TableViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Composable function OrderSummaryScreen
 *
 * @param navController
 * @param orderViewModel
 * @param productViewModel
 * @param tableViewModel
 * @param tableManager
 * @param invoiceViewModel
 * @param invoiceManager
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "DefaultLocale")
@Composable
fun OrderSummaryScreen(
    navController: NavHostController,
    orderViewModel: OrderViewModel,
    productViewModel: ProductViewModel,
    tableViewModel: TableViewModel,
    tableManager: TableManager,
    invoiceViewModel: InvoiceViewModel,
    invoiceManager: InvoiceManager,

    ) {

    val lessRoundedShape = RoundedCornerShape(8.dp)

    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = MaterialTheme.colorScheme.onPrimary,
    )

    val orderSummary by orderViewModel.orderOrderSummary.observeAsState()
    val productList by productViewModel.productList.observeAsState(emptyList())
    val context = LocalContext.current
    var isPayedByCash by remember { mutableStateOf(false) }
    val productCountMap = remember { mutableStateMapOf<Product, Int>() }
    var showDeleteDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        orderSummary?.let { orderData ->
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


    Scaffold(
        topBar = {
            AppBar(
                navController,
                stringResource(id = R.string.order_summary_header),
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                orderSummary?.let { it ->
                    OrderListSummary(
                        orderSummary = it,
                        productList = productList,
                        isPayedByCash = isPayedByCash,
                        onPayedByCashChange = { isPayedByCash = it },
                        onClick = {
                            showDeleteDialog = true
                        }
                    )
                }
                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text(text = stringResource(id = R.string.confirm_delete)) },
                        text = { Text(text = stringResource(id = R.string.confirm_delete_text)) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    productViewModel.updateProductQuantitiesSum(productCountMap, context)
                                    tableViewModel.updateTableOrderStatus(
                                        orderSummary!!.tableNumber,
                                        tableManager,
                                        false
                                    )
                                    tableViewModel.updateReadyOrderStatus(
                                        orderSummary!!.tableNumber,
                                        tableManager,
                                        false
                                    )
                                    orderViewModel.deleteOrder(orderSummary!!.tableNumber, context, true)
                                    orderViewModel.clearOrderOrderSummary()
                                    navController.navigate(Routes.TableScreen.route)
                                    showDeleteDialog = false
                                }
                            ) {
                                Text(text = stringResource(id = R.string.yes))
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDeleteDialog = false }
                            ) {
                                Text(text = stringResource(id = R.string.no))
                            }
                        }
                    )
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ),
                containerColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.subtotal),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = String.format("%.2f$", orderSummary?.price ?: 0.0),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    BasicLongButton(
                        buttonText = stringResource(id = R.string.pay),
                        onClick = {
                            if (orderSummary != null) {
                                val currentDate = SimpleDateFormat(
                                    "dd/MM/yyyy (HH:mm)",
                                    Locale.getDefault()
                                ).format(Date())
                                val invoice =
                                    Invoice(
                                        invoiceDateCreated = currentDate,
                                        payedByCash = isPayedByCash,
                                        payed = true,
                                        orderDateCreated = orderSummary!!.orderDateCreated,
                                        tableNumber = orderSummary!!.tableNumber,
                                        price = orderSummary!!.price,
                                        productQuantityMap = orderSummary!!.productQuantityMap
                                    )
                                invoiceViewModel.addInvoice(invoice, invoiceManager)
                                tableViewModel.updateTableOrderStatus(
                                    orderSummary!!.tableNumber,
                                    tableManager,
                                    false
                                )
                                tableViewModel.updateReadyOrderStatus(
                                    orderSummary!!.tableNumber,
                                    tableManager,
                                    false
                                )
                                orderViewModel.deleteOrder(
                                    orderSummary!!.tableNumber,
                                    context,
                                    false
                                )
                                orderViewModel.clearOrderOrderSummary()
                                navController.navigate(Routes.OrderSuccessfulScreen.route)
                            }
                        },
                        lessRoundedShape = lessRoundedShape,
                        buttonColors = buttonColors,
                        enabled = true
                    )
                }
            }
        }
    )
}

/**
 * Composable function that displays the lazy column of orders
 *
 * @param orderSummary
 * @param productList
 * @param onClick
 * @param onPayedByCashChange
 * @param isPayedByCash
 */
@SuppressLint("DefaultLocale")
@Composable
fun OrderListSummary(
    orderSummary: Order,
    productList: List<Product>,
    onClick: () -> Unit,
    onPayedByCashChange: (Boolean) -> Unit,
    isPayedByCash: Boolean
) {
    val productMap = productList.associateBy { it.productName }
    val productQuantityMap = orderSummary.productQuantityMap
    val productCountMap = productQuantityMap.mapNotNull { (productName, quantity) ->
        productMap[productName]?.let { it to quantity }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(productCountMap) { (product, quantity) ->
                OrderSummaryCard(product, quantity)
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(start = 45.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text =  stringResource(id = R.string.subtotal),
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = String.format("%.2f$", orderSummary.price),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            item {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 60.dp, end = 16.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(start = 45.dp)
                        .clickable {
                            onClick()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text =  stringResource(id = R.string.delete),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        textDecoration = TextDecoration.Underline
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.error
                    )
                }

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text =  stringResource(id = R.string.in_cash),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
            )
            Checkbox(
                checked = isPayedByCash,
                onCheckedChange = onPayedByCashChange
            )
        }
    }
}

/**
 * Composable function that displays the information about the order in a card.
 *
 * @param product
 * @param quantity
 */
@Composable
private fun OrderSummaryCard(product: Product, quantity: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quantity.toString(),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = product.productName,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "$${product.unitPrice}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline,
        thickness = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    )
}

