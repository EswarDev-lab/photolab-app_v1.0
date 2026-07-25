package com.photolab.management.data.repository

import com.photolab.management.data.database.dao.ExpenseDao
import com.photolab.management.data.database.dao.OrderDao
import com.photolab.management.data.database.dao.ProductDao
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

data class DashboardStats(
    val todaysOrders: Int,
    val todaysRevenue: Double,
    val pendingPayments: Int,
    val readyOrders: Int,
    val deliveredOrders: Int,
    val lowStockCount: Int
)

@Singleton
class DashboardRepository @Inject constructor(
    private val orderDao: OrderDao,
    private val productDao: ProductDao,
    private val expenseDao: ExpenseDao
) {
    suspend fun getTodayStats(): DashboardStats {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0); cal.set(Calendar.MILLISECOND, 0)
        val startOfDay = cal.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000 - 1

        return DashboardStats(
            todaysOrders = orderDao.countOrdersToday(startOfDay, endOfDay),
            todaysRevenue = orderDao.revenueToday(startOfDay, endOfDay),
            pendingPayments = orderDao.countPendingPayments(),
            readyOrders = orderDao.countReadyOrders(),
            deliveredOrders = orderDao.countDeliveredOrders(),
            lowStockCount = 0 // populated reactively via productDao.getLowStockProducts() in the ViewModel
        )
    }
}
