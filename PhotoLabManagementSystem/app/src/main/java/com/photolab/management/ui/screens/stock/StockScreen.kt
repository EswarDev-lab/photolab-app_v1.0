package com.photolab.management.ui.screens.stock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.photolab.management.data.database.entity.ProductEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockScreen(
    onBack: () -> Unit,
    viewModel: StockViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    val actionMessage by viewModel.actionMessage.collectAsState()
    var adjustingProduct by remember { mutableStateOf<ProductEntity?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(actionMessage) {
        actionMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stock Management") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (products.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No products yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            return@Scaffold
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            items(products, key = { it.productId }) { product ->
                StockRow(product = product, onAdjust = { adjustingProduct = product })
            }
        }
    }

    adjustingProduct?.let { product ->
        StockAdjustDialog(
            product = product,
            onDismiss = { adjustingProduct = null },
            onConfirm = { qty, isIn, note ->
                viewModel.adjustStock(product, qty, isIn, note)
                adjustingProduct = null
            }
        )
    }
}

@Composable
private fun StockRow(product: ProductEntity, onAdjust: () -> Unit) {
    val lowStock = product.currentStock <= product.minimumStock
    Card(onClick = onAdjust, shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                Text(
                    "Code: ${product.productCode}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            AssistChip(
                onClick = onAdjust,
                label = { Text("${product.currentStock.toInt()} ${product.unit}") },
                colors = if (lowStock) AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    labelColor = MaterialTheme.colorScheme.onErrorContainer
                ) else AssistChipDefaults.assistChipColors()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StockAdjustDialog(
    product: ProductEntity,
    onDismiss: () -> Unit,
    onConfirm: (quantity: Double, isStockIn: Boolean, note: String) -> Unit
) {
    var quantityText by remember { mutableStateOf("") }
    var isStockIn by remember { mutableStateOf(true) }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adjust Stock — ${product.name}") },
        text = {
            Column {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = isStockIn, onClick = { isStockIn = true }, label = { Text("Stock In") })
                    FilterChip(selected = !isStockIn, onClick = { isStockIn = false }, label = { Text("Stock Out") })
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = quantityText,
                    onValueChange = { quantityText = it },
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Note (optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val qty = quantityText.toDoubleOrNull() ?: 0.0
                onConfirm(qty, isStockIn, note)
            }) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
