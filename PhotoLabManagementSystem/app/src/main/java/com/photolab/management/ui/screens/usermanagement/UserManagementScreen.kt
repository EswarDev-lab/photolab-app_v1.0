package com.photolab.management.ui.screens.usermanagement

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.photolab.management.data.database.entity.UserEntity
import com.photolab.management.data.database.entity.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(
    onBack: () -> Unit,
    onAddUser: () -> Unit,
    viewModel: UserManagementViewModel = hiltViewModel()
) {
    val users by viewModel.users.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Management") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddUser) {
                Icon(Icons.Filled.Add, contentDescription = "Add user")
            }
        }
    ) { padding ->
        if (users.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No staff accounts yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            return@Scaffold
        }

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(padding).fillMaxSize()
        ) {
            items(users, key = { it.userId }) { user ->
                UserRow(user = user, onToggleEnabled = { enabled -> viewModel.setUserEnabled(user.userId, enabled) })
            }
        }
    }
}

@Composable
private fun UserRow(user: UserEntity, onToggleEnabled: (Boolean) -> Unit) {
    Card(shape = RoundedCornerShape(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(user.fullName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                Text(
                    "@${user.username} · ${if (user.role == UserRole.ADMIN) "Admin" else "Staff"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = user.isEnabled, onCheckedChange = onToggleEnabled)
        }
    }
}
