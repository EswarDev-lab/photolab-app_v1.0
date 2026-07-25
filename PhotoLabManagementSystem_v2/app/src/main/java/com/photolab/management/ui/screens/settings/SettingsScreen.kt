package com.photolab.management.ui.screens.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val settings = uiState.settings

    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let(viewModel::onLogoPicked) }

    LaunchedEffect(uiState.savedMessage) {
        if (uiState.savedMessage != null) {
            delay(2000)
            // message auto-clears on next field edit; nothing else required here
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text("Company Logo", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            ) {
                if (settings.logoPath != null) {
                    AsyncImage(
                        model = settings.logoPath,
                        contentDescription = "Company logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Surface(color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.fillMaxSize()) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Filled.PhotoCamera, contentDescription = null)
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { logoPickerLauncher.launch("image/*") }) {
                Text(if (settings.logoPath != null) "Change Logo" else "Upload Logo")
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))

            Text("Company Profile", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = settings.companyName,
                onValueChange = { v -> viewModel.updateField { it.copy(companyName = v) } },
                label = { Text("Company Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = settings.address ?: "",
                onValueChange = { v -> viewModel.updateField { it.copy(address = v) } },
                label = { Text("Address") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = settings.phone ?: "",
                onValueChange = { v -> viewModel.updateField { it.copy(phone = v) } },
                label = { Text("Phone") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = settings.email ?: "",
                onValueChange = { v -> viewModel.updateField { it.copy(email = v) } },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = settings.gstNumber ?: "",
                onValueChange = { v -> viewModel.updateField { it.copy(gstNumber = v) } },
                label = { Text("GST Number") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = settings.invoicePrefix,
                onValueChange = { v -> viewModel.updateField { it.copy(invoicePrefix = v) } },
                label = { Text("Invoice Prefix") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = settings.receiptFooter ?: "",
                onValueChange = { v -> viewModel.updateField { it.copy(receiptFooter = v) } },
                label = { Text("Receipt Footer Message") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = settings.isDarkMode,
                    onCheckedChange = { v -> viewModel.updateField { it.copy(isDarkMode = v) } }
                )
                Spacer(Modifier.width(8.dp))
                Text("Dark Mode")
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = viewModel::save,
                enabled = !uiState.isSaving,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Text("Save Settings")
                }
            }

            if (uiState.savedMessage != null) {
                Spacer(Modifier.height(8.dp))
                Text(uiState.savedMessage!!, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
