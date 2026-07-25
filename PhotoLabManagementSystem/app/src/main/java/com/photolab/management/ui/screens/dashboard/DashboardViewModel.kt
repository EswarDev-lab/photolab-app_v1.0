package com.photolab.management.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photolab.management.data.database.entity.ProductEntity
import com.photolab.management.data.database.entity.UserRole
import com.photolab.management.data.repository.DashboardRepository
import com.photolab.management.data.repository.DashboardStats
import com.photolab.management.data.repository.ProductRepository
import com.photolab.management.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val fullName: String = "",
    val role: UserRole = UserRole.STAFF,
    val stats: DashboardStats = DashboardStats(0, 0.0, 0, 0, 0, 0),
    val lowStockProducts: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val productRepository: ProductRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            val stats = dashboardRepository.getTodayStats()
            _uiState.value = _uiState.value.copy(stats = stats)
        }

        viewModelScope.launch {
            combine(
                sessionManager.fullName,
                sessionManager.role,
                productRepository.getLowStockProducts()
            ) { name, role, lowStock ->
                _uiState.value.copy(
                    fullName = name ?: "",
                    role = role ?: UserRole.STAFF,
                    lowStockProducts = lowStock,
                    isLoading = false
                )
            }.collect { newState -> _uiState.value = newState }
        }
    }

    fun refresh() = loadDashboard()
}
