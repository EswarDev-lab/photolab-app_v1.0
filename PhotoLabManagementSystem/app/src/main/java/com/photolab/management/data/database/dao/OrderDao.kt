package com.photolab.management.data.database.dao

import androidx.room.*
import com.photolab.management.data.database.entity.OrderEntity
import com.photolab.management.data.database.entity.OrderStatus
import com.photolab.management.data.database.entity.PaymentStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: OrderEntity): Long

    @Update
    suspend fun update(order: OrderEntity)

    @Query("SELECT * FROM orders ORDER BY bookingDate DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getById(orderId: Long): OrderEntity?

    @Query("SELECT * FROM orders WHERE orderStatus = :status ORDER BY bookingDate DESC")
    fun getByStatus(status: OrderStatus): Flow<List<OrderEntity>>

    @Query("UPDATE orders SET orderStatus = :status WHERE orderId = :orderId")
    suspend fun updateStatus(orderId: Long, status: OrderStatus)

    @Query("UPDATE orders SET paymentStatus = :status WHERE orderId = :orderId")
    suspend fun updatePaymentStatus(orderId: Long, status: PaymentStatus)

    @Query("SELECT COUNT(*) FROM orders WHERE bookingDate BETWEEN :startOfDay AND :endOfDay")
    suspend fun countOrdersToday(startOfDay: Long, endOfDay: Long): Int

    @Query("SELECT COALESCE(SUM(finalAmount),0) FROM orders WHERE bookingDate BETWEEN :startOfDay AND :endOfDay")
    suspend fun revenueToday(startOfDay: Long, endOfDay: Long): Double

    @Query("SELECT COUNT(*) FROM orders WHERE paymentStatus != 'PAID'")
    suspend fun countPendingPayments(): Int

    @Query("SELECT COUNT(*) FROM orders WHERE orderStatus = 'READY'")
    suspend fun countReadyOrders(): Int

    @Query("SELECT COUNT(*) FROM orders WHERE orderStatus = 'DELIVERED'")
    suspend fun countDeliveredOrders(): Int
}
