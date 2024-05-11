
package com.islamelmrabet.cookconnect.ui.screens.commonScreens.authScreens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.GoogleAuthProvider
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.ButtonWithIcon
import com.islamelmrabet.cookconnect.tools.ClickableText
import com.islamelmrabet.cookconnect.tools.TextFieldLogin
import com.islamelmrabet.cookconnect.utils.AuthRes
import com.islamelmrabet.cookconnect.utils.AuthManager
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LogInScreen(auth: AuthManager, navController: NavController) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val onEmailChange: (String) -> Unit = { setEmail(it) }
    val onPasswordChange: (String) -> Unit = { setPassword(it) }

    var isButtonEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(email, password) {
        isButtonEnabled = email.isNotBlank() && password.isNotBlank()
    }

    Scaffold(
        topBar = {
            AppBar(
                navController,
                stringResource(id = R.string.login),
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
                TextFieldLogin(
                    labelEmailText = stringResource(id = R.string.email),
                    labelEmailTextField =  stringResource(id = R.string.email_example),
                    labelPasswordText = stringResource(id = R.string.password),
                    labelPasswordTextField =  stringResource(id = R.string.password_label),
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange,
                    emailValue = email,
                    passwordValue = password
                )
                ClickableText(
                    text = stringResource(id = R.string.forgot_password),
                    onClick = {
                        navController.navigate(Routes.ForgotPasswordScreen.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(50.dp))
                ButtonWithIcon(
                    buttonText = stringResource(id = R.string.login),
                    onClick = {
                        scope.launch {
                            emailPassSignIn(email, password, auth, context, navController)
                        }
                    },
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = buttonColors,
                    icon = Icons.Default.Person,
                    enabled = isButtonEnabled
                )
            }
        }
    )
}

private suspend fun emailPassSignIn(email: String, password: String, auth: AuthManager, context: Context, navigation: NavController) {
    when (val result = auth.signInWithEmailAndPassword(email, password)) {
        is AuthRes.Success -> {
            Toast.makeText(context, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show()
            navigation.navigate(Routes.WelcomeScreen.route) {
                popUpTo(Routes.LogInScreen.route) {
                    inclusive = true
                }
            }
        }

        is AuthRes.Error -> {
            Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}
