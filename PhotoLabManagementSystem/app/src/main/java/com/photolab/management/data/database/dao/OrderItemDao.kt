package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.OrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDao {
    @Insert
    suspend fun insertAll(items: List<OrderItemEntity>)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getItemsForOrder(orderId: Long): Flow<List<OrderItemEntity>>

    @Query("""SELECT productId, SUM(quantity) as totalQty FROM order_items 
        GROUP BY productId ORDER BY totalQty DESC LIMIT :limit""")
    suspend fun getTopSellingProductIds(limit: Int): List<ProductQty>
}

data class ProductQty(val productId: Long, val totalQty: Double)
