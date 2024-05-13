package com.islamelmrabet.cookconnect.ui.screens.cookerScreens

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.utils.AuthManager

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderCookerScreen(auth: AuthManager, navCotroller: NavHostController){
    Scaffold(
        topBar = {
            AppBar(
                navCotroller,
                stringResource(id = R.string.restaurant_screen_header),
                Routes.WelcomeScreen.route
            )
        },
        content = { contentPadding ->
        }
    )
}