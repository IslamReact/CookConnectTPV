package com.islamelmrabet.cookconnect.ui.screens.cookerScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Order
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.CookerAndWaiterAppBar
import com.islamelmrabet.cookconnect.tools.DrawerHeader
import com.islamelmrabet.cookconnect.tools.DrawerFooter
import com.islamelmrabet.cookconnect.managers.AuthManager
import com.islamelmrabet.cookconnect.managers.OrderCookerManager
import com.islamelmrabet.cookconnect.managers.TableManager
import com.islamelmrabet.cookconnect.model.localModels.getCookerNavigationItems
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.MainViewModel
import com.islamelmrabet.cookconnect.viewModel.OrderCookerViewModel
import com.islamelmrabet.cookconnect.viewModel.TableViewModel
import kotlinx.coroutines.launch

/**
 * Composable screen OrderCookerScreen
 *
 * @param auth
 * @param navController
 * @param authViewModel
 * @param orderCookerViewModel
 * @param orderCookerManager
 * @param mainViewModel
 * @param tableViewModel
 * @param tableManager
 */
@Composable
fun OrderCookerScreen(
    auth: AuthManager,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    orderCookerViewModel: OrderCookerViewModel,
    orderCookerManager: OrderCookerManager,
    mainViewModel: MainViewModel,
    tableViewModel: TableViewModel,
    tableManager: TableManager
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex by mainViewModel.drawerSelectedIndex.collectAsState()
    var lastLogInDate by rememberSaveable { mutableStateOf("") }
    val allOrders by orderCookerViewModel.fetchOrderDataFlow().collectAsState(initial = emptyList())
    val context = LocalContext.current
    val cookerNavigationItem = getCookerNavigationItems()
    val logOutText = stringResource(id = R.string.logOut)

    LaunchedEffect(Unit) {
        val fetchedLastLoginDate = authViewModel.getLastLoginDate()
        fetchedLastLoginDate?.let { lastLogInDate = it }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primary
            ) {
                DrawerHeader()
                cookerNavigationItem.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            if (item.title == logOutText) {
                                auth.signOut()
                                Log.d("LogOut event", "Successfully logged out")
                                navController.popBackStack()
                                navController.navigate(Routes.WelcomeScreen.route) {
                                    popUpTo(Routes.WelcomeScreen.route) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                navController.navigate(item.route)

                            }
                            mainViewModel.updateSelectedIndex(index)
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = MaterialTheme.colorScheme.inversePrimary,
                            unselectedContainerColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        shape = RoundedCornerShape(1.dp)
                    )
                }
                Spacer(modifier = Modifier.height(275.dp))
                DrawerFooter(lastLogInDate)
            }
        },
    ) {
        Scaffold(
            topBar = {
                CookerAndWaiterAppBar(
                    stringResource(id = R.string.order_search_header),
                    onClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
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
                            .padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 15.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.Coffee, contentDescription = "table")
                        Text(
                            text = stringResource(id = R.string.order_screen_header),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp)
                            .shadow(5.dp)
                            .height(1.dp)
                            .background(color = Color.Transparent)
                    )
                    ShowLazyListOfOrders(
                        allOrders,
                        orderCookerViewModel,
                        context,
                        orderCookerManager,
                        tableViewModel,
                        tableManager
                    )
                }
            }
        )
    }
}

/**
 * Composable function that displays the lazy Column of Orders.
 *
 * @param orders
 * @param orderCookerViewModel
 * @param context
 * @param orderCookerManager
 * @param tableViewModel
 * @param tableManager
 */
@Composable
fun ShowLazyListOfOrders(
    orders: List<Order>,
    orderCookerViewModel: OrderCookerViewModel,
    context: Context,
    orderCookerManager: OrderCookerManager,
    tableViewModel: TableViewModel,
    tableManager: TableManager
) {
    if (orders.isEmpty()) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.Url("https://lottie.host/b1a6675e-378f-4df7-9ad7-05a74a1676f1/Z0Cdlmwi4Z.json")
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.relax_text),
                modifier = Modifier
                    .padding(top = 100.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 30.sp,
                letterSpacing = 0.5.sp,
            )
            Text(
                text = stringResource(id = R.string.no_orders_text),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 30.sp,
                letterSpacing = 0.5.sp,
            )
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            orders.forEachIndexed { _, order ->
                item {
                    var visible by remember { mutableStateOf(true) }
                    OrderCard(order, onOrderReadyClick = {
                        orderCookerViewModel.updateOrderReadyStatus(
                            order.orderDateCreated,
                            orderCookerManager,
                            context
                        )
                        tableViewModel.updateReadyOrderStatus(
                            order.tableNumber,
                            tableManager,
                            true
                        )
                        visible = false

                    })
                }
            }
        }
    }
}

/**
 * Composable function that displays the order card.
 *
 * @param order
 * @param onOrderReadyClick
 */
@Composable
fun OrderCard(order: Order, onOrderReadyClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .heightIn(min = 100.dp, max = 1000.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        border = BorderStroke(
            0.3.dp,
            MaterialTheme.colorScheme.primary
        ),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
            contentColor = MaterialTheme.colorScheme.scrim,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
            disabledContentColor = MaterialTheme.colorScheme.outline
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Mesa ${order.tableNumber}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Text(
                    text = order.orderDateCreated,
                    Modifier.padding(start = 10.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                order.productQuantityMap.entries.forEach { (product, quantity) ->
                    ProductRow(product = product, quantity = quantity)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onOrderReadyClick,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.outline,
                    disabledContentColor = MaterialTheme.colorScheme.outline
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = stringResource(id = R.string.ready))
            }
        }
    }
}

/**
 * Composable that displays the information of the order.
 *
 * @param product
 * @param quantity
 */
@Composable
fun ProductRow(product: String, quantity: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)
                .size(20.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quantity.toString(),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = product,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}
