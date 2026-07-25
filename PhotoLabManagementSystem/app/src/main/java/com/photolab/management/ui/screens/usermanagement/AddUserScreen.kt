package com.photolab.management.ui.screens.usermanagement

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.photolab.management.data.database.entity.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: UserManagementViewModel = hiltViewModel()
) {
    val formState by viewModel.formState.collectAsState()
    val userCreated by viewModel.userCreated.collectAsState()

    LaunchedEffect(userCreated) {
        if (userCreated) onSaved()
    }
    LaunchedEffect(Unit) { viewModel.resetForm() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Staff Account") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = formState.fullName,
                onValueChange = viewModel::onFullNameChange,
                label = { Text("Full Name *") },
                isError = formState.fullNameError != null,
                supportingText = { formState.fullNameError?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = formState.username,
                onValueChange = viewModel::onUsernameChange,
                label = { Text("Username *") },
                isError = formState.usernameError != null,
                supportingText = { formState.usernameError?.let { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = formState.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password *") },
                isError = formState.passwordError != null,
                supportingText = { formState.passwordError?.let { Text(it) } },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = formState.phone,
                onValueChange = viewModel::onPhoneChange,
                label = { Text("Phone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Text("Role", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = formState.role == UserRole.STAFF,
                    onClick = { viewModel.onRoleChange(UserRole.STAFF) },
                    label = { Text("Staff") }
                )
                FilterChip(
                    selected = formState.role == UserRole.ADMIN,
                    onClick = { viewModel.onRoleChange(UserRole.ADMIN) },
                    label = { Text("Admin") }
                )
            }

            if (formState.errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Text(formState.errorMessage!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = viewModel::createUser,
                enabled = !formState.isSaving,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                if (formState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                } else {
                    Text("Create Account")
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
