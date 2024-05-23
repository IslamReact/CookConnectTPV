package com.islamelmrabet.cookconnect.model.localModels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.TableBar
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.TableBar
import androidx.compose.ui.graphics.vector.ImageVector
import com.islamelmrabet.cookconnect.navigation.Routes

data class NavigationItem (
    val title: String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
    val route : String,
)

val navigationItems = listOf(
    NavigationItem(
        title = "Mesas",
        selectedIcon = Icons.Filled.TableBar,
        unselectedIcon = Icons.Outlined.TableBar,
        route = Routes.TableScreen.route,
    ),
    NavigationItem(
        title = "Inventario",
        selectedIcon = Icons.Filled.Inventory,
        unselectedIcon = Icons.Outlined.Inventory,
        route = Routes.InventoryScreen.route,
    ),
    NavigationItem(
        title = "Historial de Ventas",
        selectedIcon = Icons.Filled.AccountBalance,
        unselectedIcon = Icons.Outlined.AccountBalance,
        route = Routes.InvoiceScreen.route,
    ),
    NavigationItem(
        title = "Ajustes Perfil",
        selectedIcon = Icons.Filled.ManageAccounts,
        unselectedIcon = Icons.Outlined.ManageAccounts,
        route = Routes.AccountSettingsScreen.route,
    ),
    NavigationItem(
        title = "Cerrar Sesion",
        selectedIcon = Icons.AutoMirrored.Filled.Logout,
        unselectedIcon = Icons.AutoMirrored.Outlined.Logout,
        route = Routes.WelcomeScreen.route,
    )
)

val cookerNavigationItem = listOf(
    NavigationItem(
        title = "Pedidos",
        selectedIcon = Icons.Filled.Coffee,
        unselectedIcon = Icons.Outlined.TableBar,
        route = Routes.OrderCookerScreen.route,
    ),
    NavigationItem(
        title = "Historial de Ventas",
        selectedIcon = Icons.Filled.AccountBalance,
        unselectedIcon = Icons.Outlined.AccountBalance,
        route = Routes.InvoiceScreen.route,
    ),
    NavigationItem(
        title = "Ajustes Perfil",
        selectedIcon = Icons.Filled.ManageAccounts,
        unselectedIcon = Icons.Outlined.ManageAccounts,
        route = Routes.AccountSettingsScreen.route,
    ),
    NavigationItem(
        title = "Cerrar Sesion",
        selectedIcon = Icons.AutoMirrored.Filled.Logout,
        unselectedIcon = Icons.AutoMirrored.Outlined.Logout,
        route = Routes.WelcomeScreen.route,
    )
)