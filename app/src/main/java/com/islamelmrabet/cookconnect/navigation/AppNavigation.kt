package com.islamelmrabet.cookconnect.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.islamelmrabet.cookconnect.ui.screens.authScreens.CreateAccountScreen
import com.islamelmrabet.cookconnect.ui.screens.authScreens.ForgotPasswordScreen
import com.islamelmrabet.cookconnect.ui.screens.authScreens.LogInScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.AccountSettingsScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.InvoiceScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.SplashScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.WelcomeScreen
import com.islamelmrabet.cookconnect.ui.screens.cookerScreens.OrderCookerScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.AddProductScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.EditProductScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.InventoryScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.OrderScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.TableScreen
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.utils.ProductManager
import com.islamelmrabet.cookconnect.utils.TableManager
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel

@Composable
fun AppNavigation(context: Context,authViewModel: AuthViewModel, productViewModel: ProductViewModel) {
    val navController = rememberNavController()
    val authManager = AuthManager(context)
    val tableManager = TableManager(context)
    val productManager = ProductManager(context)

    val user: FirebaseUser? = authManager.getCurrentUser()

    NavHost(navController = navController,
        startDestination = Routes.SplashScreen.route,
    ){
        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Routes.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }
        composable(Routes.LogInScreen.route  + "/{email}"){backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            LogInScreen(auth = authManager, navController,email, authViewModel)
        }
        composable(Routes.CreateAccountScreen.route) {
            CreateAccountScreen(auth = authManager,navController)
        }
        composable(Routes.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(auth = authManager,navController)
        }
        composable(Routes.TableScreen.route) {
            TableScreen(auth = authManager,navController, tableManager, authViewModel)
        }
        composable(Routes.OrderScreen.route) {
            OrderScreen(auth = authManager,navController)
        }
        composable(Routes.InventoryScreen.route) {
            InventoryScreen(auth = authManager,navController,authViewModel,productViewModel)
        }
        composable(Routes.EditProductScreen.route + "/{productName}" ) { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName")
            EditProductScreen(auth = authManager,navController, productViewModel,productManager,productName)
        }
        composable(Routes.AddProductScreen.route) {
            AddProductScreen(auth = authManager,navController, productViewModel, productManager)
        }
        composable(Routes.OrderCookerScreen.route) {
            OrderCookerScreen(auth = authManager,navController, authViewModel)
        }
        composable(Routes.AccountSettingsScreen.route) {
            AccountSettingsScreen(auth = authManager,navController,authViewModel)
        }
        composable(Routes.InvoiceScreen.route) {
            InvoiceScreen(auth = authManager,navController,authViewModel)
        }
    }
}