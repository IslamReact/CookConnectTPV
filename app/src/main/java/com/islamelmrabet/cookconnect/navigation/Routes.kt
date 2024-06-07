package com.islamelmrabet.cookconnect.navigation

/**
 * Class: Routes
 *
 * Description: This sealed class provides the routes for the navigation flow.
 *
 * @property route
 */
sealed class Routes(val route:String){
    data object SplashScreen : Routes("splash_screen")

    //**********************************************************************************************
    // AUTH SCREEN ROUTES
    //**********************************************************************************************
    data object  WelcomeScreen : Routes("main_screen")
    data object  LogInScreen : Routes("logIn_screen")
    data object  CreateAccountScreen : Routes("create_account_screen")
    data object  ForgotPasswordScreen : Routes("forgot_password_screen")


    //**********************************************************************************************
    // APP SCREEN ROUTES
    //**********************************************************************************************
    data object  TableScreen : Routes("table_screen")
    data object  OrderScreen : Routes("order_screen")
    data object  InventoryScreen : Routes("inventory_screen")
    data object  EditProductScreen : Routes("edit_product_screen")
    data object  AddProductScreen : Routes("add_product_screen")
    data object  OrderCookerScreen : Routes("order_cooker_screen")
    data object  InvoiceScreen : Routes("invoice_screen")
    data object  AccountSettingsScreen : Routes("account_settings_screen")
    data object  OrderSummaryScreen : Routes("order_summary_screen")
    data object  OrderSuccessfulScreen : Routes("order_successful_screen")
    data object  ContactScreen : Routes("contact_screen")


    //**********************************************************************************************
    // ON BOARDING SCREEN ROUTES
    //**********************************************************************************************
    data object  FirstOnBoardingScreen : Routes("first_onboarding_screen")

}