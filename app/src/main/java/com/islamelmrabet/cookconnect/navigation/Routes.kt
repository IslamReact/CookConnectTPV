package com.islamelmrabet.cookconnect.navigation

sealed class Routes(val route:String){
    object SplashScreen : Routes("splash_screen")
    object  WelcomeScreen : Routes("main_screen")
    object  LogInScreen : Routes("logIn_screen")
    object  CreateAccountScreen : Routes("create_account_screen")
}