package com.photolab.management.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

enum class PaymentMode { CASH, UPI, CARD, NET_BANKING, CHEQUE }

@Entity(
    tableName = "payments",
    foreignKeys = [ForeignKey(entity = OrderEntity::class, parentColumns = ["orderId"], childColumns = ["orderId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("orderId")]
)
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true) val paymentId: Long = 0,
    val orderId: Long,
    val amount: Double,
    val mode: PaymentMode,
    val paidAt: Long = System.currentTimeMillis(),
    val notes: String? = null
)
