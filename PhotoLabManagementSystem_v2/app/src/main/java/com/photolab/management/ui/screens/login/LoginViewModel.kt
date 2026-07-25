package com.photolab.management.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photolab.management.data.database.entity.UserRole
import com.photolab.management.data.repository.AuthRepository
import com.photolab.management.data.repository.LoginResult
import com.photolab.management.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val rememberLogin: Boolean = true,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val loggedInRole: UserRole? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value, errorMessage = null)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value, errorMessage = null)
    }

    fun onRememberToggle(value: Boolean) {
        _uiState.value = _uiState.value.copy(rememberLogin = value)
    }

    fun login() {
        val state = _uiState.value
        if (state.username.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter both username and password")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            when (val result = authRepository.login(state.username, state.password)) {
                is LoginResult.Success -> {
                    sessionManager.saveSession(
                        userId = result.user.userId,
                        username = result.user.username,
                        fullName = result.user.fullName,
                        role = result.user.role,
                        remember = state.rememberLogin
                    )
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        loginSuccess = true,
                        loggedInRole = result.user.role
                    )
                }
                LoginResult.InvalidCredentials -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Invalid username or password"
                    )
                }
                LoginResult.AccountDisabled -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "This account has been disabled. Contact your admin."
                    )
                }
            }
        }
    }
}
