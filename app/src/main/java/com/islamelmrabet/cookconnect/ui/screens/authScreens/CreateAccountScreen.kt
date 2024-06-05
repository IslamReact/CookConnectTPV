package com.islamelmrabet.cookconnect.ui.screens.authScreens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Worker
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.ButtonWithIcon
import com.islamelmrabet.cookconnect.tools.ClickableText
import com.islamelmrabet.cookconnect.tools.ImportantInfoCard
import com.islamelmrabet.cookconnect.tools.TextFieldLogin
import com.islamelmrabet.cookconnect.managers.AuthManager
import com.islamelmrabet.cookconnect.managers.AuthRes
import kotlinx.coroutines.launch

/**
 * Composable Screen CreateAccountScreen
 *
 * @param auth
 * @param navController
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAccountScreen(auth: AuthManager, navController: NavController) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    var expandedState by remember { mutableStateOf(false) }
    val options = listOf(stringResource(id = R.string.admin_role), stringResource(R.string.cooker_role), stringResource(R.string.waiter_role))
    var selectedItem by remember { mutableStateOf(options[0]) }

    val (name, setName) = remember { mutableStateOf("") }
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    val onNameChange: (String) -> Unit = { setName(it) }
    val onEmailChange: (String) -> Unit = { setEmail(it) }
    val onPasswordChange: (String) -> Unit = { setPassword(it) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var isButtonEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(email, password) {
        isButtonEnabled = email.isNotBlank() && password.isNotBlank()
    }

    Scaffold(
        topBar = {
            AppBar(
                navController,
                stringResource(id = R.string.register),
                Routes.WelcomeScreen.route
            )
        },
        content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(start = 25.dp, end = 25.dp)
                    .padding(top = contentPadding.calculateTopPadding() + 30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.userName),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = { R.string.userName_example },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(50.dp))
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
                    text = stringResource(id = R.string.already_got_account),
                    onClick = {
                        navController.navigate("${Routes.LogInScreen.route}/@gmail.com")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = stringResource(id = R.string.rol_label),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandedState,
                        onExpandedChange = { expandedState = !expandedState },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        OutlinedTextField(
                            value = selectedItem,
                            modifier = Modifier.menuAnchor(),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState) }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedState,
                            onDismissRequest = { expandedState = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            options.forEachIndexed { index, option ->
                                DropdownMenuItem(
                                    text = { Text(text = option) },
                                    onClick = {
                                        selectedItem = options[index]
                                        expandedState = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(50.dp))
                ButtonWithIcon(
                    buttonText = stringResource(id = R.string.register),
                    onClick = {
                        val worker = Worker(
                            name = name,
                            email = email,
                            userRole = selectedItem,
                            password = password,
                            isANewPassword = false,
                        )
                        scope.launch {
                            signUp(email, password, worker, auth, context, navController)
                        }
                    },
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = buttonColors,
                    icon = Icons.Default.Person,
                    enabled = isButtonEnabled
                )
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
                        text = stringResource(id = R.string.important_create_account_text),
                        textAlign = TextAlign.Justify,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }
        }
    )
}

/**
 * Private suspend function that calls the authManager to make the sign up request.
 *
 * @param email
 * @param password
 * @param worker
 * @param auth
 * @param context
 * @param navigation
 */
private suspend fun signUp(
    email: String,
    password: String,
    worker: Worker,
    auth: AuthManager,
    context: Context,
    navigation: NavController
) {
    when (val result = auth.createUserWithEmailAndPassword(email, password, worker)) {
        is AuthRes.Success -> {
            Toast.makeText(context, R.string.successful_register, Toast.LENGTH_SHORT).show()
            navigation.popBackStack()
        }
        is AuthRes.Error -> {
            Toast.makeText(context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT)
                .show()
        }
    }
}