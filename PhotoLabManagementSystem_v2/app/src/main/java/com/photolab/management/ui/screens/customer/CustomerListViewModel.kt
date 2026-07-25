package com.photolab.management.ui.screens.customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photolab.management.data.database.entity.CustomerEntity
import com.photolab.management.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class CustomerListUiState(
    val customers: List<CustomerEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true
)

@HiltViewModel
class CustomerListViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(true)

    val uiState: StateFlow<CustomerListUiState> = searchQuery
        .debounce(250)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            _isLoading.value = true
            if (query.isBlank()) customerRepository.getAllCustomers()
            else customerRepository.search(query)
        }
        .combine(searchQuery) { customers, query -> customers to query }
        .map { (customers, query) ->
            _isLoading.value = false
            CustomerListUiState(customers = customers, searchQuery = query, isLoading = false)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CustomerListUiState())

    fun onSearchChange(query: String) {
        searchQuery.value = query
    }
}
