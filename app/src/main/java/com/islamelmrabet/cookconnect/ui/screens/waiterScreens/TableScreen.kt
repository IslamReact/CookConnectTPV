package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.sharp.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.CookerAndWaiterAppBar
import com.islamelmrabet.cookconnect.utils.AuthManager

@Composable
fun TableScreen(auth: AuthManager, navController: NavHostController) {
    Scaffold(
        topBar = {
            CookerAndWaiterAppBar(
                stringResource(id = R.string.table_screen_header),
                onClick = {}
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp, end = 15.dp, bottom = 10.dp)
                ){
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "table" )
                    Text(
                        text = stringResource(id = R.string.table_screen_header),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .size(1.dp, 30.dp)
                            .background(color = Color.Gray)
                            .fillMaxHeight()
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    IconButton(
                        onClick = {},
                        modifier = Modifier
                    ){
                        Icon(
                            imageVector = Icons.Sharp.AddCircle,
                            contentDescription = ""
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp)
                        .shadow(5.dp)
                        .height(1.dp)
                        .background(color = Color.Transparent)
                )
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