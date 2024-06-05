package com.islamelmrabet.cookconnect.ui.screens.commonScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.viewModel.PreferencesViewModel
import kotlinx.coroutines.delay

/**
 * Composable function that displays the contents of Splash screen
 *
 * @param navController
 * @param preferencesViewModel
 */
@Composable
fun SplashScreen(navController: NavHostController, preferencesViewModel: PreferencesViewModel) {

    LaunchedEffect(key1 = true) {
        delay(5000)
        navController.popBackStack()
        preferencesViewModel.isDataStored {
            if (it) navController.navigate(Routes.WelcomeScreen.route)
            else navController.navigate(Routes.FirstOnBoardingScreen.route)
        }

    }
    Splash()
}

/**
 * Composable Screen SplashScreen
 *
 */
@Composable
fun Splash() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.logoapp),
            contentDescription = "Logo CookConnect",
        )
    }
}
