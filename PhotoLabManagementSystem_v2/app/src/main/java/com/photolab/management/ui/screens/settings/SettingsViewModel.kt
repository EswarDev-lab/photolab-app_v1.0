package com.photolab.management.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photolab.management.data.database.entity.CompanySettingsEntity
import com.photolab.management.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val settings: CompanySettingsEntity = CompanySettingsEntity(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val savedMessage: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.observeSettings().collect { settings ->
                _uiState.value = _uiState.value.copy(
                    settings = settings ?: CompanySettingsEntity(),
                    isLoading = false
                )
            }
        }
    }

    fun updateField(update: (CompanySettingsEntity) -> CompanySettingsEntity) {
        _uiState.value = _uiState.value.copy(settings = update(_uiState.value.settings), savedMessage = null)
    }

    fun onLogoPicked(uri: Uri) {
        val path = settingsRepository.persistLogo(uri)
        updateField { it.copy(logoPath = path) }
    }

    fun save() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            settingsRepository.saveSettings(_uiState.value.settings)
            _uiState.value = _uiState.value.copy(isSaving = false, savedMessage = "Settings saved")
        }
    }
}
