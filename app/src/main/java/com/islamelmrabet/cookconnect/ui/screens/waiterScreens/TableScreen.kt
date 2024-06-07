package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Table
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.BasicButton
import com.islamelmrabet.cookconnect.tools.CookerAndWaiterAppBar
import com.islamelmrabet.cookconnect.tools.DrawerHeader
import com.islamelmrabet.cookconnect.tools.DrawerFooter
import com.islamelmrabet.cookconnect.tools.OutlinedTableTextField
import com.islamelmrabet.cookconnect.managers.AuthManager
import com.islamelmrabet.cookconnect.managers.TableManager
import com.islamelmrabet.cookconnect.model.localModels.getNavigationItems
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.MainViewModel
import com.islamelmrabet.cookconnect.viewModel.TableViewModel
import kotlinx.coroutines.launch

/**
 * Composable screen TableScreen
 *
 * @param auth
 * @param navController
 * @param tableManager
 * @param authViewModel
 * @param tableViewModel
 * @param mainViewModel
 */
@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableScreen(
    auth: AuthManager,
    navController: NavHostController,
    tableManager: TableManager,
    authViewModel: AuthViewModel,
    tableViewModel: TableViewModel,
    mainViewModel: MainViewModel
) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val showDialogState = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val selectedItemIndex by mainViewModel.drawerSelectedIndex.collectAsState()
    var lastLogInDate by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val allTables by tableViewModel.fetchTableDataFlow().collectAsState(initial = emptyList())
    val navigationItems = getNavigationItems()
    val logOutText = stringResource(id = R.string.logOut)

    val vibrator = context.getSystemService(Vibrator::class.java)
    val (number, setNumber) = remember { mutableIntStateOf(0) }
    val (capacity, setCapacity) = remember { mutableIntStateOf(0) }

    val onNumberChange: (Int) -> Unit = { setNumber(it) }
    val onCapacityChange: (Int) -> Unit = { setCapacity(it) }

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
                navigationItems.forEachIndexed { index, item ->
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
                    stringResource(id = R.string.table_screen_header),
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
                        Icon(imageVector = Icons.Default.TableBar, contentDescription = "table")
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
                            onClick = { showDialogState.value = true },
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
                    ShowLazyListOfTables(allTables, navController, vibrator, tableViewModel, context, tableManager)
                }
            }
        )
        if (showDialogState.value) {
            ModalBottomSheetAddTable(
                showDialogState = showDialogState,
                sheetState = sheetState,
                number = number,
                onNumberChange = onNumberChange,
                capacity = capacity,
                onCapacityChange = onCapacityChange,
                lessRoundedShape = lessRoundedShape,
                buttonColors = buttonColors,
                manager = tableManager,
                context = context,
                tableViewModel = tableViewModel
            )
        }
    }
}

/**
 * Composable function that displays the information and fields to add a table
 *
 * @param showDialogState
 * @param sheetState
 * @param number
 * @param onNumberChange
 * @param capacity
 * @param onCapacityChange
 * @param lessRoundedShape
 * @param buttonColors
 * @param manager
 * @param context
 * @param tableViewModel
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ModalBottomSheetAddTable(
    showDialogState: MutableState<Boolean>,
    sheetState: SheetState,
    number: Int,
    onNumberChange: (Int) -> Unit,
    capacity: Int,
    onCapacityChange: (Int) -> Unit,
    lessRoundedShape: RoundedCornerShape,
    buttonColors: ButtonColors,
    manager: TableManager,
    context: Context,
    tableViewModel: TableViewModel
) {
    ModalBottomSheet(
        onDismissRequest = {
            showDialogState.value = false
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.add_tables),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary,
                style = TextStyle(
                    fontSize = 25.sp,
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.outline,
                        offset = Offset(2.0f, 5.0f),
                        blurRadius = 7f
                    )
                ),
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
            Column {
                OutlinedTableTextField(stringResource(id = R.string.table_number), number, onNumberChange)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTableTextField(stringResource(id = R.string.people_capacity), capacity, onCapacityChange)
            }
            Row(
                modifier = Modifier
                    .padding(top = 30.dp)
            ) {
                BasicButton(
                    buttonText = stringResource(id = R.string.add_tables),
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = buttonColors,
                    onClick = {
                        val table = Table(
                            number = number,
                            capacity = capacity,
                            gotOrder = false,
                            gotOrderReady = false
                        )
                        tableViewModel.addTable(table, manager, context) {
                            showDialogState.value = false
                        }
                    }
                )
            }
        }
    }
}

/**
 * Composable function that shows the lazy column of tables.
 *
 * @param tables
 * @param navController
 * @param tableManager
 * @param context
 * @param tableViewModel
 */
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun ShowLazyListOfTables(tables: List<Table>, navController: NavController, vibrator: Vibrator,  tableViewModel: TableViewModel, context: Context, tableManager: TableManager) {
    if (tables.isEmpty()) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.Url("https://lottie.host/f741b1cc-6935-4d12-8d6a-1be02907e008/7v6a565aGQ.json")
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.no_tables),
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
                text = stringResource(id = R.string.start_adding_tables),
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
    }else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            userScrollEnabled = true
        ) {
            tables.forEachIndexed { _, table ->
                item {
                    TableIcon(table, navController, vibrator, showDialog = true, tableViewModel ,context, tableManager)
                }
            }
        }
    }
}

/**
 * Composable function that displays the table information.
 *
 * @param table
 * @param navController
 * @param tableManager
 * @param context
 * @param tableViewModel
 *
 */
@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun TableIcon(table: Table, navController: NavController, vibrator: Vibrator, showDialog: Boolean, tableViewModel: TableViewModel, context: Context, tableManager: TableManager) {
    val showAlertDialog = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(10.dp)
            .clip(shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.onTertiary,
            contentColor = MaterialTheme.colorScheme.scrim,
            disabledContainerColor = MaterialTheme.colorScheme.outline,
            disabledContentColor = MaterialTheme.colorScheme.outline
        ),
        border = BorderStroke(
            0.3.dp,
            MaterialTheme.colorScheme.primary
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val onClick: () -> Unit = {
                navController.navigate("${Routes.OrderScreen.route}/${table.number}")
            }
            val onLongClick: () -> Unit = {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                if (showDialog) {
                    showAlertDialog.value = true
                }
            }
            if (table.capacity > 4) {
                Image(
                    painter = painterResource(id = R.drawable.order_large_table),
                    contentDescription = "table icon",
                    modifier = Modifier
                        .combinedClickable(
                            onClick = onClick,
                            onLongClick = onLongClick
                        )
                        .size(40.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.order_table),
                    contentDescription = "table icon",
                    modifier = Modifier
                        .combinedClickable(
                            onClick = onClick,
                            onLongClick = onLongClick
                        )
                        .size(40.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = R.string.table, table.number),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(8.dp))
                        .background(
                            color = if (table.gotOrderReady) {
                                MaterialTheme.colorScheme.secondary
                            } else if (table.gotOrder) {
                                MaterialTheme.colorScheme.surfaceBright
                            } else {
                                MaterialTheme.colorScheme.outline
                            }
                        )
                        .padding(1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Order Ready",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        if (showAlertDialog.value) {
            AlertDialog(
                onDismissRequest = { showAlertDialog.value = false},
                title = { Text(text = stringResource(id = R.string.confirm_delete)) },
                text = { Text(text = stringResource(id = R.string.confirm_delete_text)) },
                confirmButton = {
                    TextButton(onClick = {
                        tableViewModel.deleteTable(table.number,tableManager, context)
                        showAlertDialog.value = false
                    }) {
                        Text(text = stringResource(id = R.string.yes))
                    }
                }
            )
        }
    }
}

