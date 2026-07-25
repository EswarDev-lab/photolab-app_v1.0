package com.photolab.management.ui.screens.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photolab.management.data.database.entity.ProductEntity
import com.photolab.management.data.database.entity.StockTransactionType
import com.photolab.management.data.repository.ProductRepository
import com.photolab.management.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val products: StateFlow<List<ProductEntity>> = productRepository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage

    fun clearMessage() { _actionMessage.value = null }

    fun adjustStock(product: ProductEntity, quantity: Double, isStockIn: Boolean, note: String) {
        if (quantity <= 0) {
            _actionMessage.value = "Enter a quantity greater than zero"
            return
        }
        viewModelScope.launch {
            val userId = sessionManager.userId.first() ?: return@launch
            val delta = if (isStockIn) quantity else -quantity
            val type = if (isStockIn) StockTransactionType.PURCHASE_IN else StockTransactionType.ADJUSTMENT
            productRepository.adjustStock(product.productId, delta, type, userId, note.ifBlank { null })
            _actionMessage.value = "${if (isStockIn) "Added" else "Removed"} $quantity ${product.unit} for ${product.name}"
        }
    }
}
