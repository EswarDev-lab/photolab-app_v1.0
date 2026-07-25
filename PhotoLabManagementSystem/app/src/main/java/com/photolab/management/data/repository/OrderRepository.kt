package com.photolab.management.data.repository

import com.photolab.management.data.database.dao.OrderDao
import com.photolab.management.data.database.dao.OrderItemDao
import com.photolab.management.data.database.entity.OrderEntity
import com.photolab.management.data.database.entity.OrderItemEntity
import com.photolab.management.data.database.entity.OrderStatus
import com.photolab.management.data.database.entity.PaymentStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val orderItemDao: OrderItemDao
) {
    fun getAllOrders() = orderDao.getAllOrders()
    fun getByStatus(status: OrderStatus) = orderDao.getByStatus(status)
    fun getItemsForOrder(orderId: Long) = orderItemDao.getItemsForOrder(orderId)

    /** Generates a sequential order number like PL-2026-000123 */
    suspend fun generateOrderNumber(): String {
        val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val timestamp = System.currentTimeMillis() % 1000000
        return "PL-$year-%06d".format(timestamp)
    }

    suspend fun createOrder(order: OrderEntity, items: List<OrderItemEntity>): Long {
        val orderId = orderDao.insert(order)
        orderItemDao.insertAll(items.map { it.copy(orderId = orderId) })
        return orderId
    }

    suspend fun updateOrderStatus(orderId: Long, status: OrderStatus) = orderDao.updateStatus(orderId, status)
    suspend fun updatePaymentStatus(orderId: Long, status: PaymentStatus) = orderDao.updatePaymentStatus(orderId, status)
}
