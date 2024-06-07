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
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes


/**
 * Class: NavigationItem
 *
 * @property title
 * @property selectedIcon
 * @property unselectedIcon
 * @property route
 */
data class NavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
)

@Composable
fun getNavigationItems(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = stringResource(id = R.string.table_screen_header),
            selectedIcon = Icons.Filled.TableBar,
            unselectedIcon = Icons.Outlined.TableBar,
            route = Routes.TableScreen.route,
        ),
        NavigationItem(
            title = stringResource(id = R.string.inventory_screen_header),
            selectedIcon = Icons.Filled.Inventory,
            unselectedIcon = Icons.Outlined.Inventory,
            route = Routes.InventoryScreen.route,
        ),
        NavigationItem(
            title = stringResource(id = R.string.invoice_screen_header),
            selectedIcon = Icons.Filled.AccountBalance,
            unselectedIcon = Icons.Outlined.AccountBalance,
            route = Routes.InvoiceScreen.route,
        ),
        NavigationItem(
            title = stringResource(id = R.string.account_screen_header),
            selectedIcon = Icons.Filled.ManageAccounts,
            unselectedIcon = Icons.Outlined.ManageAccounts,
            route = Routes.AccountSettingsScreen.route,
        ),
        NavigationItem(
            title = stringResource(id = R.string.logOut),
            selectedIcon = Icons.AutoMirrored.Filled.Logout,
            unselectedIcon = Icons.AutoMirrored.Outlined.Logout,
            route = Routes.WelcomeScreen.route,
        )
    )
}

@Composable
fun getCookerNavigationItems(): List<NavigationItem> {
    return listOf(
        NavigationItem(
            title = stringResource(id = R.string.order_screen_header),
            selectedIcon = Icons.Filled.Coffee,
            unselectedIcon = Icons.Outlined.TableBar,
            route = Routes.OrderCookerScreen.route,
        ),
        NavigationItem(
            title = stringResource(id = R.string.invoice_screen_header),
            selectedIcon = Icons.Filled.AccountBalance,
            unselectedIcon = Icons.Outlined.AccountBalance,
            route = Routes.InvoiceScreen.route,
        ),
        NavigationItem(
            title = stringResource(id = R.string.account_screen_header),
            selectedIcon = Icons.Filled.ManageAccounts,
            unselectedIcon = Icons.Outlined.ManageAccounts,
            route = Routes.AccountSettingsScreen.route,
        ),
        NavigationItem(
            title = stringResource(id = R.string.logOut),
            selectedIcon = Icons.AutoMirrored.Filled.Logout,
            unselectedIcon = Icons.AutoMirrored.Outlined.Logout,
            route = Routes.WelcomeScreen.route,
        )
    )
}
