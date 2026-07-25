package com.photolab.management.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.photolab.management.data.database.entity.UserRole
import com.photolab.management.ui.components.StatCard
import com.photolab.management.ui.theme.ErrorRed
import com.photolab.management.ui.theme.InfoBlue
import com.photolab.management.ui.theme.SuccessGreen
import com.photolab.management.ui.theme.WarningAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNewOrder: () -> Unit,
    onNewCustomer: () -> Unit,
    onViewReports: () -> Unit,
    onViewStock: () -> Unit,
    onOpenSettings: () -> Unit = {},
    onManageUsers: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Dashboard", style = MaterialTheme.typography.titleLarge)
                        if (uiState.fullName.isNotBlank()) {
                            Text(
                                "Welcome back, ${uiState.fullName}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = onNewOrder, icon = {
                Icon(Icons.Filled.Add, contentDescription = null)
            }, text = { Text("New Order") })
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Text("Today's Overview", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }

            item {
                StatCard(
                    title = "Today's Orders",
                    value = uiState.stats.todaysOrders.toString(),
                    icon = Icons.Filled.ShoppingCart,
                    accentColor = InfoBlue
                )
            }
            item {
                StatCard(
                    title = "Today's Revenue",
                    value = "₹%.0f".format(uiState.stats.todaysRevenue),
                    icon = Icons.Filled.CurrencyRupee,
                    accentColor = SuccessGreen
                )
            }
            item {
                StatCard(
                    title = "Ready Orders",
                    value = uiState.stats.readyOrders.toString(),
                    icon = Icons.Filled.CheckCircle,
                    accentColor = SuccessGreen
                )
            }
            item {
                StatCard(
                    title = "Delivered",
                    value = uiState.stats.deliveredOrders.toString(),
                    icon = Icons.Filled.LocalShipping,
                    accentColor = InfoBlue
                )
            }
            item {
                StatCard(
                    title = "Pending Payments",
                    value = uiState.stats.pendingPayments.toString(),
                    icon = Icons.Filled.Warning,
                    accentColor = WarningAmber
                )
            }
            item {
                StatCard(
                    title = "Low Stock Alerts",
                    value = uiState.lowStockProducts.size.toString(),
                    icon = Icons.Filled.Inventory2,
                    accentColor = ErrorRed
                )
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Spacer(Modifier.height(8.dp))
                Text("Quick Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    QuickActionButton("New Customer", Icons.Filled.PersonAdd, Modifier.weight(1f), onNewCustomer)
                    QuickActionButton("Stock", Icons.Filled.Inventory, Modifier.weight(1f), onViewStock)
                    QuickActionButton("Reports", Icons.Filled.BarChart, Modifier.weight(1f), onViewReports)
                    if (uiState.role == UserRole.ADMIN) {
                        QuickActionButton("Admin", Icons.Filled.AdminPanelSettings, Modifier.weight(1f), onManageUsers)
                    }
                }
            }

            if (uiState.lowStockProducts.isNotEmpty()) {
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    Spacer(Modifier.height(8.dp))
                    Text("Low Stock Products", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                }
                items(uiState.lowStockProducts, span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) { product ->
                    Card(shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(product.name, fontWeight = FontWeight.Medium)
                                Text(
                                    "Code: ${product.productCode}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            AssistChip(onClick = {}, label = { Text("${product.currentStock.toInt()} left") })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(onClick = onClick, modifier = modifier, contentPadding = PaddingValues(vertical = 12.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(20.dp))
            Spacer(Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
        }
    }
}
