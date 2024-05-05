package com.islamelmrabet.cookconnect.ui.screens.commonScreens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LogInScreen(navController : NavController) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Iniciar sesión",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigate(Routes.WelcomeScreen.route)}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 150.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Email",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = "",
                onValueChange = { /* Handle email change */ },
                label = { Text("ejemplo@ejemplo.com") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Contraseña",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = "",
                onValueChange = { /* Handle password change */ },
                label = { Text("...") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle sign in button click */ },
                shape = lessRoundedShape,
                modifier = Modifier
                    .height(50.dp)
                    .width(180.dp),
                colors = buttonColors,
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Forward Arrow",
                            modifier = Modifier.size(25.dp).padding(end = 5.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.login),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Forward Arrow",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }
    }
}