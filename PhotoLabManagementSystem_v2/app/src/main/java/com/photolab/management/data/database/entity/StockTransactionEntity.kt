package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class StockTransactionType { PURCHASE_IN, SALE_OUT, ADJUSTMENT, DAMAGE, RETURN, TRANSFER }

@Entity(
    tableName = "stock_transactions",
    foreignKeys = [ForeignKey(entity = ProductEntity::class, parentColumns = ["productId"], childColumns = ["productId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("productId")]
)
data class StockTransactionEntity(
    @PrimaryKey(autoGenerate = true) val transactionId: Long = 0,
    val productId: Long,
    val type: StockTransactionType,
    val quantity: Double,
    val note: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val createdByUserId: Long
)
