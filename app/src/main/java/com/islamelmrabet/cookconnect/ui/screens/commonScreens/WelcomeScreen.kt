package com.islamelmrabet.cookconnect.ui.screens.commonScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.islamelmrabet.cookconnect.R


@Composable
fun WelcomeScreen() {
    Column {
        Image(painter = painterResource(id = R.drawable.logoapp), contentDescription ="")
    }
}