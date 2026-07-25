package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: ProductEntity): Long

    @Update
    suspend fun update(product: ProductEntity)

    @Query("UPDATE products SET isActive = 0 WHERE productId = :productId")
    suspend fun deactivate(productId: Long)

    @Query("SELECT * FROM products WHERE isActive = 1 ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE barcode = :barcode LIMIT 1")
    suspend fun getByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM products WHERE isActive = 1 AND currentStock <= minimumStock ORDER BY name ASC")
    fun getLowStockProducts(): Flow<List<ProductEntity>>

    @Query("UPDATE products SET currentStock = currentStock + :delta WHERE productId = :productId")
    suspend fun adjustStock(productId: Long, delta: Double)

    @Query("""SELECT * FROM products WHERE isActive = 1 AND 
        (name LIKE '%' || :query || '%' OR productCode LIKE '%' || :query || '%' OR barcode LIKE '%' || :query || '%')""")
    fun search(query: String): Flow<List<ProductEntity>>
}
