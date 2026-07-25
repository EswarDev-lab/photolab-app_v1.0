package com.photolab.management.ui.screens.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photolab.management.data.database.entity.CustomerEntity
import com.photolab.management.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Patterns

data class AddEditCustomerUiState(
    val name: String = "",
    val phone: String = "",
    val whatsapp: String = "",
    val email: String = "",
    val address: String = "",
    val gstNumber: String = "",
    val notes: String = "",
    val nameError: String? = null,
    val phoneError: String? = null,
    val emailError: String? = null,
    val gstError: String? = null,
    val isSaving: Boolean = false,
    val saveError: String? = null,
    val saveSuccess: Boolean = false
)

private val GST_REGEX = Regex("^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$")

@HiltViewModel
class AddEditCustomerViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditCustomerUiState())
    val uiState: StateFlow<AddEditCustomerUiState> = _uiState.asStateFlow()

    fun onNameChange(v: String) { _uiState.value = _uiState.value.copy(name = v, nameError = null) }
    fun onPhoneChange(v: String) { _uiState.value = _uiState.value.copy(phone = v, phoneError = null) }
    fun onWhatsappChange(v: String) { _uiState.value = _uiState.value.copy(whatsapp = v) }
    fun onEmailChange(v: String) { _uiState.value = _uiState.value.copy(email = v, emailError = null) }
    fun onAddressChange(v: String) { _uiState.value = _uiState.value.copy(address = v) }
    fun onGstChange(v: String) { _uiState.value = _uiState.value.copy(gstNumber = v.uppercase(), gstError = null) }
    fun onNotesChange(v: String) { _uiState.value = _uiState.value.copy(notes = v) }

    private fun validate(): Boolean {
        val state = _uiState.value
        var valid = true
        var nameError: String? = null
        var phoneError: String? = null
        var emailError: String? = null
        var gstError: String? = null

        if (state.name.isBlank()) {
            nameError = "Customer name is required"; valid = false
        }
        if (state.phone.isBlank() || !Patterns.PHONE.matcher(state.phone).matches() || state.phone.length < 10) {
            phoneError = "Enter a valid phone number"; valid = false
        }
        if (state.email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            emailError = "Enter a valid email address"; valid = false
        }
        if (state.gstNumber.isNotBlank() && !GST_REGEX.matches(state.gstNumber)) {
            gstError = "Enter a valid 15-character GSTIN"; valid = false
        }

        _uiState.value = state.copy(
            nameError = nameError, phoneError = phoneError, emailError = emailError, gstError = gstError
        )
        return valid
    }

    fun save() {
        if (!validate()) return
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, saveError = null)
            val result = customerRepository.addCustomer(
                CustomerEntity(
                    name = state.name.trim(),
                    phone = state.phone.trim(),
                    whatsappNumber = state.whatsapp.ifBlank { null },
                    email = state.email.ifBlank { null },
                    address = state.address.ifBlank { null },
                    gstNumber = state.gstNumber.ifBlank { null },
                    notes = state.notes.ifBlank { null }
                )
            )
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(isSaving = false, saveSuccess = true) },
                onFailure = { e -> _uiState.value = _uiState.value.copy(isSaving = false, saveError = e.message) }
            )
        }
    }
}
