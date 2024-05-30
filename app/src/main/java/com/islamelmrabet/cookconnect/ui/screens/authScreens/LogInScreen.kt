package com.islamelmrabet.cookconnect.ui.screens.authScreens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.ButtonWithIcon
import com.islamelmrabet.cookconnect.tools.ClickableText
import com.islamelmrabet.cookconnect.tools.ImportantInfoCard
import com.islamelmrabet.cookconnect.tools.TextFieldLogin
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.utils.AuthRes
import com.islamelmrabet.cookconnect.viewModel.AuthViewModel
import kotlinx.coroutines.launch

/**
 * Composable screen LogInScreen
 *
 * @param auth
 * @param navController
 * @param initialEmail
 * @param authViewModel
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LogInScreen(
    auth: AuthManager,
    navController: NavController,
    initialEmail: String? = null,
    authViewModel: AuthViewModel,
) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val (email, setEmail) = remember { mutableStateOf(initialEmail ?: "") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val (userRole, setUserRole) = remember { mutableStateOf("") }
    val (isANewPassword, setisANewPassword) = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    val onEmailChange: (String) -> Unit = { setEmail(it) }
    val onPasswordChange: (String) -> Unit = { setPassword(it) }

    LaunchedEffect(email, password, userRole) {
        isButtonEnabled = email.isNotBlank() && password.isNotBlank() && userRole.isNotBlank()
    }

    LaunchedEffect(Unit) {
        if (initialEmail != null) {
            val fetchedPassword = authViewModel.getPasswordByEmail(initialEmail)
            fetchedPassword?.let { setPassword(it) }
            val fetchedRole = authViewModel.getRoleByEmail(initialEmail)
            fetchedRole?.let { setUserRole(it) }
            val fetchedIsANewPassword = authViewModel.getIsANewPassword(initialEmail)
            fetchedIsANewPassword?.let { setisANewPassword(it) }
        }
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
                    labelEmailTextField = stringResource(id = R.string.email_example),
                    labelPasswordText = stringResource(id = R.string.password),
                    labelPasswordTextField = stringResource(id = R.string.password_label),
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
                        isLoading = true
                        scope.launch {
                            if (isANewPassword) {
                                authViewModel.updatePassword(email, auth, context, password)
                                authViewModel.updateIsANewPassword(email, auth, false)
                                try {
                                    emailPassSignIn(
                                        email,
                                        password,
                                        auth,
                                        context,
                                        navController,
                                        userRole
                                    )
                                } finally {
                                    isLoading = false
                                }
                            } else {
                                try {
                                    emailPassSignIn(
                                        email,
                                        password,
                                        auth,
                                        context,
                                        navController,
                                        userRole
                                    )
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    },
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = buttonColors,
                    icon = Icons.Default.Person,
                    enabled = isButtonEnabled
                )
                if (isLoading) {
                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.Url("https://lottie.host/28d9d00d-fd59-47a3-9274-84ae50025090/NQicxfwezK.json")
                    )
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
                ImportantInfoCard {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "info",
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stringResource(id = R.string.important),
                            textAlign = TextAlign.Start,
                            style = TextStyle(fontSize = 16.sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(id = R.string.important_login_text),
                        textAlign = TextAlign.Justify,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }
        }
    )
}

/**
 * Private suspend function that calls the authManager to make the sign in request.
 *
 * @param email
 * @param password
 * @param auth
 * @param context
 * @param navigation
 * @param userRole
 */
private suspend fun emailPassSignIn(
    email: String,
    password: String,
    auth: AuthManager,
    context: Context,
    navigation: NavController,
    userRole: String
) {
    when (val result = auth.signInWithEmailAndPassword(email, password)) {
        is AuthRes.Success -> {
            Toast.makeText(context, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show()
            when (userRole) {
                "Cocinero" -> {
                    navigation.navigate(Routes.OrderCookerScreen.route) {
                        popUpTo(Routes.LogInScreen.route) { inclusive = true }
                        popUpTo(Routes.WelcomeScreen.route) { inclusive = true }
                    }
                }

                "Camarero" -> {
                    navigation.navigate(Routes.TableScreen.route) {
                        popUpTo(Routes.LogInScreen.route) { inclusive = true }
                        popUpTo(Routes.WelcomeScreen.route) { inclusive = true }
                    }
                }

                else -> {
                    Toast.makeText(context, "Rol desconocido: $userRole", Toast.LENGTH_SHORT).show()
                    navigation.navigate(Routes.WelcomeScreen.route)
                }
            }
        }

        is AuthRes.Error -> {
            Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT)
                .show()
        }
    }
}