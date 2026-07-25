package com.photolab.management.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.photolab.management.data.database.dao.*
import com.photolab.management.data.database.entity.*

@Database(
    entities = [
        UserEntity::class,
        CustomerEntity::class,
        CategoryEntity::class,
        ProductEntity::class,
        SupplierEntity::class,
        OrderEntity::class,
        OrderItemEntity::class,
        PaymentEntity::class,
        ExpenseEntity::class,
        StockTransactionEntity::class,
        PurchaseEntity::class,
        PurchaseItemEntity::class,
        CompanySettingsEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun customerDao(): CustomerDao
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun paymentDao(): PaymentDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun supplierDao(): SupplierDao
    abstract fun stockTransactionDao(): StockTransactionDao
    abstract fun purchaseDao(): PurchaseDao
    abstract fun companySettingsDao(): CompanySettingsDao

    companion object {
        const val DATABASE_NAME = "photolab_management.db"
    }
}
