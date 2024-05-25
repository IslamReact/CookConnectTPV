package com.islamelmrabet.cookconnect.ui.screens.commonScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.localModels.NavigationItem
import com.islamelmrabet.cookconnect.model.localModels.cookerNavigationItem
import com.islamelmrabet.cookconnect.model.localModels.navigationItems
import com.islamelmrabet.cookconnect.model.localModels.settingsList
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.CookerAndWaiterAppBar
import com.islamelmrabet.cookconnect.tools.DrawerHeader
import com.islamelmrabet.cookconnect.tools.HeaderFooter
import com.islamelmrabet.cookconnect.tools.OutlinedBasicButton
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountSettingsScreen(
    auth: AuthManager,
    navController: NavHostController,
    authViewModel: AuthViewModel,
    mainViewModel: MainViewModel
) {

    val lessRoundedShape = RoundedCornerShape(8.dp)

    val buttonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.error,
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItemIndex by mainViewModel.drawerSelectedIndex.collectAsState()
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
                HeaderFooter(lastLogInDate)
            }
        },
    ) {
        Scaffold(
            topBar = {
                CookerAndWaiterAppBar(
                    stringResource(id = R.string.account_screen_header),
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
                        modifier = Modifier
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_male_png),
                            contentDescription = "",
                            modifier = Modifier.size(100.dp)
                        )
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = workerName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = workerID,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            )
                            Text(
                                text = workerEmail,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                fontWeight = FontWeight.Normal,
                                fontSize = 15.sp
                            )
                        }
                    }
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outline,
                        thickness = 1.dp,
                    )
                    LazyColumn {
                        itemsIndexed(settingsList) { index, item ->
                            SettingItemDesign(
                                title = item.title,
                                routes = item.route,
                                onClick = {}
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Item POS 1.0v",
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            fontSize = 14.sp
                        )
                        OutlinedBasicButton(
                            buttonText = stringResource(id = R.string.logOut),
                            lessRoundedShape = lessRoundedShape,
                            buttonColors = buttonColors,
                            border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.error),
                            onClick = {
                                auth.signOut()
                                Log.d("LogOut event", "Succesfully logged out")
                                navController.navigate(Routes.WelcomeScreen.route) {
                                    popUpTo(Routes.WelcomeScreen.route) {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        )
    }
}


@Composable
fun SettingItemDesign(title: String, routes: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = title)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = "",
            modifier = Modifier.size(24.dp)
        )
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outline,
        thickness = 1.dp,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}