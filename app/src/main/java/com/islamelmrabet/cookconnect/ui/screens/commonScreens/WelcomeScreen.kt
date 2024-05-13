package com.islamelmrabet.cookconnect.ui.screens.commonScreens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.BasicButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController : NavController) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    var expandedState by remember { mutableStateOf(false) }
    val options = remember { mutableStateListOf<String>() }
    var selectedItem by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val workersRef = Firebase.firestore.collection("workers")
        workersRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val namesAndRolesAndEmail = mutableListOf<String>()
                for (document in task.result) {
                    val name = document.getString("name")
                    val userRole = document.getString("userRole")
                    val email = document.getString("email")
                    if (name != null && userRole != null && email != null) {
                        namesAndRolesAndEmail.add("$name - $userRole - $email")
                    }
                }
                options.addAll(namesAndRolesAndEmail)
                if (options.isNotEmpty()) {
                    selectedItem = options[0]
                }
                loading = false
            } else {
                Log.d(TAG, "Error getting documents: ${task.exception?.message}")
            }
        }
    }

    Column(
        modifier = Modifier.padding(start = 25.dp, top = 60.dp, end = 25.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Primera row: Bienvenido a ItemPOS
        Text(
            text = stringResource(id = R.string.welcome),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.5.sp
        )

        // Segunda row: Descripción
        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = stringResource(id = R.string.welcome_text),
            fontSize = 20.sp,
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
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                // Mostrar menú desplegable solo cuando los datos están cargados
                ExposedDropdownMenuBox(
                    expanded = expandedState,
                    onExpandedChange = { expandedState = !expandedState },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    selectedItem?.let {
                        OutlinedTextField(
                            value = it,
                            modifier = Modifier.menuAnchor(),
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState)}
                        )
                    }

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
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_logo),
                contentDescription = "Welcome Logo",
                modifier = Modifier.size(240.dp)
            )
        }

        Spacer(modifier = Modifier.weight(0.5f))

        // Cuarta row: Botones
        Row{
            BasicButton(
                buttonText = stringResource(id = R.string.register),
                lessRoundedShape = lessRoundedShape,
                buttonColors = buttonColors,
                onClick = {
                    navController.navigate(Routes.CreateAccountScreen.route)
                }
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.2f))
            BasicButton(
                buttonText = stringResource(id = R.string.login),
                lessRoundedShape = lessRoundedShape,
                buttonColors = buttonColors,
                onClick = {
                    selectedItem?.let { selected ->
                        val email = selected.split(" - ")[2]
                        navController.navigate("${Routes.LogInScreen.route}/$email")
                    }
                }
            )
        }
    }
}

