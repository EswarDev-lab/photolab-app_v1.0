package com.photolab.management.ui.screens.usermanagement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photolab.management.data.database.entity.UserEntity
import com.photolab.management.data.database.entity.UserRole
import com.photolab.management.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NewUserFormState(
    val username: String = "",
    val password: String = "",
    val fullName: String = "",
    val phone: String = "",
    val role: UserRole = UserRole.STAFF,
    val usernameError: String? = null,
    val passwordError: String? = null,
    val fullNameError: String? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class UserManagementViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val users: StateFlow<List<UserEntity>> = authRepository.getAllUsers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _formState = MutableStateFlow(NewUserFormState())
    val formState: StateFlow<NewUserFormState> = _formState

    private val _userCreated = MutableStateFlow(false)
    val userCreated: StateFlow<Boolean> = _userCreated

    fun onUsernameChange(v: String) { _formState.value = _formState.value.copy(username = v, usernameError = null) }
    fun onPasswordChange(v: String) { _formState.value = _formState.value.copy(password = v, passwordError = null) }
    fun onFullNameChange(v: String) { _formState.value = _formState.value.copy(fullName = v, fullNameError = null) }
    fun onPhoneChange(v: String) { _formState.value = _formState.value.copy(phone = v) }
    fun onRoleChange(v: UserRole) { _formState.value = _formState.value.copy(role = v) }

    fun resetForm() {
        _formState.value = NewUserFormState()
        _userCreated.value = false
    }

    fun createUser() {
        val state = _formState.value
        var valid = true
        var usernameError: String? = null
        var passwordError: String? = null
        var fullNameError: String? = null

        if (state.username.isBlank() || state.username.length < 3) {
            usernameError = "Username must be at least 3 characters"; valid = false
        }
        if (state.password.length < 6) {
            passwordError = "Password must be at least 6 characters"; valid = false
        }
        if (state.fullName.isBlank()) {
            fullNameError = "Full name is required"; valid = false
        }
        if (!valid) {
            _formState.value = state.copy(
                usernameError = usernameError, passwordError = passwordError, fullNameError = fullNameError
            )
            return
        }

        viewModelScope.launch {
            _formState.value = state.copy(isSaving = true, errorMessage = null)
            try {
                authRepository.createUser(
                    username = state.username,
                    password = state.password,
                    fullName = state.fullName,
                    role = state.role,
                    phone = state.phone.ifBlank { null }
                )
                _formState.value = NewUserFormState()
                _userCreated.value = true
            } catch (e: Exception) {
                _formState.value = _formState.value.copy(
                    isSaving = false,
                    errorMessage = "Username already exists or is invalid"
                )
            }
        }
    }

    fun setUserEnabled(userId: Long, enabled: Boolean) {
        viewModelScope.launch { authRepository.setUserEnabled(userId, enabled) }
    }
}
