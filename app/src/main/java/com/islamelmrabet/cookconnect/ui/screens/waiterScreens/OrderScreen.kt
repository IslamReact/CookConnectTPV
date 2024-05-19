package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.SpeakerNotes
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.NoteAlt
import androidx.compose.material.icons.outlined.SpeakerNotes
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.model.localModels.navigationItems
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.BasicLongButton
import com.islamelmrabet.cookconnect.tools.BasicLongButtonWithIcon
import com.islamelmrabet.cookconnect.tools.CookerAndWaiterAppBar
import com.islamelmrabet.cookconnect.tools.DrawerHeader
import com.islamelmrabet.cookconnect.tools.HeaderFooter
import com.islamelmrabet.cookconnect.tools.OutlinedBasicButton
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel
import com.islamelmrabet.cookconnect.tools.Result
import kotlinx.coroutines.launch



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderScreen(auth: AuthManager, navController: NavHostController, productViewModel: ProductViewModel, authViewModel: AuthViewModel){
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val showDialogState = remember { mutableStateOf(false) }
    var lastLogInDate by rememberSaveable { mutableStateOf("") }

    val selectedProducts = remember { mutableStateListOf<Product>() }


    LaunchedEffect(Unit) {
        val fetchedLastLoginDate = authViewModel.getLastLoginDate()
        fetchedLastLoginDate?.let { lastLogInDate = it }
        productViewModel.fetchProductData()
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
                SetDataForOrder(productViewModel, selectedProducts )
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

                    },
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = buttonColors,
                    enabled = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                BasicLongButtonWithIcon(
                    buttonText = "${selectedProducts.size} items",
                    onClick = {

                    },
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = buttonColors,
                    enabled = true
                )
            }
        }
    )
}


@Composable
fun SetDataForOrder(productViewModel: ProductViewModel, selectedProducts: MutableList<Product>) {

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
            ShowLazyListOfProductsForOrder(result.data, selectedProducts)
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
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Unknown error")
            }
        }
    }
}

@Composable
fun ShowLazyListOfProductsForOrder(products: List<Product>, selectedProducts: MutableList<Product>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(products) { product ->
            ProductCardForOrder(product, selectedProducts)
        }
    }
}

@Composable
fun ProductCardForOrder(product: Product, selectedProducts: MutableList<Product>) {

    Card(
        modifier = Modifier
            .padding(4.dp),
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
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Text(
                text = product.productName,
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = {

                },
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.SpeakerNotes,
                        contentDescription = ""
                    )
                }
            )
            Button(
                onClick = {
                    selectedProducts.add(product)
                },
                shape = RoundedCornerShape(5.dp),
                enabled = product.quantity > 0,
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
                    selectedProducts.remove(product)
                },
                shape = RoundedCornerShape(5.dp),
                enabled = product.quantity > 0,
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
