package com.islamelmrabet.cookconnect.navigation

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.islamelmrabet.cookconnect.ui.screens.animationScreens.OrderSuccessfulScreen
import com.islamelmrabet.cookconnect.ui.screens.authScreens.CreateAccountScreen
import com.islamelmrabet.cookconnect.ui.screens.authScreens.ForgotPasswordScreen
import com.islamelmrabet.cookconnect.ui.screens.authScreens.LogInScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.AccountSettingsScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.InvoiceScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.SplashScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.WelcomeScreen
import com.islamelmrabet.cookconnect.ui.screens.cookerScreens.OrderCookerScreen
import com.islamelmrabet.cookconnect.ui.screens.onBoardingScreens.FirstOnBoardingScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.AddProductScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.EditProductScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.InventoryScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.OrderScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.OrderSummaryScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.TableScreen
import com.islamelmrabet.cookconnect.managers.AuthManager
import com.islamelmrabet.cookconnect.managers.InvoiceManager
import com.islamelmrabet.cookconnect.managers.OrderCookerManager
import com.islamelmrabet.cookconnect.managers.ProductManager
import com.islamelmrabet.cookconnect.managers.TableManager
import com.islamelmrabet.cookconnect.ui.screens.settingsScreen.ContactScreen
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.InvoiceViewModel
import com.islamelmrabet.cookconnect.viewModel.MainViewModel
import com.islamelmrabet.cookconnect.viewModel.OrderCookerViewModel
import com.islamelmrabet.cookconnect.viewModel.OrderViewModel
import com.islamelmrabet.cookconnect.viewModel.PreferencesViewModel
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel
import com.islamelmrabet.cookconnect.viewModel.TableViewModel

/**
 * Class: AppNavigation
 *
 * Description: This class contains the navigation flow of the app.
 *              Each composable defines which attributes will contain each screen.
 *
 * @param context
 * @param authViewModel
 * @param productViewModel
 * @param tableViewModel
 * @param mainViewModel
 * @param orderViewModel
 * @param orderCookerViewModel
 * @param invoiceViewModel
 * @param preferencesViewModel
 */
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AppNavigation(
    context: Context,
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    tableViewModel: TableViewModel,
    mainViewModel: MainViewModel,
    orderViewModel: OrderViewModel,
    orderCookerViewModel: OrderCookerViewModel,
    invoiceViewModel: InvoiceViewModel,
    preferencesViewModel: PreferencesViewModel
) {
    val navController = rememberNavController()
    val authManager = AuthManager(context)
    val tableManager = TableManager()
    val productManager = ProductManager()
    val orderCookerManager = OrderCookerManager()
    val invoiceManager = InvoiceManager()

    NavHost(
        navController = navController,
        startDestination = Routes.SplashScreen.route,
    ) {
        composable(Routes.SplashScreen.route) {
            SplashScreen(navController, preferencesViewModel)
        }
        composable(Routes.FirstOnBoardingScreen.route) {
            FirstOnBoardingScreen(navController, preferencesViewModel)
        }
        composable(Routes.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }
        composable(Routes.LogInScreen.route + "/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            LogInScreen(auth = authManager, navController, email, authViewModel)
        }
        composable(Routes.CreateAccountScreen.route) {
            CreateAccountScreen(auth = authManager, navController)
        }
        composable(Routes.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(auth = authManager, navController, authViewModel)
        }
        composable(Routes.TableScreen.route) {
            TableScreen(
                auth = authManager,
                navController,
                tableManager,
                authViewModel,
                tableViewModel,
                mainViewModel
            )
        }
        composable(Routes.OrderScreen.route + "/{tableNumber}") { backStackEntry ->
            val tableNumberString = backStackEntry.arguments?.getString("tableNumber")
            val tableNumber = tableNumberString?.toIntOrNull()
            OrderScreen(
                navController,
                productViewModel,
                orderViewModel,
                tableNumber,
                tableViewModel,
                tableManager
            )
        }
        composable(Routes.InventoryScreen.route) {
            InventoryScreen(
                auth = authManager,
                navController,
                authViewModel,
                productViewModel,
                mainViewModel
            )
        }
        composable(Routes.EditProductScreen.route + "/{productName}") { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName")
            EditProductScreen(
                navController,
                productViewModel,
                productManager,
                productName
            )
        }
        composable(Routes.AddProductScreen.route) {
            AddProductScreen(navController, productViewModel, productManager)
        }
        composable(Routes.OrderCookerScreen.route) {
            OrderCookerScreen(
                auth = authManager,
                navController,
                authViewModel,
                orderCookerViewModel,
                orderCookerManager,
                mainViewModel,
                tableViewModel,
                tableManager
            )
        }
        composable(Routes.AccountSettingsScreen.route) {
            AccountSettingsScreen(auth = authManager, navController, authViewModel, mainViewModel)
        }
        composable(Routes.InvoiceScreen.route) {
            InvoiceScreen(
                auth = authManager,
                navController,
                authViewModel,
                mainViewModel,
                invoiceViewModel
            )
        }
        composable(Routes.OrderSummaryScreen.route) {
            OrderSummaryScreen(
                navController,
                orderViewModel,
                productViewModel,
                tableViewModel,
                tableManager,
                invoiceViewModel,
                invoiceManager
            )
        }
        composable(Routes.OrderSuccessfulScreen.route) {
            OrderSuccessfulScreen(navController)
        }
        composable(Routes.ContactScreen.route) {
            ContactScreen(navController)
        }
    }
}