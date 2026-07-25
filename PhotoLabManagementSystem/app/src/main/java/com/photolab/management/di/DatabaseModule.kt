package com.photolab.management.di

import android.content.Context
import androidx.room.Room
import com.photolab.management.data.database.AppDatabase
import com.photolab.management.data.database.DatabaseSeeder
import com.photolab.management.data.database.dao.*
import com.photolab.management.utils.SessionManager
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        databaseLazy: Lazy<AppDatabase>
    ): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            // Destructive fallback only while the schema is still evolving pre-release.
            // Once this app has real production data, replace with proper Migration objects.
            .fallbackToDestructiveMigration()
            .addCallback(DatabaseSeeder(databaseLazy))
            .build()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideCustomerDao(db: AppDatabase): CustomerDao = db.customerDao()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()

    @Provides
    fun provideCategoryDao(db: AppDatabase): CategoryDao = db.categoryDao()

    @Provides
    fun provideOrderDao(db: AppDatabase): OrderDao = db.orderDao()

    @Provides
    fun provideOrderItemDao(db: AppDatabase): OrderItemDao = db.orderItemDao()

    @Provides
    fun providePaymentDao(db: AppDatabase): PaymentDao = db.paymentDao()

    @Provides
    fun provideExpenseDao(db: AppDatabase): ExpenseDao = db.expenseDao()

    @Provides
    fun provideSupplierDao(db: AppDatabase): SupplierDao = db.supplierDao()

    @Provides
    fun provideStockTransactionDao(db: AppDatabase): StockTransactionDao = db.stockTransactionDao()

    @Provides
    fun providePurchaseDao(db: AppDatabase): PurchaseDao = db.purchaseDao()

    @Provides
    fun provideCompanySettingsDao(db: AppDatabase): CompanySettingsDao = db.companySettingsDao()

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager =
        SessionManager(context)
}
