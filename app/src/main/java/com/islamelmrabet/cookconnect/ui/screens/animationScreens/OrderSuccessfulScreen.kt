package com.islamelmrabet.cookconnect.ui.screens.animationScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.islamelmrabet.cookconnect.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun OrderSuccessfulScreen(navController: NavHostController) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url("https://lottie.host/351beb02-ab0c-4715-bee0-bf9498a0a5ae/rsGvAATjm3.json")
    )
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(5000)
        navController.navigate(Routes.TableScreen.route) {
            popUpTo(Routes.OrderSummaryScreen.route) { inclusive = true }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            initialAlpha = 0.3f
        ) + slideInVertically(
            initialOffsetY = { fullHeight -> fullHeight / 2 }
        ),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
        }
    }
}
