package com.islamelmrabet.cookconnect.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseUser
import com.islamelmrabet.cookconnect.ui.screens.authScreens.CreateAccountScreen
import com.islamelmrabet.cookconnect.ui.screens.authScreens.LogInScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.SplashScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.WelcomeScreen
import com.islamelmrabet.cookconnect.ui.screens.authScreens.ForgotPasswordScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.AccountSettingsScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.InvoiceScreen
import com.islamelmrabet.cookconnect.ui.screens.cookerScreens.OrderCookerScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.AddProductScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.EditProductScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.InventoryScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.OrderScreen
import com.islamelmrabet.cookconnect.ui.screens.waiterScreens.TableScreen
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.utils.TableManager
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel

@Composable
fun AppNavigation(context: Context,authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authManager = AuthManager(context)
    val tableManager = TableManager(context)

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
            TableScreen(auth = authManager,navController, tableManager)
        }
        composable(Routes.OrderScreen.route) {
            OrderScreen(auth = authManager,navController)
        }
        composable(Routes.InventoryScreen.route) {
            InventoryScreen(auth = authManager,navController)
        }
        composable(Routes.EditProductScreen.route) {
            EditProductScreen(auth = authManager,navController)
        }
        composable(Routes.AddProductScreen.route) {
            AddProductScreen(auth = authManager,navController)
        }
        composable(Routes.OrderCookerScreen.route) {
            OrderCookerScreen(auth = authManager,navController)
        }
        composable(Routes.InventoryScreen.route) {
            InvoiceScreen(auth = authManager,navController)
        }
        composable(Routes.AccountSettingsScreen.route) {
            AccountSettingsScreen(auth = authManager,navController)
        }
    }
}