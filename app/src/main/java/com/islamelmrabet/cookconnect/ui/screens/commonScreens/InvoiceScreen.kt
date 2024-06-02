package com.islamelmrabet.cookconnect.ui.screens.commonScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Invoice
import com.islamelmrabet.cookconnect.model.localModels.NavigationItem
import com.islamelmrabet.cookconnect.model.localModels.cookerNavigationItem
import com.islamelmrabet.cookconnect.model.localModels.navigationItems
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.CookerAndWaiterAppBar
import com.islamelmrabet.cookconnect.tools.DrawerHeader
import com.islamelmrabet.cookconnect.tools.DrawerFooter
import com.islamelmrabet.cookconnect.managers.AuthManager
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.InvoiceViewModel
import com.islamelmrabet.cookconnect.viewModel.MainViewModel
import kotlinx.coroutines.launch

/**
 * Composable Screen InvoiceScreen
 *
 * @param auth
 * @param navController
 * @param authViewModel
 * @param mainViewModel
 * @param invoiceManager
 * @param invoiceViewModel
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InvoiceScreen(
    auth: AuthManager,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel,
    invoiceViewModel: InvoiceViewModel
) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex by mainViewModel.drawerSelectedIndex.collectAsState()
    var lastLogInDate by rememberSaveable { mutableStateOf("") }
    var workerName by rememberSaveable { mutableStateOf("") }
    var workerID by rememberSaveable { mutableStateOf("") }
    var workerEmail by rememberSaveable { mutableStateOf("") }
    var workerRole by rememberSaveable { mutableStateOf("") }
    var currentWorkerMenu by rememberSaveable { mutableStateOf<List<NavigationItem>>(emptyList()) }
    val allInvoices by invoiceViewModel.fetchInvoiceDataFlow().collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        val fetchedLastLoginDate = authViewModel.getLastLoginDate()
        fetchedLastLoginDate?.let { lastLogInDate = it }

        val fetchedWorkerName = authViewModel.getUsername()
        fetchedWorkerName?.let { workerName = it }

        val fetchedWorkerId = authViewModel.getUserID()
        fetchedWorkerId?.let { workerID = it }

        val fetchedWorkerEmail = authViewModel.getEmail()
        fetchedWorkerEmail?.let { workerEmail = it }

        val fetchedRole = authViewModel.getRole()
        fetchedRole?.let {
            workerRole = it
            currentWorkerMenu = if (workerRole == "Camarero") {
                navigationItems
            } else {
                cookerNavigationItem
            }
        }
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primary
            ) {
                DrawerHeader()
                currentWorkerMenu.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            if (item.title == "Cerrar Sesion") {
                                auth.signOut()
                                Log.d("LogOut event", "Succesfully logged out")
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
                    stringResource(id = R.string.invoice_screen_header),
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
                            .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountBalance,
                            contentDescription = "account balance"
                        )
                        Text(
                            text = stringResource(id = R.string.invoice_search_header),
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
                            onClick = { },
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

                    ShowLazyListOfInvoices(allInvoices)
                }
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .height(50.dp)
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    ShowTotalPrice(allInvoices)
                }
            }
        )
    }
}

/**
 * Composable function that contains the lazy Column of the list of invoices
 *
 * @param invoices
 */
@Composable
fun ShowLazyListOfInvoices(
    invoices: List<Invoice>,
) {
    if (invoices.isEmpty()) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.Url("https://lottie.host/3e5d3a68-0a7d-4e34-bc01-47e4233b5a2f/L0fjp8aoNm.json")
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
    } else {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(invoices) { invoice ->
                var isExpanded by remember { mutableStateOf(false) }
                InvoiceCard(invoice, isExpanded) {
                    isExpanded = !isExpanded
                }
            }
        }
    }
}

/**
 * Composable function that displays the design of the invoice card
 *
 * @param invoice
 * @param isExpanded
 * @param onCardClick
 */
@Composable
fun InvoiceCard(invoice: Invoice, isExpanded: Boolean, onCardClick: () -> Unit) {
    val baseHeight = 70.dp
    val extraHeightPerItem = 30.dp
    val targetHeight =
        if (isExpanded) baseHeight + (invoice.productQuantityMap.size * extraHeightPerItem) else baseHeight

    val cardHeight by animateDpAsState(targetValue = targetHeight, label = "")

    Card(
        modifier = Modifier
            .clickable { onCardClick() }
            .height(cardHeight),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
            contentColor = MaterialTheme.colorScheme.scrim,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
            disabledContentColor = MaterialTheme.colorScheme.outline
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "$${invoice.price}",
                        fontSize = 25.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = invoice.invoiceDateCreated.toString(),
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .width(100.dp)
                        .background(
                            color = if (!invoice.isPayed) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.error
                            },
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (!invoice.isPayed) "Pagado" else "No pagado",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            if (isExpanded) {
                invoice.productQuantityMap.forEach { (product, quantity) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = quantity.toString(),
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                        Text(
                            modifier = Modifier.padding(top = 5.dp, start = 5.dp),
                            text = product,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShowTotalPrice(invoices: List<Invoice>) {
    val totalPriceInCash = invoices.filter { it.isPayedByCash }.sumOf { it.price }
    val totalPrice = invoices.filter { !it.isPayedByCash }.sumOf { it.price }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row {
            Text(
                text = "Total en efectvo: ",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = String.format("$%.2f", totalPriceInCash),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Row {
            Text(
                text = "Total en tarjeta: ",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = String.format("$%.2f", totalPrice),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }
}
