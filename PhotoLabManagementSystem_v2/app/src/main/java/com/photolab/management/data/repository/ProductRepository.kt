package com.photolab.management.data.repository

import com.photolab.management.data.database.dao.ProductDao
import com.photolab.management.data.database.dao.StockTransactionDao
import com.photolab.management.data.database.entity.ProductEntity
import com.photolab.management.data.database.entity.StockTransactionEntity
import com.photolab.management.data.database.entity.StockTransactionType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val productDao: ProductDao,
    private val stockTransactionDao: StockTransactionDao
) {
    fun getAllProducts() = productDao.getAllProducts()
    fun getLowStockProducts() = productDao.getLowStockProducts()
    fun search(query: String) = productDao.search(query)
    suspend fun getByBarcode(barcode: String) = productDao.getByBarcode(barcode)
    suspend fun addProduct(product: ProductEntity) = productDao.insert(product)
    suspend fun updateProduct(product: ProductEntity) = productDao.update(product)

    suspend fun adjustStock(
        productId: Long,
        delta: Double,
        type: StockTransactionType,
        userId: Long,
        note: String? = null
    ) {
        productDao.adjustStock(productId, delta)
        stockTransactionDao.insert(
            StockTransactionEntity(
                productId = productId,
                type = type,
                quantity = delta,
                note = note,
                createdByUserId = userId
            )
        )
    }
}
