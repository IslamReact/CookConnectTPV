package com.islamelmrabet.cookconnect.tools

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.islamelmrabet.cookconnect.R

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

@Composable
fun OutlinedTableTextField(
    labelTableAttribute: String, tableAttribute: Int, onTableAttributeChange: (Int) -> Unit
) {
    Column {
        Text(
            text = labelTableAttribute,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = tableAttribute.toString(),
            onValueChange = { onTableAttributeChange(it.toIntOrNull() ?: 0) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxWidth()
        )
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
        onClick = onClick,
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
fun OutlinedBasicButton(
    buttonText: String,
    lessRoundedShape: RoundedCornerShape,
    buttonColors: ButtonColors,
    border: BorderStroke,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        shape = lessRoundedShape,
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        colors = buttonColors,
        border = border,
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buttonText,
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
    icon: ImageVector? = null,
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
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = "Forward Arrow",
                        modifier = Modifier
                            .size(25.dp)
                            .padding(end = 5.dp)
                    )
                }
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

@Composable
fun BasicLongButton(
    buttonText: String,
    onClick: () -> Unit,
    lessRoundedShape: RoundedCornerShape,
    buttonColors: ButtonColors,
    enabled: Boolean
) {
    Button(
        onClick = { onClick() },
        shape = lessRoundedShape,
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        colors = buttonColors,
        enabled = enabled,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = buttonText,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }
    )
}

@Composable
fun BasicLongButtonWithIcon(
    buttonText: String,
    secondaryText: String,
    onClick: () -> Unit,
    lessRoundedShape: RoundedCornerShape,
    buttonColors: ButtonColors,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        shape = lessRoundedShape,
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth(),
        colors = buttonColors,
        enabled = enabled,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.ShoppingCart,
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = buttonText,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = secondaryText,
                    modifier = Modifier.padding(end = 4.dp)
                )
            }
        }
    )
}

// TODO: APP BAR
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppBar(navController: NavController, topAppBarText: String, route: String) {
    Box {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = topAppBarText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontSize = 25.sp,
                    )
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
fun CookerAndWaiterAppBar(topAppBarText: String, onClick: () -> Unit) {
    Box {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = topAppBarText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    style = TextStyle(
                        fontSize = 25.sp
                    )
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            navigationIcon = {
                IconButton(onClick = onClick) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
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

//TODO: DRAWER COMPONENTS
@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable._cropped),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(shape = RoundedCornerShape(7.dp)),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "ItemPOS",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Text(
            text = stringResource(id = R.string.restaurant_screen_header),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )
    }
}

@Composable
fun HeaderFooter(lastLogin: String) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icons8_time_38),
                    contentDescription = "",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
            Spacer(modifier = Modifier.size(20.dp))
            Column {
                Text(
                    text = "Último inicio de sesión:",
                    color = MaterialTheme.colorScheme.scrim
                )
                Text(
                    text = lastLogin,
                    color = MaterialTheme.colorScheme.scrim

                )
            }
        }
    }
}

//TODO: ANIMATION COMPONENTS
@Composable
fun OrderSuccessful() {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.Url("https://lottie.host/351beb02-ab0c-4715-bee0-bf9498a0a5ae/rsGvAATjm3.json"))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}