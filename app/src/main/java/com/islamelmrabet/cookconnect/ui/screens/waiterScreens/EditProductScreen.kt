package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Fastfood
import androidx.compose.material.icons.sharp.MonetizationOn
import androidx.compose.material.icons.sharp.Numbers
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
fun EditProductScreen(
    auth: AuthManager,
    navController: NavHostController,
    productViewModel: ProductViewModel,
    productManager: ProductManager,
    productNameToEdit: String?
) {

    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )

    val outlinedbuttonColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.error,
    )

    val categoryOptions = listOf("Bebida", "Dulce", "Salado", "Licores", "Verdura")
    var expandedState by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(categoryOptions[0]) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var productName by remember { mutableStateOf(productNameToEdit ?: "") }
    var quantity by remember { mutableIntStateOf(0) }
    var unitPrice by remember { mutableDoubleStateOf(0.00) }



    LaunchedEffect(productNameToEdit) {
        if (productNameToEdit != null) {
            val fetchedProduct = productViewModel.getProduct(productNameToEdit)
            fetchedProduct?.let {
                productName = it.productName
                quantity = it.quantity
                unitPrice = it.unitPrice
                selectedItem = it.category
            }
        }
    }

    LaunchedEffect(productName, quantity, unitPrice, selectedItem) {
        isButtonEnabled = productName.isNotBlank() &&
                quantity > 0 &&
                unitPrice > 0.00 &&
                selectedItem.isNotBlank()
    }


    Scaffold(
        topBar = {
            AppBar(
                navController,
                stringResource(id = R.string.edit_product_screen_header),
                Routes.InventoryScreen.route
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .padding(top = contentPadding.calculateTopPadding() + 30.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nombre del producto *",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                placeholder = {
                    Text(text = "Azucar Hacendado...")
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Sharp.Fastfood, contentDescription = "Hola")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Cantidad *",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = quantity.toString(),
                onValueChange = { quantity = it.toIntOrNull() ?: 0 },
                leadingIcon = {
                    Icon(imageVector = Icons.Sharp.Numbers, contentDescription = "Hola")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Precio unitario *",
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = unitPrice.toString(),
                onValueChange = { unitPrice = it.toDoubleOrNull() ?: 0.00 },
                leadingIcon = {
                    Icon(imageVector = Icons.Sharp.MonetizationOn, contentDescription = "Hola")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Categoria *",
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
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
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
                        categoryOptions.forEachIndexed { index, option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    selectedItem = categoryOptions[index]
                                    expandedState = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                modifier = Modifier
                    .padding(bottom = 16.dp),
            ) {
                OutlinedBasicButton(
                    buttonText = "Borrar Producto",
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = outlinedbuttonColors,
                    border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.error),
                    onClick = {
                        if (productNameToEdit != null) {
                            productViewModel.deleteProduct(productName, productManager, context)
                        }
                        navController.navigate(Routes.InventoryScreen.route)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
                BasicLongButton(
                    buttonText = "Modificar Producto",
                    onClick = {
                        val product = Product(
                            productName = productName,
                            unitPrice = unitPrice,
                            quantity = quantity,
                            category = selectedItem
                        )
                        if (productNameToEdit != null) {
                            productViewModel.updateProduct(
                                productNameToEdit,
                                product,
                                productManager,
                                context
                            )
                            navController.navigate(Routes.InventoryScreen.route)
                        }
                    },
                    lessRoundedShape = lessRoundedShape,
                    buttonColors = buttonColors,
                    enabled = isButtonEnabled
                )
            }
        }
    }
}