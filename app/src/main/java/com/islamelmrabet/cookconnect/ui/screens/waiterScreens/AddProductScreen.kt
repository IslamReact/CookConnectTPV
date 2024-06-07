package com.islamelmrabet.cookconnect.ui.screens.waiterScreens

import android.annotation.SuppressLint
import android.widget.Toast
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
import com.islamelmrabet.cookconnect.tools.AppBar
import com.islamelmrabet.cookconnect.tools.BasicLongButton
import com.islamelmrabet.cookconnect.managers.ProductManager
import com.islamelmrabet.cookconnect.viewModel.ProductViewModel
import kotlinx.coroutines.launch

/**
 * Composable screen AddProductScreen
 *
 * @param navController
 * @param productViewModel
 * @param productManager
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AddProductScreen(
    navController: NavHostController,
    productViewModel: ProductViewModel,
    productManager: ProductManager
) {

    val lessRoundedShape = RoundedCornerShape(8.dp)
    val primaryColor = MaterialTheme.colorScheme.primary

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = primaryColor
    )
    val categoryOptions = listOf(
        stringResource(id = R.string.drink),
        stringResource(id = R.string.sweet),
        stringResource(id = R.string.salt),
        stringResource(id = R.string.liquor),
        stringResource(id = R.string.vegetables)
    )
    var expandedState by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(categoryOptions[0]) }
    var isButtonEnabled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val (productName, setProductName) = remember { mutableStateOf("") }
    val (quantity, setQuantity) = remember { mutableIntStateOf(0) }
    val (unitPrice, setUnitPrice) = remember { mutableDoubleStateOf(0.00) }

    val onProductNameChange: (String) -> Unit = { setProductName(it) }
    val onQuantityChange: (Int) -> Unit = { setQuantity(it) }
    val onUnitPriceChange: (Double) -> Unit = { setUnitPrice(it) }


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
                stringResource(id = R.string.add_product_screen_header)
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
                    text = stringResource(id = R.string.product_name),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = productName,
                    onValueChange = onProductNameChange,
                    placeholder = {
                        Text(text = stringResource(id = R.string.product_placeHolder))
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Sharp.Fastfood, contentDescription = "fastFood")
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
                    text = stringResource(id = R.string.product_placeHolder),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = quantity.toString(),
                    onValueChange = { onQuantityChange(it.toIntOrNull() ?: 0) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Sharp.Numbers, contentDescription = "Numbers")
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
                    text = stringResource(id = R.string.unit_price),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = unitPrice.toString(),
                    onValueChange = { onUnitPriceChange(it.toDoubleOrNull() ?: 0.00) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Sharp.MonetizationOn, contentDescription = "Money")
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
                    text = stringResource(id = R.string.category),
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
                    BasicLongButton(
                        buttonText = stringResource(id = R.string.add_product),
                        onClick = {
                            scope.launch {
                                val exists = productViewModel.productExists(productName)
                                if (exists) {
                                    Toast.makeText(
                                        context,
                                        R.string.product_exists,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val product = Product(
                                        productName = productName,
                                        unitPrice = unitPrice,
                                        quantity = quantity,
                                        category = selectedItem
                                    )
                                    productViewModel.addProduct(product, productManager, context)
                                    setQuantity(0)
                                    setUnitPrice(0.00)
                                    setProductName("")
                                }
                            }
                        },
                        lessRoundedShape = lessRoundedShape,
                        buttonColors = buttonColors,
                        enabled = isButtonEnabled
                    )
                }
            }
        }
    )
}