package com.islamelmrabet.cookconnect.ui.screens.commonScreens

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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.ButtonDefaults
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
import com.islamelmrabet.cookconnect.model.localModels.NavigationItem
import com.islamelmrabet.cookconnect.model.localModels.cookerNavigationItem
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
fun InvoiceScreen(auth: AuthManager, navController: NavHostController, authViewModel: AuthViewModel){
    val lessRoundedShape = RoundedCornerShape(8.dp)

    val buttonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.error,
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
    var lastLogInDate by rememberSaveable { mutableStateOf("") }
    var workerName by rememberSaveable { mutableStateOf("") }
    var workerID by rememberSaveable { mutableStateOf("") }
    var workerEmail by rememberSaveable { mutableStateOf("") }
    var workerRole by rememberSaveable { mutableStateOf("") }
    var currentWorkerMenu by rememberSaveable { mutableStateOf<List<NavigationItem>>(emptyList()) }

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
            ModalDrawerSheet (
                drawerContainerColor = MaterialTheme.colorScheme.primary
            ){
                DrawerHeader()
                currentWorkerMenu.forEachIndexed { index, item ->
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
                        Icon(imageVector = Icons.Filled.AccountBalance, contentDescription = "table")
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
                            onClick = {  },
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
                }
            }
        )
    }
}