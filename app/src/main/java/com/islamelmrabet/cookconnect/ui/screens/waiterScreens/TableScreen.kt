package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.utils.AuthManager

@Composable
fun TableScreen(auth: AuthManager, navController: NavHostController) {
    Scaffold(
        topBar = {
            AppBar(
                navController,
                stringResource(id = R.string.table_screen_header),
                Routes.WelcomeScreen.route
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                // Your content here

                // Button to sign out and navigate back to welcome screen
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate(Routes.WelcomeScreen.route) {
                            popUpTo(Routes.WelcomeScreen.route) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = "Sign Out")
                }
            }
        }
    )
}