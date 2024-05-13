package com.islamelmrabet.cookconnect.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes

// TODO: ALL TEXT FIELDS OF THE APP
@Composable
fun TextFieldLogin(
    labelEmailText: String,
    labelEmailTextField: String,
    labelPasswordText: String,
    labelPasswordTextField: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    emailValue: String,
    passwordValue: String
) {
    val passwordFocusRequester by remember { mutableStateOf(FocusRequester()) }

    Text(
        text = labelEmailText,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = emailValue,
        onValueChange = onEmailChange,
        placeholder = { Text(text = labelEmailTextField) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { passwordFocusRequester.requestFocus() }
        ),
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(50.dp))

    // Password Field
    Text(
        text = labelPasswordText,
        textAlign = TextAlign.Start,
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = passwordValue,
        onValueChange = onPasswordChange,
        placeholder = { Text(text = labelPasswordTextField) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),

        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(passwordFocusRequester)
    )
}

@Composable
fun ImportantInfoCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

// TODO: ALL BUTTONS OF THE APP
@Composable
fun BasicButton(
    buttonText: String,
    lessRoundedShape: RoundedCornerShape,
    buttonColors: ButtonColors,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick ,
        shape = lessRoundedShape,
        modifier = Modifier
            .height(50.dp)
            .width(160.dp),
        colors = buttonColors,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buttonText,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Forward Arrow",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    )
}

@Composable
fun ButtonWithIcon(
    buttonText: String,
    onClick: () -> Unit,
    lessRoundedShape: RoundedCornerShape,
    buttonColors: ButtonColors,
    icon : ImageVector,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        shape = lessRoundedShape,
        modifier = Modifier
            .height(50.dp)
            .width(180.dp),
        colors = buttonColors,
        enabled = enabled,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = "Forward Arrow",
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 5.dp)
                )
                Text(
                    text = buttonText,
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

// TODO: APP BAR
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBar(navController: NavController, topAppBarText : String, route: String) {
    Box {
        TopAppBar(
            title = {
                Text(
                    text = topAppBarText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(30.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                ) {
                    IconButton(onClick = { navController.navigate(route) }) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookerAndWaiterAppBar(topAppBarText : String, onClick: () -> Unit) {
    Box {
        TopAppBar(
            title = {
                Text(
                    text = topAppBarText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                Box(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(30.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                ) {
                    IconButton(onClick = onClick ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(3.dp)
        )
    }
}

//TODO: TEXTOS CLICKABLES

@Composable
fun ClickableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle.Default
) {
    Text(
        text = text,
        style = textStyle.merge(
            color = MaterialTheme.colorScheme.primary,
            textDecoration = TextDecoration.Underline,
            fontSize = 14.sp
        ),
        textAlign = TextAlign.End,
        modifier = modifier.clickable { onClick() }
    )
}

