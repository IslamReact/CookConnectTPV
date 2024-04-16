package com.islamelmrabet.cookconnect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.SplashScreen
import com.islamelmrabet.cookconnect.ui.screens.commonScreens.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = Routes.SplashScreen.route,
    ){
        composable(Routes.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(Routes.WelcomeScreen.route) {
            WelcomeScreen()
        }
    }
}