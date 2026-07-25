package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.PaymentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {
    @Insert
    suspend fun insert(payment: PaymentEntity): Long

    @Query("SELECT * FROM payments WHERE orderId = :orderId ORDER BY paidAt DESC")
    fun getPaymentsForOrder(orderId: Long): Flow<List<PaymentEntity>>

    @Query("SELECT COALESCE(SUM(amount),0) FROM payments WHERE orderId = :orderId")
    suspend fun totalPaidForOrder(orderId: Long): Double
}
