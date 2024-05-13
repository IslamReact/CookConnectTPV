package com.islamelmrabet.cookconnect.navigation

sealed class Routes(val route:String){
    object SplashScreen : Routes("splash_screen")
    object  WelcomeScreen : Routes("main_screen")
    object  LogInScreen : Routes("logIn_screen")
    object  CreateAccountScreen : Routes("create_account_screen")
    object  ForgotPasswordScreen : Routes("forgot_password_screen")
    object  TableScreen : Routes("oable_screen")
    object  OrderScreen : Routes("order_screen")
    object  InventoryScreen : Routes("inventory_screen")
    object  EditProductScreen : Routes("edit_product_screen")
    object  AddProductScreen : Routes("add_product_screen")
    object  OrderCookerScreen : Routes("order_cooker_screen")
    object  InvoiceScreen : Routes("invoice_screen")
    object  AccountSettingsScreen : Routes("account_settings_screen")
}