package com.islamelmrabet.cookconnect.ui.screens.commonScreens.authScreens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.islamelmrabet.cookconnect.utils.AuthManager
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.utils.AuthRes
import kotlinx.coroutines.launch
import java.lang.RuntimeException

@Composable
fun ForgotPasswordScreen(auth: AuthManager, navController: NavController) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            AppBar(
                navController,
                stringResource(id = R.string.forgot_password_label),
                Routes.WelcomeScreen.route
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .padding(top = contentPadding.calculateTopPadding() + 30.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.email),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    label = { Text(text = "Correo electrónico") },
                    value = email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(30.dp))
                Box(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                    Button(
                        onClick = {
                            scope.launch {
                                when (val res = auth.resetPassword(email)) {
                                    is AuthRes.Success -> {
                                        Toast.makeText(
                                            context,
                                            "Correo enviado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate(Routes.LogInScreen.route)
                                    }

                                    is AuthRes.Error -> {
                                        Toast.makeText(
                                            context,
                                            "Error al enviar el correo",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        },
                        shape = lessRoundedShape,
                        modifier = Modifier
                            .height(50.dp)
                            .width(205.dp),
                        colors = buttonColors,
                        content = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = stringResource(id = R.string.reset_password),
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    )
}
