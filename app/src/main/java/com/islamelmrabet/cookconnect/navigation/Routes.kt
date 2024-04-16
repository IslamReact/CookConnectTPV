package com.islamelmrabet.cookconnect.navigation

sealed class Routes(val route:String){
    object SplashScreen : Routes("splash_screen")
    object  WelcomeScreen : Routes("main_screen")
}