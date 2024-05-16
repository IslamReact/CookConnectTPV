package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.sharp.Search
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.localModels.navigationItems
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.CookerAndWaiterAppBar
import com.islamelmrabet.cookconnect.tools.DrawerHeader
import com.islamelmrabet.cookconnect.tools.HeaderFooter
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InventoryScreen(auth: AuthManager, navController: NavHostController,authViewModel: AuthViewModel){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val showDialogState = remember { mutableStateOf(false) }
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
//    var pendingOrders by rememberSaveable { mutableStateOf(mutableStateListOf<Order>()) }
    var lastLogInDate by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val fetchedLastLoginDate = authViewModel.getLastLoginDate()
        fetchedLastLoginDate?.let { lastLogInDate = it }
    }

//    LaunchedEffect(Unit) {
//        var pendingOrders = authViewModel.getOrders()
//        pendingOrders?.let { pendingOrders = it }
//    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet (
                drawerContainerColor = MaterialTheme.colorScheme.primary
            ){
                DrawerHeader()
                navigationItems.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            if (item.title == "Cerrar Sesion"){
                                auth.signOut()
                                Log.d("LogOut event","Succesfully logged out")
                                navController.navigate(Routes.WelcomeScreen.route) {
                                    popUpTo(Routes.WelcomeScreen.route) {
                                        inclusive = true
                                    }
                                }
                            }else{
                                navController.navigate(item.route)

                            }
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex){
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
                HeaderFooter(lastLogInDate)
            }
        },
    ){
        Scaffold(
            topBar = {
                CookerAndWaiterAppBar(
                    stringResource(id = R.string.inventary_screen_header),
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
                        Icon(imageVector = Icons.Outlined.Inventory, contentDescription = "table")
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
                        Spacer(modifier = Modifier.width(15.dp))
                        Box(
                            modifier = Modifier
                                .size(1.dp, 30.dp)
                                .background(color = Color.Gray)
                                .fillMaxHeight()
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        IconButton(
                            onClick = { navController.navigate(Routes.AddProductScreen.route) },
                            modifier = Modifier
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AddCircleOutline,
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
                }
            }
        )
    }
}