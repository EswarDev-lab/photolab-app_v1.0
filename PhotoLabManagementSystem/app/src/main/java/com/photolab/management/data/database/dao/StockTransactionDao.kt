package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.StockTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StockTransactionDao {
    @Insert
    suspend fun insert(transaction: StockTransactionEntity): Long

    @Query("SELECT * FROM stock_transactions WHERE productId = :productId ORDER BY createdAt DESC")
    fun getForProduct(productId: Long): Flow<List<StockTransactionEntity>>

    @Query("SELECT * FROM stock_transactions ORDER BY createdAt DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<StockTransactionEntity>>
}
