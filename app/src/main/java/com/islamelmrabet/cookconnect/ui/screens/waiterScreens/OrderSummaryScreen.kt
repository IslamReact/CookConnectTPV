package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Fastfood
import androidx.compose.material.icons.sharp.MonetizationOn
import androidx.compose.material.icons.sharp.Numbers
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.islamelmrabet.cookconnect.R
import com.islamelmrabet.cookconnect.model.firebaseModels.Product
import com.islamelmrabet.cookconnect.navigation.Routes
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.BasicLongButton
import com.islamelmrabet.cookconnect.tools.OutlinedBasicButton
import com.islamelmrabet.cookconnect.utils.AuthManager
import com.islamelmrabet.cookconnect.utils.ProductManager
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderSummaryScreen(navController: NavHostController){

    val lessRoundedShape = RoundedCornerShape(8.dp)

    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = MaterialTheme.colorScheme.primary,
    )

    Scaffold(
        topBar = {
            AppBar(
                navController,
                stringResource(id = R.string.order_summary_header),
                Routes.InventoryScreen.route
            )
        },content = { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier
                       .fillMaxWidth()
                       .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Efectivo",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                    )
                    Checkbox(checked = true, onCheckedChange = {})
                }
            }
        },
        bottomBar = {
            BottomAppBar (
                modifier = Modifier
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Subtotal",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "$46.87",
                            fontWeight = FontWeight.Bold
                        )
                    }
                    BasicLongButton(
                        buttonText = "Cobrar",
                        onClick = { /*TODO*/ },
                        lessRoundedShape = lessRoundedShape,
                        buttonColors = buttonColors,
                        enabled = true
                    )
                }
            }
        }
    )
}

@Composable
fun OrderListSummary(){
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {

    }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Subtotal")
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "$46.87")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ){

        }
    }

}
