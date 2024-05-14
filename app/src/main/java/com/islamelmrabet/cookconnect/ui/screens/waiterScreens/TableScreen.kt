package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.AddCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.Table
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.CookerAndWaiterAppBar
import com.islamelmrabet.cookconnect.tools.DrawerHeader
import com.islamelmrabet.cookconnect.tools.DrawerItem
import com.islamelmrabet.cookconnect.tools.HeaderFooter
import com.islamelmrabet.cookconnect.tools.OutlinedTableTextField
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.utils.TableManager
import kotlinx.coroutines.launch

@Composable
fun TableScreen(auth: AuthManager, navController: NavHostController, tableManager: TableManager) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    val (number, setNumber) = remember { mutableIntStateOf(0) }
    val (capacity, setCapacity) = remember { mutableIntStateOf(0) }

    val onNumberChange: (Int) -> Unit = { setNumber(it) }
    val onCapacityChange: (Int) -> Unit = { setCapacity(it) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader()
                DrawerItem(
                    text = "Mesas",
                    icon = Icons.Default.ShoppingCart,
                    onClick = {
                        // Handle click action for Mesas
                    }
                )
                DrawerItem(
                    text = "Inventario",
                    icon = Icons.Default.Build,
                    onClick = {
                        // Handle click action for Mesas
                    }
                )
                DrawerItem(
                    text = "Historial de Ventas",
                    icon = Icons.Default.Create,
                    onClick = {
                        // Handle click action for Mesas
                    }
                )
                DrawerItem(
                    text = "Cuenta",
                    icon = Icons.Default.Person,
                    onClick = {
                        // Handle click action for Mesas
                    }
                )
                Spacer(modifier = Modifier.height(350.dp))
                HeaderFooter("18/02/2004")
            }
        },
    ){
    Scaffold(
        topBar = {
            CookerAndWaiterAppBar(
                stringResource(id = R.string.table_screen_header),
                onClick = {
                    scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }}
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
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "table" )
                    Text(
                        text = stringResource(id = R.string.table_screen_header),
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
                        onClick = { showDialog = true },
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.AddCircle,
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
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate(Routes.WelcomeScreen.route) {
                            popUpTo(Routes.WelcomeScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Sign Out")
                }
            }
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Add Table") },
            confirmButton = {
                Button(
                    onClick = {
                        val table = Table(
                            number = number,
                            capacity = capacity,
                        )
                        tableManager.addTable(table)
                        showDialog = false
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            },
            text = {
                Column {
                    OutlinedTableTextField("Number", number, onNumberChange)
                    OutlinedTableTextField("Capacity", capacity, onCapacityChange)
                }
            }
        )
    }
}
}


