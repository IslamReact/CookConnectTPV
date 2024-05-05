package com.islamelmrabet.cookconnect.ui.screens.commonScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.sharp.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController : NavController) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    var expandedState by remember { mutableStateOf(false) }
    val options = listOf("Opción 1", "Opción 2", "Opción 3")
    var selectedItem by remember { mutableStateOf(options[0]) }


    Column(
        modifier = Modifier.padding(start = 25.dp, top = 60.dp, end = 16.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Primera row: Bienvenido a ItemPOS
        Text(
            text = stringResource(id = R.string.welcome),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.5.sp
        )

        // Segunda row: Descripción
        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = stringResource(id = R.string.welcome_text),
            fontSize = 15.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Justify
        )

        // Tercera row: Lista desplegable
        Row (
            modifier = Modifier
                .padding(vertical = 30.dp)
                .fillMaxWidth(),
        ){
            ExposedDropdownMenuBox(
                expanded = expandedState,
                onExpandedChange = { expandedState = !expandedState },
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextField(
                    value = selectedItem,
                    modifier = Modifier.menuAnchor(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState)}
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

        // Cuarta row vacía
        Spacer(modifier = Modifier.weight(1f))

        // Quinta row: Botones
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { },
                shape = lessRoundedShape,
                modifier = Modifier
                    .height(50.dp)
                    .width(160.dp),
                colors = buttonColors,
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.register),
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
            Spacer(modifier = Modifier.width(15.dp))
            Button(
                onClick = { navController.navigate(Routes.LogInScreen.route)},
                shape = lessRoundedShape,
                modifier = Modifier
                    .height(50.dp)
                    .width(160.dp),
                colors = buttonColors,
                content = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.login),
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
    }
}
