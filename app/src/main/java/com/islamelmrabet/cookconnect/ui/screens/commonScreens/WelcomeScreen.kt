package com.islamelmrabet.cookconnect.ui.screens.commonScreens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.BasicButton

/**
 * Composable screen WelcomeScreen
 *
 * @param navController
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(navController: NavController) {
    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url("https://lottie.host/22030422-d085-49d7-9886-4a48803576e6/HJkT0Z7bVp.json")
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
        Text(
            text = stringResource(id = R.string.welcome),
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.5.sp
        )
        Text(
            modifier = Modifier.padding(top = 15.dp),
            text = stringResource(id = R.string.welcome_text),
            fontSize = 20.sp,
            lineHeight = 22.sp,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Justify
        )
        Row(
            modifier = Modifier
                .padding(vertical = 30.dp)
                .fillMaxWidth(),
        ) {
            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever
                    )
                }
            } else {
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
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedState) }
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

        Row {
            BasicButton(
                buttonText = stringResource(id = R.string.register),
                lessRoundedShape = lessRoundedShape,
                buttonColors = buttonColors,
                onClick = {
                    navController.navigate(Routes.CreateAccountScreen.route)
                }
            )
            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
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

