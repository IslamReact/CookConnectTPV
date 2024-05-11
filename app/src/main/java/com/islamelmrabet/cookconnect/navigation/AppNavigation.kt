package com.islamelmrabet.cookconnect.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.authScreens.CreateAccountScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.authScreens.LogInScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.SplashScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.WelcomeScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.authScreens.ForgotPasswordScreen
import com.islamelmrabet.cookconnect.utils.AuthManager

@Composable
fun AppNavigation(context: Context, navController: NavHostController = rememberNavController()) {
    val authManager: AuthManager = AuthManager(context)

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
        composable(Routes.LogInScreen.route) {
            LogInScreen(auth = authManager,navController)
        }
        composable(Routes.CreateAccountScreen.route) {
            CreateAccountScreen(auth = authManager,navController)
        }
        composable(Routes.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(auth = authManager,navController)
        }
    }
}