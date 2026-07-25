package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.PurchaseEntity
import com.photolab.management.data.database.entity.PurchaseItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {
    @Insert
    suspend fun insertPurchase(purchase: PurchaseEntity): Long

    @Insert
    suspend fun insertItems(items: List<PurchaseItemEntity>)

    @Query("SELECT * FROM purchases ORDER BY purchaseDate DESC")
    fun getAll(): Flow<List<PurchaseEntity>>

    @Query("SELECT * FROM purchase_items WHERE purchaseId = :purchaseId")
    fun getItems(purchaseId: Long): Flow<List<PurchaseItemEntity>>
}
